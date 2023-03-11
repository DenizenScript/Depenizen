package com.denizenscript.depenizen.bukkit.properties.coreprotect;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.*;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.CoreProtectBridge;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectLocationProperties implements Property {

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static CoreProtectLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new CoreProtectLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
            "coreprotect_log_placement", "coreprotect_log_removal", "coreprotect_log_interaction"
    };

    public CoreProtectLocationProperties(LocationTag location) {
        this.location = location;
    }

    public LocationTag location;

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "CoreProtectLocation";
    }

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.coreprotect_logs[<duration>]>
        // @returns ListTag(MapTag)
        // @plugin Depenizen, CoreProtect
        // @description
        // Returns a list changes to the given block within the given duration.
        // Each Map has keys:
        // "action" ("removal", "placement", or "interaction"),
        // "is_rolled_back" (true/false),
        // "material" (MaterialTag),
        // "player_name" (player username, not a PlayerTag instance),
        // "time" (TimeTag)
        // -->
        PropertyParser.registerTag(CoreProtectLocationProperties.class, ListTag.class, "coreprotect_logs", (attribute, property) -> {
            if (!attribute.hasParam()) {
                return null;
            }
            DurationTag duration = attribute.paramAsType(DurationTag.class);
            if (duration == null) {
                attribute.echoError("Invalid duration.");
                return null;
            }
            ListTag logs = new ListTag();
            for (String[] logEntry : CoreProtectBridge.apiInstance.blockLookup(property.location.getBlock(), duration.getSecondsAsInt())) {
                CoreProtectAPI.ParseResult parsed = CoreProtectBridge.apiInstance.parseResult(logEntry);
                MapTag entry = new MapTag();
                entry.putObject("action", new ElementTag(parsed.getActionString(), true));
                entry.putObject("is_rolled_back", new ElementTag(parsed.isRolledBack()));
                entry.putObject("material", parsed.getBlockData() == null ? new MaterialTag(parsed.getType()) : new MaterialTag(parsed.getBlockData()));
                entry.putObject("player_name", new ElementTag(parsed.getPlayer()));
                entry.putObject("time", new TimeTag(parsed.getTimestamp()));
                logs.addObject(entry);
            }
            return logs;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object LocationTag
        // @name coreprotect_log_placement
        // @input MapTag
        // @plugin Depenizen, CoreProtect
        // @description
        // Adds a CoreProtect log entry for this block location of somebody placing a block.
        // Input map must have 'user' (a name, not a PlayerTag) and 'material' (a block MaterialTag)
        // For example: - adjust <context.location> coreprotect_log_placement:[user=<player.name>;material=stone]
        // -->
        if (mechanism.matches("coreprotect_log_placement") && mechanism.requireObject(MapTag.class)) {
            MapTag map = mechanism.valueAsType(MapTag.class);
            ElementTag user = map.getElement("user");
            MaterialTag material = map.getObjectAs("material", MaterialTag.class, mechanism.context);
            if (user == null) {
                mechanism.echoError("Missing required input, check meta docs.");
                return;
            }
            if (material == null || !material.getMaterial().isBlock()) {
                mechanism.echoError("Invalid block material input.");
                return;
            }
            CoreProtectBridge.apiInstance.logPlacement(user.asString(), location.clone(), material.getMaterial(), material.getModernData());
        }

        // <--[mechanism]
        // @object LocationTag
        // @name coreprotect_log_removal
        // @input MapTag
        // @plugin Depenizen, CoreProtect
        // @description
        // Adds a CoreProtect log entry for this block location of somebody removing a block.
        // Input map must have 'user' (a name, not a PlayerTag) and 'material' (a block MaterialTag)
        // For example: - adjust <context.location> coreprotect_log_removal:[user=<player.name>;material=stone]
        // -->
        if (mechanism.matches("coreprotect_log_removal") && mechanism.requireObject(MapTag.class)) {
            MapTag map = mechanism.valueAsType(MapTag.class);
            ElementTag user = map.getElement("user");
            MaterialTag material = map.getObjectAs("material", MaterialTag.class, mechanism.context);
            if (user == null) {
                mechanism.echoError("Missing required input, check meta docs.");
                return;
            }
            if (material == null || !material.getMaterial().isBlock()) {
                mechanism.echoError("Invalid block material input.");
                return;
            }
            CoreProtectBridge.apiInstance.logRemoval(user.asString(), location.clone(), material.getMaterial(), material.getModernData());
        }

        // <--[mechanism]
        // @object LocationTag
        // @name coreprotect_log_interaction
        // @input ElementTag
        // @plugin Depenizen, CoreProtect
        // @description
        // Adds a CoreProtect log entry for this block location of somebody interacting with a block.
        // Input is just the name of who interacted (not a PlayerTag).
        // -->
        if (mechanism.matches("coreprotect_log_interaction") && mechanism.hasValue()) {
            CoreProtectBridge.apiInstance.logInteraction(mechanism.getValue().asString(), location.clone());
        }
    }
}
