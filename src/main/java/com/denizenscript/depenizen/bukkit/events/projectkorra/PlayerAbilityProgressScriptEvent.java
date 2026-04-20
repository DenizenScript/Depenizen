package com.denizenscript.depenizen.bukkit.events.projectkorra;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.projectkorra.projectkorra.event.AbilityProgressEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAbilityProgressScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // projectkorra player progresses
    //
    // @Switch ability:<ability> to only process the event if the ability matches the specified ability.
    //
    // @Triggers when a player progresses a bending ability.
    //
    // @Context
    // <context.ability> returns the ability's name.
    // <context.source> returns the player who triggered the ability.
    // <context.element> returns the ability's element name.
    // <context.cooldown> returns the ability's cooldown.
    // <context.is_explosive> returns if the ability is explosive.
    // <context.is_harmless> returns if the ability is harmless.
    // <context.is_hidden> returns if the ability is hidden.
    // <context.is_ignite> returns if the ability can ignite.
    // <context.is_sneak> returns if the ability is triggered by sneak.
    //
    // @Plugin Depenizen, ProjectKorra
    //
    // @Player Always.
    //
    // -->

    public PlayerAbilityProgressScriptEvent() {
        registerCouldMatcher("projectkorra player progresses");
        registerSwitches("ability");
    }

    public AbilityProgressEvent event;
    public ElementTag ability;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("ability", ability)) {
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
            case "source" -> new PlayerTag(event.getAbility().getPlayer());
            case "element" -> new ElementTag(event.getAbility().getElement().getName());
            case "cooldown" -> new ElementTag(event.getAbility().getCooldown());
            case "is_explosive" -> new ElementTag(event.getAbility().isExplosiveAbility());
            case "is_harmless" -> new ElementTag(event.getAbility().isHarmlessAbility());
            case "is_hidden" -> new ElementTag(event.getAbility().isHiddenAbility());
            case "is_ignite" -> new ElementTag(event.getAbility().isIgniteAbility());
            case "is_sneak" -> new ElementTag(event.getAbility().isSneakAbility());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onAbilityProgress(AbilityProgressEvent event) {
        this.event = event;
        this.ability = new ElementTag(event.getAbility().getName());
        fire(event);
    }
}
