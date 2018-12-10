package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.magicspells.SpellCastScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.magicspells.MagicSpellsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;

public class MagicSpellsSupport extends Support {

    public MagicSpellsSupport() {
        registerProperty(MagicSpellsPlayerExtension.class, dPlayer.class);
        registerScriptEvents(new SpellCastScriptEvent());
    }

    
}
