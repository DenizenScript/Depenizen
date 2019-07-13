package com.denizenscript.depenizen.bukkit.events.libsdisguises;

import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguiseTag;
import me.libraryaddict.disguise.events.UndisguiseEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityUndisguisesScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // libsdisguises undisguises disguise
    // libsdisguises undisguise disguise
    // libsdisguises undisguises <dLibsDisguises>
    // libsdisguises undisguise <dLibsDisguises>
    //
    // @Regex ^on libsdisguises [^\s]+ undisguise$
    //
    // @Cancellable true
    //
    // @Triggers when a entity undisguises.
    //
    // @Context
    // <context.entity> returns the entity who undisguised.
    // <context.disguise> returns the disguise in use.
    //
    // @Plugin Depenizen, LibsDisguises
    //
    // -->

    public EntityUndisguisesScriptEvent() {
        instance = this;
    }

    public static EntityUndisguisesScriptEvent instance;

    public UndisguiseEvent event;
    public EntityTag entity;
    public LibsDisguiseTag disguise;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("libsdisguises undisguise");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String disguiseName = path.eventArgLowerAt(2);

        if (disguiseName.equals("disguise")) {
            return true;
        }

        LibsDisguiseTag dDisguise = LibsDisguiseTag.valueOf(disguiseName);
        return dDisguise != null && dDisguise.equals(disguise);
    }

    @Override
    public String getName() {
        return "UndisguiseEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("entity")) {
            return entity;
        }
        else if (name.equals("disguise")) {
            return disguise;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onUndisguise(UndisguiseEvent event) {
        disguise = new LibsDisguiseTag(event.getDisguise());
        entity = new EntityTag(event.getEntity());
        this.event = event;
        fire(event);
    }
}
