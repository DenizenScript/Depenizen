package net.gnomeffinway.depenizen.commands.worldedit;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dMaterial;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class SchematicCommand extends AbstractCommand {

    // <--[command]
    // @Name Schematic
    // @Syntax schematic [create/load/unload/rotate/paste/save] [name:<name>] (angle:<#>) (<location>) (<cuboid>) (noair)
    // @Group Depenizen
    // @Plugin Depenizen, WorldEdit
    // @Required 2
    // @Stable stable
    // @Short Creates, loads, pastes, and saves WorldEdit schematics.
    // @Author mcmonkey

    // @Description
    // Todo

    // @Tags
    // <schematic[<name>].height>
    // <schematic[<name>].length>
    // <schematic[<name>].width>
    // <schematic[<name>].block[<location>]>
    // <schematic[<name>].origin>
    // <schematic[<name>].offset>
    // <schematic[<name>].blocks>

    // @Usage
    // Use to create a new schematic from a cuboid and an origin location
    // - schematic create name:MySchematic cu@<player.location.sub[5,5,5]>|<player.location.add[5,5,5]> <player.location>

    // @Usage
    // Use to load a schematic
    // - schematic load name:MySchematic

    // @Usage
    // Use to unload a schematic
    // - schematic unload name:MySchematic

    // @Usage
    // Use to rotate a loaded schematic
    // - schematic rotate name:MySchematic angle:90

    // @Usage
    // Use to paste a loaded schematic
    // - schematic paste name:MySchematic <player.location> noair

    // @Usage
    // Use to save a created schematic
    // - schematic save name:MySchematic

    // -->

    @Override
    public void onEnable() {
        TagManager.registerTagEvents(this);
        schematics = new HashMap<String, CuboidClipboard>();
    }


    private enum Type { CREATE, LOAD, UNLOAD, ROTATE, PASTE, SAVE }
    public static Map<String, CuboidClipboard> schematics;

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // - schematic create name:Potato cu@x,y,z,world|x,y,z,world origin:x,y,z,world
        // - schematic load name:Potato
        // - schematic unload name:Potato
        // - schematic rotate name:Potato angle:90
        // - schematic paste name:Potato location:x,y,z,world (noair)
        // - schematic save name:Potato
        // - schematic [load/unload/rotate/paste] [name:<name>] (angle:<#>) (<location>) (noair)

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values()))
                scriptEntry.addObject("type", new Element(arg.raw_value.toUpperCase()));

            else if (!scriptEntry.hasObject("name")
                    && arg.matchesPrefix("name"))
                scriptEntry.addObject("name", arg.asElement());

            else if (!scriptEntry.hasObject("angle")
                    && arg.matchesPrimitive(aH.PrimitiveType.Integer))
                scriptEntry.addObject("angle", arg.asElement());

            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(dLocation.class))
                scriptEntry.addObject("location", arg.asType(dLocation.class));

            else if (!scriptEntry.hasObject("noair")
                    && arg.matches("noair"))
                scriptEntry.addObject("noair", Element.TRUE);

            else if (!scriptEntry.hasObject("cuboid")
                    && arg.matchesArgumentType(dCuboid.class))
                scriptEntry.addObject("cuboid", arg.asType(dCuboid.class));

            else
                arg.reportUnhandled();
        }

        if (!scriptEntry.hasObject("type"))
            throw new InvalidArgumentsException("Missing type argument!");

        if (!scriptEntry.hasObject("name"))
            throw new InvalidArgumentsException("Missing name argument!");

    }


    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element angle = scriptEntry.getElement("angle");
        Element type = scriptEntry.getElement("type");
        Element name = scriptEntry.getElement("name");
        Element noair = scriptEntry.getElement("noair");
        dLocation location = scriptEntry.getdObject("location");
        dCuboid cuboid = scriptEntry.getdObject("cuboid");

        dB.report(scriptEntry, getName(), type.debug()
                + name.debug()
                + (location != null ? location.debug(): "")
                + (cuboid != null ? cuboid.debug(): "")
                + (angle != null ? angle.debug(): "")
                + (noair != null ? noair.debug(): ""));

        CuboidClipboard cc;
        switch (Type.valueOf(type.asString())) {
            case CREATE:
                if (schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is already loaded.");
                    return;
                }
                if (cuboid == null) {
                    dB.echoError("Missing cuboid argument!");
                    return;
                }
                if (location == null) {
                    dB.echoError("Missing origin location argument!");
                    return;
                }
                try {
                    LocalWorld world = new BukkitWorld(cuboid.getWorld());
                    EditSession es = new EditSession(world, 99999999);
                    Vector origin = new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
                    Vector max = new Vector(cuboid.getHigh(0).getX(), cuboid.getHigh(0).getY(), cuboid.getHigh(0).getZ());
                    Vector min = new Vector(cuboid.getLow(0).getX(), cuboid.getLow(0).getY(), cuboid.getLow(0).getZ());
                    Vector size = max.subtract(min).add(Vector.ONE);
                    cc = new CuboidClipboard(size, min, min.subtract(origin));
                    cc.copy(es, new CuboidRegion(world, min, max));
                    schematics.put(name.asString().toUpperCase(), cc);
                }
                catch (Exception ex) {
                    dB.echoError("Error creating schematic file " + name.asString() + ".");
                    dB.echoError(ex);
                    return;
                }
                break;
            case LOAD:
                if (schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is already loaded.");
                    return;
                }
                try {
                    String directory = URLDecoder.decode(System.getProperty("user.dir"));
                    File f = new File(directory + "/plugins/Denizen/schematics/" + name.asString() + ".schematic");
                    if (!f.exists()) {
                        dB.echoError("Schematic file " + name.asString() + " does not exist.");
                        return;
                    }
                    cc = SchematicFormat.MCEDIT.load(f);
                    schematics.put(name.asString().toUpperCase(), cc);
                }
                catch (Exception ex) {
                    dB.echoError("Error loading schematic file " + name.asString() + ".");
                    dB.echoError(ex);
                    return;
                }
                break;
            case UNLOAD:
                if (!schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is not loaded.");
                    return;
                }
                schematics.remove(name.asString().toUpperCase());
                break;
            case ROTATE:
                if (!schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is not loaded.");
                    return;
                }
                if (angle == null) {
                    dB.echoError("Missing angle argument!");
                    return;
                }
                schematics.get(name.asString().toUpperCase()).rotate2D(angle.asInt());
                break;
            case PASTE:
                if (!schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is not loaded.");
                    return;
                }
                if (location == null) {
                    dB.echoError("Missing location argument!");
                    return;
                }
                try {
                    schematics.get(name.asString().toUpperCase())
                            .paste(new EditSession(new BukkitWorld(location.getWorld()), 99999999),
                                    new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()),
                                    noair != null);
                }
                catch (Exception ex) {
                    dB.echoError("Exception pasting schematic file " + name.asString() + ".");
                    dB.echoError(ex);
                    return;
                }
                break;
            case SAVE:
                if (!schematics.containsKey(name.asString().toUpperCase())) {
                    dB.echoError("Schematic file " + name.asString() + " is not loaded.");
                    return;
                }
                try {
                    cc = schematics.get(name.asString().toUpperCase());
                    String directory = URLDecoder.decode(System.getProperty("user.dir"));
                    File f = new File(directory + "/plugins/Denizen/schematics/" + name.asString() + ".schematic");
                    SchematicFormat.MCEDIT.save(cc, f);
                }
                catch (Exception ex) {
                    dB.echoError("Error saving schematic file " + name.asString() + ".");
                    dB.echoError(ex);
                    return;
                }
                break;
        }
    }
    @TagManager.TagEvents
    public void schematicTags(ReplaceableTagEvent event) {

        if (!event.matches("schematic, schem")) return;

        if (!event.hasNameContext()) {
            return;
        }

        String id = event.getNameContext().toUpperCase();

        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry()).fulfill(1);

        if (!schematics.containsKey(id)) {
            // Meta below
            if (attribute.startsWith("exists")) {
                event.setReplaced(new Element(false)
                        .getAttribute(attribute.fulfill(1)));
                return;
            }

            dB.echoError("Schematic file " + id + " is not loaded.");
            return;
        }

        CuboidClipboard cc = schematics.get(id);

        //
        // Check attributes
        //

        // <--[tag]
        // @attribute <schematic[<name>].exists>
        // @returns Element(Boolean)
        // @description
        // Returns whether the schematic exists.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("exists")) {
            event.setReplaced(new Element(true)
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].height>
        // @returns Element(Number)
        // @description
        // Returns the height of the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("height")) {
            event.setReplaced(new Element(cc.getHeight())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].length>
        // @returns Element(Number)
        // @description
        // Returns the length of the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("length")) {
            event.setReplaced(new Element(cc.getLength())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].width>
        // @returns Element(Number)
        // @description
        // Returns the width of the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("width")) {
            event.setReplaced(new Element(cc.getWidth())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].block[<location>]>
        // @returns dMaterial
        // @description
        // Returns the material for the block at the location in the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("block")) {
            if (attribute.hasContext(1) && dLocation.matches(attribute.getContext(1))) {
                dLocation location = dLocation.valueOf(attribute.getContext(1));
                BaseBlock bb = cc.getBlock(new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()));
                event.setReplaced(dMaterial.valueOf(bb.getType() + "," + bb.getData()) // TODO: Better representation of the block
                        .getAttribute(attribute.fulfill(1)));
                return;
            }
        }

        // <--[tag]
        // @attribute <schematic[<name>].origin>
        // @returns dLocation
        // @description
        // Returns the origin location of the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("origin")) {
            event.setReplaced(new dLocation(Bukkit.getWorlds().get(0),
                    cc.getOrigin().getX(), cc.getOrigin().getY(), cc.getOrigin().getZ())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].offset>
        // @returns dLocation
        // @description
        // Returns the offset location of the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("offset")) {
            event.setReplaced(new dLocation(Bukkit.getWorlds().get(0),
                    cc.getOffset().getX(), cc.getOffset().getY(), cc.getOffset().getZ())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].blocks>
        // @returns Element(Number)
        // @description
        // Returns the number of blocks in the schematic.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("blocks")) {
            event.setReplaced(new Element(cc.getHeight() * cc.getWidth() * cc.getLength())
                    .getAttribute(attribute.fulfill(1)));
            return;
        }

        // <--[tag]
        // @attribute <schematic[<name>].cuboid[<origin location>]>
        // @returns dCuboid
        // @description
        // Returns a cuboid of where the schematic would be if it was pasted at an origin.
        // @plugin WorldEdit
        // -->
        if (attribute.startsWith("cuboid") && attribute.hasContext(1)) {
            dLocation origin = dLocation.valueOf(attribute.getContext(1));
            Location schemSize = new Location(origin.getWorld(), cc.getWidth(), cc.getHeight(), cc.getLength());
            Location offset = new Location(origin.getWorld(), cc.getOffset().getX(), cc.getOffset().getY(), cc.getOffset().getZ());
            Location min = new Location(origin.getWorld(), origin.getX() + offset.getX(), origin.getY() + offset.getY(), origin.getZ() + offset.getZ());
            Location max = new Location(origin.getWorld(), min.getX() + schemSize.getX() - 1, min.getY() + schemSize.getY() - 1, min.getZ() + schemSize.getZ() - 1);
            event.setReplaced(new dCuboid(min, max)
                    .getAttribute(attribute.fulfill(1)));
            return;
        }
    }
}
