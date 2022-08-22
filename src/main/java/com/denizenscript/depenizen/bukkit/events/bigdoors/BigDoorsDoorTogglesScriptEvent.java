package com.denizenscript.depenizen.bukkit.events.bigdoors;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import nl.pim16aap2.bigDoors.Door;
import nl.pim16aap2.bigDoors.events.DoorEventToggle;
import nl.pim16aap2.bigDoors.events.DoorEventTogglePrepare;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BigDoorsDoorTogglesScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // bigdoors door toggles|opens|closes
    //
    // @Cancellable true
    //
    // @Triggers when a Big Doors door opens or closes.
    //
    // @Context
    // <context.door> returns the Big Doors door being toggled.
    // <context.state> returns the Big Doors open state ('open' or 'close').
    //
    // @Plugin Depenizen, Big Doors
    //
    // @Switch door:<id> to only process the event if the door matches the input.
    //
    // @Group Depenizen
    //
    // -->

    public BigDoorsDoorTogglesScriptEvent() {
        registerCouldMatcher("bigdoors door toggles|opens|closes");
    }

    public DoorEventTogglePrepare event;
    public Door door;
    public DoorEventToggle.ToggleType toggleType;

    @Override
    public boolean matches(ScriptPath path) {
        String cmd = path.eventArgLowerAt(2);
        if (cmd.equals("opens") && toggleType != DoorEventToggle.ToggleType.OPEN) {
            return false;
        }
        if (cmd.equals("closes") && toggleType != DoorEventToggle.ToggleType.CLOSE) {
            return false;
        }
        if (!runGenericSwitchCheck(path, "door", String.valueOf(door.getDoorUID()))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("door")) {
            return new BigDoorsDoorTag(door);
        }
        else if (name.equals("state")) {
            return new ElementTag(toggleType.toString());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onBigDoorsDoorToggles(DoorEventTogglePrepare event) {
        this.event = event;
        this.toggleType = event.getToggleType();
        this.door = event.getDoor();
        fire(event);
    }
}
