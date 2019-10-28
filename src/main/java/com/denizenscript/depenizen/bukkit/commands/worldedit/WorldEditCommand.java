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
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
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
    // @Short Allows access to various WorldEdit actions.
    //
    // @Description
    // Allows access to various WorldEdit actions.
    // Actions can be either create_schematic, copy_to_clipboard or paste.
    // Specify noair to paste without air blocks from the schematic.
    // Specify rotate to rotate the schematic when pasting it.
    // Specify undoable to attach the paste to a player's WorldEdit history which allows them to undo/redo.
    // The file path starts in the folder: /plugins/Denizen/schematics/
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

    @SuppressWarnings("unchecked")
    @Override
    public void execute(ScriptEntry scriptEntry) {

        ElementTag file = scriptEntry.getObjectTag("file");
        ElementTag action = scriptEntry.getObjectTag("action");
        PlayerTag target = Utilities.getEntryPlayer(scriptEntry);
        LocationTag position = scriptEntry.getObjectTag("position");
        ElementTag noAir = scriptEntry.getObjectTag("noair");
        CuboidTag cuboid = scriptEntry.getObjectTag("cuboid");
        ElementTag undoable = scriptEntry.getObjectTag("undoable");
        ElementTag rotate = scriptEntry.getObjectTag("rotate");

        // Report to dB
        Debug.report(scriptEntry, getName(), action.debug()
                + (target != null ? target.debug() : "")
                + (position != null ? position.debug() : "")
                + (noAir != null ? noAir.debug() : "")
                + (cuboid != null ? cuboid.debug() : "")
                + (undoable != null ? undoable.debug() : "")
                + (rotate != null ? rotate.debug() : "")
                + (file != null ? file.debug() : ""));



        if (action.asString().equalsIgnoreCase("paste")) {
            if (file == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File path not specified");
                return;
            }
            if (position == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Position not specified");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            File file_load = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file_load);
            if (format == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File not found");
                return;
            }
            Clipboard clipboard;
            try {
                ClipboardReader reader = format.getReader(new FileInputStream(file_load));
                clipboard = reader.read();
            }
            catch (IOException e) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File did not load correctly or not found!");
                return;
            }
            if (clipboard == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Clipboard became null");
                return;
            }
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            if (rotate != null) {
                AffineTransform transform = new AffineTransform();
                transform = transform.rotateY(rotate.asInt());
                holder.setTransform(holder.getTransform().combine(transform));
            }
            World w = new BukkitWorld(position.getWorld());
            boolean pastenoair = true;
            if (noAir == null) {
                pastenoair = false;
            }
            if (undoable != null) {
                if (target == null) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "Player not found in queue");
                    return;
                }
                com.sk89q.worldedit.entity.Player p = BukkitAdapter.adapt(target.getOfflinePlayer().getPlayer());
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(w, -1,null,p);
                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                        .ignoreAirBlocks(pastenoair)
                        .build();
                try {
                    Operations.complete(operation);
                }
                catch (WorldEditException e) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "WorldEdit caught an exception while pasting a schematic");
                    return;
                }
                editSession.flushSession();
                WorldEdit.getInstance().getSessionManager().get(p).remember(editSession);
            }
            else {
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(w, -1);
                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                        .ignoreAirBlocks(pastenoair)
                        .build();
                try {
                    Operations.complete(operation);
                }
                catch (WorldEditException e) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "WorldEdit caught an exception while pasting a schematic");
                    return;
                }
                editSession.flushSession();
            }

        }
        else if (action.asString().equalsIgnoreCase("create_schematic")) {
            if (cuboid == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Cuboid not specified");
                return;
            }
            if (file == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File not specified");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            File file_load = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
            LocationTag top1 = new LocationTag(cuboid.getHigh(0));
            LocationTag bot1 = new LocationTag(cuboid.getLow(0));
            BlockVector3 top = BlockVector3.at(top1.getBlockX(), top1.getBlockY(), top1.getBlockZ());
            BlockVector3 bot = BlockVector3.at(bot1.getBlockX(), bot1.getBlockY(), bot1.getBlockZ());
            World w = new BukkitWorld(cuboid.getWorld());
            CuboidRegion region = new CuboidRegion(w, bot, top);

            if (position == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Position not specified");
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
            catch (WorldEditException e) {
                Debug.echoError(scriptEntry.getResidingQueue(), "WorldEdit caught an exception while loading a schematic to clipboard");
                return;
            }
            try {
                try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file_load))) {
                    writer.write(clipboard);
                }
            }
            catch (IOException e) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File not found");
            }

        }
        else if (action.asString().equalsIgnoreCase("copy_to_clipboard")) {

            if (target == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Player not found in queue");
                return;
            }
            if (file == null) {
                if (cuboid == null) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "Cuboid or File not specified");
                    return;
                }
                if (position == null) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "Position not specified");
                    return;
                }
                LocationTag top1 = new LocationTag(cuboid.getHigh(0));
                LocationTag bot1 = new LocationTag(cuboid.getLow(0));
                BlockVector3 top = BlockVector3.at(top1.getBlockX(), top1.getBlockY(), top1.getBlockZ());
                BlockVector3 bot = BlockVector3.at(bot1.getBlockX(), bot1.getBlockY(), bot1.getBlockZ());
                World w = new BukkitWorld(cuboid.getWorld());
                CuboidRegion region = new CuboidRegion(w, bot, top);
                com.sk89q.worldedit.entity.Player p = BukkitAdapter.adapt(target.getOfflinePlayer().getPlayer());
                BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                BlockVector3 pos = BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ());
                clipboard.setOrigin(pos);
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1,p);

                ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
                forwardExtentCopy.setCopyingEntities(false);
                try {
                    Operations.complete(forwardExtentCopy);
                }
                catch (WorldEditException e) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "WorldEdit caught an exception while loading a schematic to clipboard");
                }
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                WorldEdit.getInstance().getSessionManager().get(p).setClipboard(holder);
            }
            else if (cuboid == null) {
                String directory = URLDecoder.decode(System.getProperty("user.dir"));
                File file_load = new File(directory + "/plugins/Denizen/schematics/" + file + ".schem");
                ClipboardFormat format = ClipboardFormats.findByFile(file_load);
                if (format == null) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "File not found");
                    return;
                }
                Clipboard clipboard;
                Closer closer = Closer.create();
                try {
                    FileInputStream fis = closer.register(new FileInputStream(file_load));
                    BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
                    ClipboardReader reader = closer.register(format.getReader(bis));
                    clipboard = reader.read();
                }
                catch (IOException e) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "File not found");
                    return;
                }
                if (clipboard == null) {
                    Debug.echoError(scriptEntry.getResidingQueue(), "Clipboard returned null");
                    return;
                }
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                com.sk89q.worldedit.entity.Player p = BukkitAdapter.adapt(target.getOfflinePlayer().getPlayer());
                WorldEdit.getInstance().getSessionManager().get(p).setClipboard(holder);
            }
            else {
                Debug.echoError(scriptEntry.getResidingQueue(), "Both cuboid and file args were specified");
            }
        }

    }

}
