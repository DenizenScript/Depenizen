package com.denizenscript.depenizen.bukkit.properties.betonquest;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.PlayerConverter;

public class BetonQuestPlayerProperties implements Property {

    public static boolean describes(ObjectTag player) {
        return player instanceof PlayerTag;
    }

    public static BetonQuestPlayerProperties getFrom(ObjectTag player) {
        if (!describes(player)) {
            return null;
        }
        else {
            return new BetonQuestPlayerProperties((PlayerTag) player);
        }
    }

    public static final String[] handledMechs = new String[] {
    }; // None

    private BetonQuestPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.beton_quest[<package>].variable[<variable>]>
        // @returns ElementTag
        // @plugin Depenizen, BetonQuest
        // @description
        // Returns a variable from a BetonQuest package, for the player.
        // -->
        PropertyParser.registerTag(BetonQuestPlayerProperties.class, ElementTag.class, "beton_quest", (attribute, object) -> {
            String quest_package = attribute.getParam();
            if (attribute.startsWith("variable", 2)) {
                String variable = attribute.getContext(2);
                attribute.fulfill(1);
                return new ElementTag(BetonQuest.getInstance().getVariableValue(quest_package, "%" + variable + "%", PlayerConverter.getID(object.player.getPlayerEntity())));
            }
            return null;
        });
    }

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BetonQuestPlayerPropertes";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }
}
