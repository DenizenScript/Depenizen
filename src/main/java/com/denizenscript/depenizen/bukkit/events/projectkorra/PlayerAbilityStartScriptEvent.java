package com.denizenscript.depenizen.bukkit.events.projectkorra;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.projectkorra.projectkorra.event.AbilityStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAbilityStartScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // projectkorra player starts ability <ability>
    //
    // @Triggers when a player starts a bending ability.
    //
    // @Context
    // <context.ability> returns ElementTag(String) of the ability's name.
    // <context.source> returns PlayerTag of the player who triggered the ability.
    // <context.element> returns ElementTag(String) ability's element.
    // <context.cooldown> returns ElementTag(Number) of the ability's cooldown.
    // <context.is_explosive> returns ElementTag(Boolean) if the ability is explosive.
    // <context.is_harmless> returns ElementTag(Boolean) if the ability is harmless.
    // <context.is_hidden> returns ElementTag(Boolean) if the ability is hidden.
    // <context.is_ignite> returns ElementTag(Boolean) if the ability can ignite.
    // <context.is_sneak> returns ElementTag(Boolean) if the ability is triggered by sneak.
    //
    // @Plugin Depenizen, ProjectKorra
    //
    // @Player Always.
    //
    // -->

    public PlayerAbilityStartScriptEvent() {
        registerCouldMatcher("projectkorra player starts <'ability'>");
    }

    public AbilityStartEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        String ability = path.eventArgLowerAt(3);
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
    public void onAbilityStart(AbilityStartEvent event) {
        this.event = event;
        fire(event);
    }
}
