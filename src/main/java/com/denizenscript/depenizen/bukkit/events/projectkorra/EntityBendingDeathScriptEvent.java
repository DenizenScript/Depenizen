package com.denizenscript.depenizen.bukkit.events.projectkorra;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.projectkorra.projectkorra.event.EntityBendingDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityBendingDeathScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // projectkorra <entity> killed
    //
    // @Switch by:<ability> to only process the event if the ability matches the specified ability.
    //
    // @Triggers when an entity dies from bending.
    //
    // @Context
    // <context.ability> returns the ability's name.
    // <context.source> returns the player who triggered the ability.
    // <context.target> returns the target damaged by the ability.
    // <context.damage> returns the damage dealt to the entity as a decimal.
    // <context.element> returns the ability's element name.
    // <context.is_explosive> returns if the ability is explosive.
    // <context.is_ignite> returns if the ability can ignite.
    // <context.is_sneak> returns if the ability is triggered by sneak.
    //
    // @Plugin Depenizen, ProjectKorra
    //
    // @Player Always.
    //
    // -->

    public EntityBendingDeathScriptEvent() {
        registerCouldMatcher("projectkorra entity killed");
        registerSwitches("by");
    }

    public EntityBendingDeathEvent event;
    public ElementTag ability;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("by", ability)) {
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
            case "ability" -> ability;
            case "source" -> new PlayerTag(event.getAttacker());
            case "target" -> new EntityTag(event.getEntity());
            case "damage" -> new ElementTag(event.getDamage());
            case "element" -> new ElementTag(event.getAbility().getElement().getName());
            case "is_explosive" -> new ElementTag(event.getAbility().isExplosiveAbility());
            case "is_ignite" -> new ElementTag(event.getAbility().isIgniteAbility());
            case "is_sneak" -> new ElementTag(event.getAbility().isSneakAbility());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onBendingDeath(EntityBendingDeathEvent event) {
        this.event = event;
        this.ability = new ElementTag(event.getAbility().getName());
        fire(event);
    }
}
