package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class MythicMobsDespawnEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mythicmob <'mob'> despawns
    //
    // @Location true
    //
    // @Triggers when a MythicMob despawns.
    //
    // @Context
    // <context.mob> Returns the MythicMob that is despawning.
    // <context.entity> Returns the EntityTag for the MythicMob.
    //
    // @Plugin Depenizen, MythicMobs
    //
    // @Group Depenizen
    //
    // -->

    public MythicMobsDespawnEvent() {
        registerCouldMatcher("mythicmob <'mob'> despawns");
    }

    public MythicMobDespawnEvent event;
    public MythicMobsMobTag mythicmob;

    @Override
    public boolean matches(ScriptPath path) {
        String mob = path.eventArgLowerAt(1);
        if (!mob.equals("mob") && !runGenericCheck(mob, mythicmob.getMobType().getInternalName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "mob" -> mythicmob;
            case "entity" -> new EntityTag(event.getEntity());
            default -> super.getContext(name);
        };
    }
    @EventHandler
    public void onMythicMobDespawns(MythicMobDespawnEvent event) {
        mythicmob = new MythicMobsMobTag(event.getMob());
        this.event = event;
        EntityTag.rememberEntity(event.getEntity());
        fire(event);
        EntityTag.forgetEntity(event.getEntity());
    }
}
