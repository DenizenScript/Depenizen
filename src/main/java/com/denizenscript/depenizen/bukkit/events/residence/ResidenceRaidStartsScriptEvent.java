package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceRaidStartEvent;
import com.bekvon.bukkit.residence.raid.RaidAttacker;
import com.bekvon.bukkit.residence.raid.RaidDefender;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ResidenceRaidStartsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence raid starts
    //
    // @Switch residence:<residence_name> to only process the event if the residence name matches specified name.
    //
    // @Triggers when a player(s) starts raiding a Residence.
    //
    // @Context
    // <context.residence> Returns a ResidenceTag of residence that is being attacked.
    // <context.defenders> Returns a ListTag(PlayerTag) of players defending the Residence.
    // <context.attackers> Returns a ListTag(PlayerTag) of players attacking the Residence.
    //
    // @Plugin Depenizen, Residence
    //
    // @Group Depenizen
    //
    // -->

    public ResidenceRaidStartsScriptEvent() {
        registerCouldMatcher("residence raid starts");
        registerSwitches("residence");
    }

    public ResidenceRaidStartEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "residence", event.getRes().getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "attackers":
                ListTag attackers = new ListTag();
                for (RaidAttacker player : event.getAttackers().values()) {
                    attackers.addObject(new PlayerTag(player.getPlayer().getPlayer()));
                }
                return attackers;
            case "defenders":
                ListTag defenders = new ListTag();
                for (RaidDefender player : event.getDefenders().values()) {
                    defenders.addObject(new PlayerTag(player.getPlayer().getPlayer()));
                }
                return defenders;
            case "residence":
                return new ResidenceTag(event.getRes());
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onResidenceRaidStartsScriptEvent(ResidenceRaidStartEvent event) {
        this.event = event;
        fire(event);
    }
}
