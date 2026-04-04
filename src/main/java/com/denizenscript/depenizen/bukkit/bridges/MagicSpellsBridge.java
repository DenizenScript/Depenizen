package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.magicspells.*;
import com.denizenscript.depenizen.bukkit.properties.magicspells.MagicSpellsPlayerProperties;
import com.nisovin.magicspells.MagicSpells;

public class MagicSpellsBridge extends Bridge {

    static class MagicSpellsTagBase extends PseudoObjectTagBase<MagicSpellsTagBase> {

        public static MagicSpellsTagBase instance;

        public MagicSpellsTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(MagicSpellsTagBase.class, "magicspells", (t) -> instance);
        }

        public void register() {

            // <--[tag]
            // @attribute <magicspells.spells>
            // @returns ListTag
            // @plugin Depenizen, MagicSpells
            // @description
            // Returns a list of all registered spells.
            // -->
            tagProcessor.registerTag(ListTag.class, "spells", (attribute, object) -> {
                return new ListTag(MagicSpells.spells(), spell -> new ElementTag(spell.getName(), true));
            });
        }
    }

    @Override
    public void init() {
        PropertyParser.registerProperty(MagicSpellsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(SpellCastScriptEvent.class);
        ScriptEvent.registerScriptEvent(SpellCastedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ManaChangeScriptEvent.class);
        ScriptEvent.registerScriptEvent(SpellLearnScriptEvent.class);
        new MagicSpellsTagBase();
    }
}
