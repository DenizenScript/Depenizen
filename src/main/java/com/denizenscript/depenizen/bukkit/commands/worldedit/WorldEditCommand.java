package com.denizenscript.depenizen.bukkit.commands.worldedit;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;

import java.io.*;
import java.net.URLDecoder;

public class WorldEditCommand extends AbstractCommand {

    // <--[command]
    // @Name worldedit
    // @Syntax worldedit [create_schematic/copy_to_clipboard/paste] (file:<file path>) (cuboid:<cuboid>) (position:<location>) (rotate:<#>) (undoable) (noair)
    // @Group Depenizen
    // @Plugin Depenizen, WorldEdit
    // @Required 2
    // @Short Controls schematics and clipboards in WorldEdit.
    //
    // @Description
    // Controls schematics and clipboards in WorldEdit.
    // The action can be create_schematic, copy_to_clipboard, or paste.
    //
    // For 'paste':
    // Specify 'noair' to exclude air blocks.
    // Specify 'rotate' to rotate the schematic when pasting it.
    // Specify 'undoable' to attach the paste to the player's WorldEdit history which allows them to undo/redo.
    //
    // For 'copy_to_clipboard':
    // Specify either a cuboid or a file.
    // The file path starts in the folder: /plugins/Denizen/schematics/
    //
    // For 'create_schematic':
    // Either specify a cuboid, or the player's clipboard will be used.
    // Specify a file to save to.
    //
    // @Tags
    // <PlayerTag.we_selection>
    //
    // @Usage
    // Use to save a cuboid to a schematic.
    // - worldedit create_schematic file:<[filepath]> cuboid:<player.we_selection> position:<player.location>
    //
    // @Usage
    // Use to copy a cuboid to a player's clipboard.
    // - worldedit copy_to_clipboard cuboid:<player.we_selection> position:<player.location>
    //
    // @Usage
    // Use to load a schematic into a player's clipboard.
    // - worldedit copy_to_clipboard file:<[filepath]>
    //
    // @Usage
    // Use to paste a schematic at a location
    // - worldedit paste file:<[filepath]> position:<player.location>
    //
    // @Usage
    // Use to paste a schematic at a location with a player attached to the edit history.
    // - worldedit paste file:<[filepath]> position:<player.location> undoable noair rotate:90
    //
    // -->

    private enum Action {CREATE_SCHEMATIC, COPY_TO_CLIPBOARD, PASTE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (Argument arg : scriptEntry.getProcessedArgs()) {

            if (!scriptEntry.hasObject("position")
                    && arg.matchesPrefix("position")) {
                scriptEntry.addObject("position", arg.asType(LocationTag.class));
            }
            else if (!scriptEntry.hasObject("file")
                    && arg.matchesPrefix("file")) {
                scriptEntry.addObject("file", arg.asElement());
            }
            else if (!scriptEntry.hasObject("cuboid")
                    && arg.matchesPrefix("cuboid")) {
                scriptEntry.addObject("cuboid", arg.asType(CuboidTag.class));
            }
            else if (!scriptEntry.hasObject("noair")
                    && arg.matches("noair")) {
                scriptEntry.addObject("noair", new ElementTag(true));
            }
            else if (!scriptEntry.hasObject("undoable")
                    && arg.matches("undoable")) {
                scriptEntry.addObject("undoable", new ElementTag(true));
            }
            else if (!scriptEntry.hasObject("rotate")
                    && arg.matchesPrefix("rotate")) {
                scriptEntry.addObject("rotate", arg.asElement());
            }
            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified!");
        }
    }

    public CuboidRegion cuboidToWECuboid(CuboidTag cuboid) {
        LocationTag top = cuboid.getHigh(0);
        LocationTag bottom = cuboid.getLow(0);
        BlockVector3 topVector = BlockVector3.at(top.getBlockX(), top.getBlockY(), top.getBlockZ());
        BlockVector3 bottomVector = BlockVector3.at(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
        World w = new BukkitWorld(cuboid.getWorld());
        return new CuboidRegion(w, bottomVector, topVector);
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        ElementTag action = scriptEntry.getObjectTag("action");
        ElementTag file = scriptEntry.getObjectTag("file");
        LocationTag position = scriptEntry.getObjectTag("position");
        ElementTag noAir = scriptEntry.getObjectTag("noair");
        CuboidTag cuboid = scriptEntry.getObjectTag("cuboid");
        ElementTag undoable = scriptEntry.getObjectTag("undoable");
        ElementTag rotate = scriptEntry.getObjectTag("rotate");

        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action.debug()
                    + (file != null ? file.debug() : "")
                    + (position != null ? position.debug() : "")
                    + (noAir != null ? noAir.debug() : "")
                    + (cuboid != null ? cuboid.debug() : "")
                    + (undoable != null ? undoable.debug() : "")
                    + (rotate != null ? rotate.debug() : ""));
        }

        PlayerTag target = Utilities.getEntryPlayer(scriptEntry);

        if (action.asString().equalsIgnoreCase("paste")) {
            if (file == null) {
                Debug.echoError("File path not specified");
                return;
            }
            if (position == null) {
                Debug.echoError("Position not specified");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            File fileToLoad = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
            if (!Utilities.canReadFile(fileToLoad)) {
                Debug.echoError("Permission to read file '" + file + "' denied by config.");
                return;
            }
            if (!fileToLoad.exists()) {
                Debug.echoError("File not found");
                return;
            }
            ClipboardFormat format = ClipboardFormats.findByFile(fileToLoad);
            if (format == null) {
                Debug.echoError("File not found");
                return;
            }
            Clipboard clipboard;
            try {
                ClipboardReader reader = format.getReader(new FileInputStream(fileToLoad));
                clipboard = reader.read();
            }
            catch (IOException ex) {
                Debug.echoError(ex);
                return;
            }
            if (clipboard == null) {
                Debug.echoError("Clipboard became null");
                return;
            }
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            if (rotate != null) {
                AffineTransform transform = new AffineTransform();
                transform = transform.rotateY(rotate.asInt());
                holder.setTransform(holder.getTransform().combine(transform));
            }
            World weWorld = new BukkitWorld(position.getWorld());
            if (undoable != null) {
                if (target == null) {
                    Debug.echoError("Player not found in queue");
                    return;
                }
                Player wePlayer = BukkitAdapter.adapt(target.getPlayerEntity());
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1, null, wePlayer);
                Operation operation = holder.createPaste(editSession)
                        .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                        .ignoreAirBlocks(noAir != null).build();
                try {
                    Operations.complete(operation);
                }
                catch (WorldEditException ex) {
                    Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard");
                    Debug.echoError(ex);
                    return;
                }
                editSession.flushSession();
                WorldEdit.getInstance().getSessionManager().get(wePlayer).remember(editSession);
            }
            else {
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
                Operation operation = holder.createPaste(editSession)
                        .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                        .ignoreAirBlocks(noAir != null).build();
                try {
                    Operations.complete(operation);
                }
                catch (WorldEditException ex) {
                    Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard");
                    Debug.echoError(ex);
                    return;
                }
                editSession.flushSession();
            }
        }
        else if (action.asString().equalsIgnoreCase("create_schematic")) {
            if (file == null) {
                Debug.echoError("File not specified");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            File fileToSave = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
            if (!Utilities.canWriteToFile(fileToSave)) {
                Debug.echoError("Permission to write file '" + file + "' denied by config.");
                return;
            }
            if (cuboid == null) {
                if (target == null) {
                    Debug.echoError("Cuboid not specified");
                    return;
                }
                Player wePlayer = BukkitAdapter.adapt(target.getPlayerEntity());
                try {
                    ClipboardHolder clipboard = WorldEdit.getInstance().getSessionManager().get(wePlayer).getClipboard();
                    try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(fileToSave))) {
                        writer.write(clipboard.getClipboard());
                    }
                    catch (IOException ex) {
                        Debug.echoError(ex);
                    }
                }
                catch (EmptyClipboardException ex) {
                    Debug.echoError("Cuboid not specified, and player does not have a clipboard");
                    return;
                }
                return;
            }
            CuboidRegion region = cuboidToWECuboid(cuboid);
            if (position == null) {
                Debug.echoError("Position not specified");
                return;
            }
            BlockVector3 pos = BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ());
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            clipboard.setOrigin(pos);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            forwardExtentCopy.setCopyingEntities(false);
            try {
                Operations.complete(forwardExtentCopy);
            }
            catch (WorldEditException ex) {
                Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard");
                Debug.echoError(ex);
                return;
            }
            try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(fileToSave))) {
                writer.write(clipboard);
            }
            catch (IOException ex) {
                Debug.echoError(ex);
            }
        }
        else if (action.asString().equalsIgnoreCase("copy_to_clipboard")) {

            if (target == null) {
                Debug.echoError("Player not found in queue");
                return;
            }
            if (file != null && cuboid != null) {
                Debug.echoError("Both cuboid and file args were specified. Only one can be used.");
                return;
            }
            if (cuboid != null) {
                if (position == null) {
                    Debug.echoError("Position not specified");
                    return;
                }
                CuboidRegion region = cuboidToWECuboid(cuboid);
                Player p = BukkitAdapter.adapt(target.getPlayerEntity());
                BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                BlockVector3 pos = BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ());
                clipboard.setOrigin(pos);
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1, p);

                ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
                forwardExtentCopy.setCopyingEntities(false);
                try {
                    Operations.complete(forwardExtentCopy);
                }
                catch (WorldEditException ex) {
                    Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard");
                    Debug.echoError(ex);
                    return;
                }
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                WorldEdit.getInstance().getSessionManager().get(p).setClipboard(holder);
                return;
            }
            if (file == null) {
                Debug.echoError("Cuboid or file must be specified.");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            File fileToLoad = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
            if (!Utilities.canReadFile(fileToLoad)) {
                Debug.echoError("Permission to read file '" + file + "' denied by config.");
                return;
            }
            if (!fileToLoad.exists()) {
                Debug.echoError("File not found");
                return;
            }
            ClipboardFormat format = ClipboardFormats.findByFile(fileToLoad);
            if (format == null) {
                Debug.echoError("File not found");
                return;
            }
            Clipboard clipboard;
            Closer closer = Closer.create();
            try {
                FileInputStream fis = closer.register(new FileInputStream(fileToLoad));
                BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
                ClipboardReader reader = closer.register(format.getReader(bis));
                clipboard = reader.read();
            }
            catch (IOException ex) {
                Debug.echoError(ex);
                return;
            }
            if (clipboard == null) {
                Debug.echoError("Clipboard returned null");
                return;
            }
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            Player wePlayer = BukkitAdapter.adapt(target.getPlayerEntity());
            WorldEdit.getInstance().getSessionManager().get(wePlayer).setClipboard(holder);
        }
    }
}
