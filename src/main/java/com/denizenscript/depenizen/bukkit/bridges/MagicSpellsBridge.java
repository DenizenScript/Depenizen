package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.magicspells.*;
import com.denizenscript.depenizen.bukkit.properties.magicspells.MagicSpellsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class MagicSpellsBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(MagicSpellsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(SpellCastScriptEvent.class);
        ScriptEvent.registerScriptEvent(SpellCastedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ManaChangeScriptEvent.class);
        ScriptEvent.registerScriptEvent(SpellLearnScriptEvent.class);
    }
}
