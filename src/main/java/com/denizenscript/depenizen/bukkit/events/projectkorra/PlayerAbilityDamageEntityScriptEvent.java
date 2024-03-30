package com.denizenscript.depenizen.bukkit.events.projectkorra;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.projectkorra.projectkorra.event.AbilityDamageEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAbilityDamageEntityScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // projectkorra player damages player|entity with <ability>
    //
    // @Triggers when a player damages an entity with a bending ability.
    //
    // @Context
    // <context.ability> returns ElementTag(String) of the ability's name.
    // <context.source> returns PlayerTag of the player who triggered the ability.
    // <context.target> returns EntityTag of the target damaged by the ability.
    // <context.damage> returns ElementTag(Decimal) of the damage dealt to the entity.
    // <context.element> returns ElementTag(String) ability's element.
    // <context.cooldown> returns ElementTag(Number) of the ability's cooldown.
    // <context.is_explosive> returns ElementTag(Boolean) if the ability is explosive.
    // <context.is_ignite> returns ElementTag(Boolean) if the ability can ignite.
    // <context.is_sneak> returns ElementTag(Boolean) if the ability is triggered by sneak.
    // <context.ignores_armor> returns ElementTag(Boolean) if the event ignores armor.
    //
    // @Plugin Depenizen, ProjectKorra
    //
    // @Player Always.
    //
    // -->

    public PlayerAbilityDamageEntityScriptEvent() {
        registerCouldMatcher("projectkorra player damages player|entity with <'ability'>");
    }

    public AbilityDamageEntityEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        String ability = path.eventArgLowerAt(5);
        // Check if event applies to any ability
        if (!ability.equals("ability") && !ability.equalsIgnoreCase(event.getAbility().getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(PlayerTag.mirrorBukkitPlayer(event.getAbility().getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "ability" -> new ElementTag(event.getAbility().getName());
            case "source" -> new PlayerTag(event.getSource());
            case "target" -> new EntityTag(event.getEntity());
            case "damage" -> new ElementTag(event.getDamage());
            case "element" -> new ElementTag(event.getAbility().getElement().getName());
            case "cooldown" -> new ElementTag(event.getAbility().getCooldown());
            case "is_explosive" -> new ElementTag(event.getAbility().isExplosiveAbility());
            case "is_ignite" -> new ElementTag(event.getAbility().isIgniteAbility());
            case "is_sneak" -> new ElementTag(event.getAbility().isSneakAbility());
            case "ignores_armor" -> new ElementTag(event.doesIgnoreArmor());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onAbilityStart(AbilityDamageEntityEvent event) {
        this.event = event;
        fire(event);
    }
}
