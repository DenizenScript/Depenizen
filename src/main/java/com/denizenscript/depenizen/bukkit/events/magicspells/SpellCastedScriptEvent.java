package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.denizenscript.denizen.objects.EntityTag;
import com.nisovin.magicspells.events.SpellCastedEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpellCastedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells entity completes spell
    // magicspells <entity> completes spell
    //
    // @Regex ^on magicspells [^\s]+ completes spell$
    //
    // @Triggers when the spell is done and everything has been handled.
    //
    // @Context
    // <context.spell_name> returns the name of the spell.
    // <context.caster> returns the entity that casted the spell.
    // <context.power> returns an ElementTag(Decimal) of the power of the spell.
    // <context.cooldown> returns an ElementTag(Decimal) of the cooldown of the spell.
    //
    // @Plugin Depenizen, MagicSpells
    //
    // @Player When the caster is a player.
    //
    // @Group Depenizen
    //
    // -->

    public SpellCastedScriptEvent() {
    }


    public SpellCastedEvent event;
    public EntityTag caster;
    private float power;
    private float cooldown;
    private ElementTag spell;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!path.eventArgLowerAt(0).equals("magicspells")) {
            return false;
        }
        if (!path.eventArgLowerAt(2).equals("completes")) {
            return false;
        }
        if (!path.eventArgLowerAt(3).equals("spell")) {
            return false;
        }
        if (!couldMatchEntity(path.eventArgLowerAt(1))) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryArgObject(1, caster)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(caster);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "power":
                return new ElementTag(power);
            case "caster":
                return caster.getDenizenObject();
            case "cooldown":
                return new ElementTag(cooldown);
            case "spell_name":
                return spell;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellCastedEvent event) {
        caster = new EntityTag(event.getCaster());
        power = event.getPower();
        cooldown = event.getCooldown();
        spell = new ElementTag(event.getSpell().getName());
        this.event = event;
        fire(event);
    }
}
