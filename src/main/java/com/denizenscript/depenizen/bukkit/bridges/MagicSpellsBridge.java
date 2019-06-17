package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.magicspells.*;
import com.denizenscript.depenizen.bukkit.extensions.magicspells.MagicSpellsPlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class MagicSpellsBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(MagicSpellsPlayerExtension.class, dPlayer.class);
        ScriptEvent.registerScriptEvent(new SpellCastScriptEvent());
        ScriptEvent.registerScriptEvent(new SpellCastedScriptEvent());
        ScriptEvent.registerScriptEvent(new ManaChangeScriptEvent());
        ScriptEvent.registerScriptEvent(new SpellLearnScriptEvent());
    }
}
