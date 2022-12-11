package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import io.lumine.mythic.core.skills.variables.Variable;
import io.lumine.mythic.core.skills.variables.VariableType;

import java.util.HashMap;
import java.util.Map;

public class MythicMobsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MythicMobsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static MythicMobsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MythicMobsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
            "mythic_variable_map"
    };

    public MythicMobsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.mythic_variable[<name>]>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the value of a MythicMobs variable for this player.
        // -->
        PropertyParser.registerTag(MythicMobsPlayerProperties.class, ElementTag.class, "mythic_variable", (attribute, object) -> {
            return new ElementTag(MythicMobsBridge.getMythicVariable(object.player.getPlayerEntity(), attribute.getParam()), true);
        });

        // <--[tag]
        // @attribute <PlayerTag.mythic_variable_map>
        // @returns MapTag
        // @plugin Depenizen, MythicMobs
        // @mechanism PlayerTag.mythic_variable_map
        // @description
        // Returns a map of the player's variables.
        // -->
        PropertyParser.registerTag(MythicMobsPlayerProperties.class, MapTag.class, "mythic_variable_map", (attribute, object) -> {
            MapTag result = new MapTag();
            for (Map.Entry<String, Variable> entry : MythicMobsBridge.getMythicVariableMap(object.player.getPlayerEntity()).entrySet()) {
                result.putObject(entry.getKey(), new ElementTag(entry.getValue().toString(), true));
            }
            return result;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object PlayerTag
        // @name mythic_variable_map
        // @input MapTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the variables of a player from a map.
        // @tags
        // <PlayerTag.mythic_variable_map>
        // -->
        if (mechanism.matches("mythic_variable_map") && mechanism.requireObject(MapTag.class)) {
            MapTag map = mechanism.valueAsType(MapTag.class);
            Map<String, Variable> newMap = new HashMap<String, Variable>();
            for (Map.Entry<StringHolder, ObjectTag> entry : map.map.entrySet()) {
                newMap.put(entry.getKey().str, Variable.ofType(VariableType.STRING, entry.getValue()));
            }
            MythicMobsBridge.setMythicVariableMap(player.getPlayerEntity(), newMap);
        }
    }
}
