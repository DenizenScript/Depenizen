package com.denizenscript.depenizen.bukkit.commands.worldedit;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.*;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.sk89q.worldedit.*;
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

public class WorldEditCommand extends AbstractCommand {

    public WorldEditCommand() {
        setName("worldedit");
        setSyntax("worldedit [create_schematic/copy_to_clipboard/paste] (file:<file path>) (cuboid:<cuboid>) (position:<location>) (rotate:<#>) (undoable) (noair)");
        setRequiredArguments(2, 7);
        autoCompile();
    }

    // <--[command]
    // @Name worldedit
    // @Syntax worldedit [create_schematic/copy_to_clipboard/paste] (file:<file path>) (cuboid:<cuboid>) (position:<location>) (rotate:<#>) (undoable) (noair)
    // @Group Depenizen
    // @Plugin Depenizen, WorldEdit
    // @Required 2
    // @Maximum 7
    // @Short Controls schematics and clipboards in WorldEdit.
    //
    // @Description
    // Controls schematics and clipboards in WorldEdit. You should almost always use <@link command schematic> instead of this.
    //
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

    public enum Action {CREATE_SCHEMATIC, COPY_TO_CLIPBOARD, PASTE}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("action") @ArgLinear @ArgDefaultNull Action action,
                                   @ArgName("file") @ArgPrefixed @ArgDefaultNull ElementTag file,
                                   @ArgName("cuboid") @ArgPrefixed @ArgDefaultNull CuboidTag cuboid,
                                   @ArgName("position") @ArgPrefixed @ArgDefaultNull LocationTag position,
                                   @ArgName("rotate") @ArgPrefixed @ArgDefaultNull ElementTag rotate,
                                   @ArgName("undoable") boolean undoable,
                                   @ArgName("noair") boolean noAir) {
        if (action == null) {
            throw new InvalidArgumentsRuntimeException("Action not specified!");
        }
        PlayerTag target = Utilities.getEntryPlayer(scriptEntry);
        switch (action) {
            case PASTE: {
                if (file == null) {
                    Debug.echoError("File path not specified.");
                    return;
                }
                if (position == null) {
                    Debug.echoError("Position not specified.");
                    return;
                }
                File fileToLoad = new File(Denizen.getInstance().getDataFolder(), "schematics/" + file + ".schem");
                if (!Utilities.canReadFile(fileToLoad)) {
                    Debug.echoError("Cannot read from that file path due to security settings in Denizen/config.yml.");
                    return;
                }
                if (!fileToLoad.exists()) {
                    Debug.echoError("File not found.");
                    return;
                }
                ClipboardFormat format = ClipboardFormats.findByFile(fileToLoad);
                if (format == null) {
                    Debug.echoError("File not found.");
                    return;
                }
                Clipboard clipboard;
                try {
                    clipboard = format.getReader(new FileInputStream(fileToLoad)).read();
                }
                catch (IOException ex) {
                    Debug.echoError(ex);
                    return;
                }
                if (clipboard == null) {
                    Debug.echoError("Clipboard became null.");
                    return;
                }
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                if (rotate != null) {
                    holder.setTransform(holder.getTransform().combine(new AffineTransform().rotateY(rotate.asInt())));
                }
                World weWorld = new BukkitWorld(position.getWorld());
                if (undoable) {
                    if (target == null) {
                        Debug.echoError("Player not found in queue.");
                        return;
                    }
                    Player wePlayer = BukkitAdapter.adapt(target.getPlayerEntity());
                    EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(weWorld).actor(wePlayer).build();
                    Operation operation = holder.createPaste(editSession)
                            .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                            .ignoreAirBlocks(noAir).build();
                    try {
                        Operations.complete(operation);
                    }
                    catch (WorldEditException ex) {
                        Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard.");
                        Debug.echoError(ex);
                        return;
                    }
                    Operations.completeBlindly(editSession.commit());
                    WorldEdit.getInstance().getSessionManager().get(wePlayer).remember(editSession);
                }
                else {
                    EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(weWorld).build();
                    Operation operation = holder.createPaste(editSession)
                            .to(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()))
                            .ignoreAirBlocks(noAir).build();
                    try {
                        Operations.complete(operation);
                    }
                    catch (WorldEditException ex) {
                        Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard.");
                        Debug.echoError(ex);
                        return;
                    }
                    Operations.completeBlindly(editSession.commit());
                }
            }
            case CREATE_SCHEMATIC: {
                if (file == null) {
                    Debug.echoError("File not specified.");
                    return;
                }
                File fileToSave = new File(Denizen.getInstance().getDataFolder(), "schematics/" + file + ".schem");
                if (!Utilities.canWriteToFile(fileToSave)) {
                    Debug.echoError("Cannot write to that file path due to security settings in Denizen/config.yml.");
                    return;
                }
                if (cuboid == null) {
                    if (target == null) {
                        Debug.echoError("Cuboid not specified.");
                        return;
                    }
                    try {
                        ClipboardHolder clipboard = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(target.getPlayerEntity())).getClipboard();
                        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(fileToSave))) {
                            writer.write(clipboard.getClipboard());
                        }
                        catch (IOException ex) {
                            Debug.echoError(ex);
                        }
                    }
                    catch (EmptyClipboardException ex) {
                        Debug.echoError("Cuboid not specified, and player does not have a clipboard.");
                        return;
                    }
                    return;
                }
                CuboidRegion region = cuboidToWECuboid(cuboid);
                if (position == null) {
                    Debug.echoError("Position not specified.");
                    return;
                }
                BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                clipboard.setOrigin(BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
                EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(region.getWorld()).maxBlocks(-1).build();
                ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
                forwardExtentCopy.setCopyingEntities(false);
                try {
                    Operations.complete(forwardExtentCopy);
                }
                catch (WorldEditException ex) {
                    Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard.");
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
            case COPY_TO_CLIPBOARD: {
                if (target == null) {
                    Debug.echoError("Player not found in queue.");
                    return;
                }
                if (file != null && cuboid != null) {
                    Debug.echoError("Both cuboid and file args were specified. Only one can be used.");
                    return;
                }
                if (cuboid != null) {
                    if (position == null) {
                        Debug.echoError("Position not specified.");
                        return;
                    }
                    CuboidRegion region = cuboidToWECuboid(cuboid);
                    Player player = BukkitAdapter.adapt(target.getPlayerEntity());
                    BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                    BlockVector3 pos = BlockVector3.at(position.getBlockX(), position.getBlockY(), position.getBlockZ());
                    clipboard.setOrigin(pos);
                    EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(region.getWorld()).maxBlocks(-1).actor(player).build();
                    ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
                    forwardExtentCopy.setCopyingEntities(false);
                    try {
                        Operations.complete(forwardExtentCopy);
                    }
                    catch (WorldEditException ex) {
                        Debug.echoError("Exception in WorldEdit while loading a schematic to clipboard.");
                        Debug.echoError(ex);
                        return;
                    }
                    WorldEdit.getInstance().getSessionManager().get(player).setClipboard(new ClipboardHolder(clipboard));
                    return;
                }
                if (file == null) {
                    Debug.echoError("Cuboid or file must be specified.");
                    return;
                }
                File fileToLoad = new File(Denizen.getInstance().getDataFolder(), "schematics/" + file + ".schem");
                if (!Utilities.canReadFile(fileToLoad)) {
                    Debug.echoError("Cannot read from that file path due to security settings in Denizen/config.yml.");
                    return;
                }
                if (!fileToLoad.exists()) {
                    Debug.echoError("File not found.");
                    return;
                }
                ClipboardFormat format = ClipboardFormats.findByFile(fileToLoad);
                if (format == null) {
                    Debug.echoError("File not found.");
                    return;
                }
                Clipboard clipboard;
                Closer closer = Closer.create();
                try {
                    FileInputStream fis = closer.register(new FileInputStream(fileToLoad));
                    BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
                    clipboard = closer.register(format.getReader(bis)).read();
                }
                catch (IOException ex) {
                    Debug.echoError(ex);
                    return;
                }
                if (clipboard == null) {
                    Debug.echoError("Clipboard returned null.");
                    return;
                }
                WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(target.getPlayerEntity())).setClipboard(new ClipboardHolder(clipboard));
            }
        }
    }


    public static CuboidRegion cuboidToWECuboid(CuboidTag cuboid) {
        LocationTag top = cuboid.getHigh(0);
        LocationTag bottom = cuboid.getLow(0);
        BlockVector3 topVector = BlockVector3.at(top.getBlockX(), top.getBlockY(), top.getBlockZ());
        BlockVector3 bottomVector = BlockVector3.at(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
        World w = new BukkitWorld(cuboid.getWorld().getWorld());
        return new CuboidRegion(w, bottomVector, topVector);
    }
}
