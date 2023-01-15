package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceRaidStartEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.UUID;

public class ResidenceRaidStartsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence raid starts
    //
    // @Cancellable true
    //
    // @Switch residence:<residence> to only process the event if the residence matches the input.
    //
    // @Triggers when players start raiding a Residence.
    //
    // @Context
    // <context.residence> Returns a ResidenceTag of the residence that is being attacked.
    // <context.defenders> Returns a ListTag(PlayerTag) of the players defending the Residence.
    // <context.attackers> Returns a ListTag(PlayerTag) of the players attacking the Residence.
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
    public ResidenceTag residence;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("residence", residence)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "attackers":
                ListTag attackers = new ListTag();
                for (UUID uuid : event.getAttackers().keySet()) {
                    attackers.addObject(new PlayerTag(uuid));
                }
                return attackers;
            case "defenders":
                ListTag defenders = new ListTag();
                for (UUID uuid : event.getDefenders().keySet()) {
                    defenders.addObject(new PlayerTag(uuid));
                }
                return defenders;
            case "residence": return residence;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceRaidStartsScriptEvent(ResidenceRaidStartEvent event) {
        this.event = event;
        residence = new ResidenceTag(event.getRes());
        fire(event);
    }
}
