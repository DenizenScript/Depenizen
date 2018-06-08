package com.denizenscript.depenizen.bukkit.events.libsdisguises;

import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import me.libraryaddict.disguise.events.UndisguiseEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
// @Plugin DepenizenBukkit, LibsDisguises
//
// -->

public class EntityUndisguisesScriptEvent extends BukkitScriptEvent implements Listener {

    public EntityUndisguisesScriptEvent() {
        instance = this;
    }

    public static EntityUndisguisesScriptEvent instance;

    public UndisguiseEvent event;
    public dEntity entity;
    public LibsDisguise disguise;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("libsdisguises undisguise");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String disguiseName = CoreUtilities.getXthArg(2, lower);

        if (disguiseName.equals("disguise")) {
            return true;
        }

        LibsDisguise dDisguise = LibsDisguise.valueOf(disguiseName);
        return dDisguise != null && dDisguise.equals(disguise);
    }

    @Override
    public String getName() {
        return "UndisguiseEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        UndisguiseEvent.getHandlerList().unregister(this);
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
    public dObject getContext(String name) {
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
        disguise = new LibsDisguise(event.getDisguise());
        entity = new dEntity(event.getEntity());

        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
