package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.PlayerConverter;

public class BetonQuestBridge extends Bridge {

    @Override
    public void init() {
        // <--[tag]
        // @attribute <PlayerTag.beton_quest[<package>].variable[<variable>]>
        // @returns ElementTag
        // @plugin Depenizen, BetonQuest
        // @description
        // Returns a variable from a BetonQuest package, for the player.
        // -->
        PlayerTag.registerTag("beton_quest", (attribute, object) -> {
            String quest_package = attribute.getContext(1);
            if (attribute.startsWith("variable", 2)) {
                String variable = attribute.getContext(2);
                attribute.fulfill(1);
                return new ElementTag(BetonQuest.getInstance().getVariableValue(quest_package, "%" + variable + "%", PlayerConverter.getID(object.getPlayerEntity())));
            }
            return null;
        });
    };
}
