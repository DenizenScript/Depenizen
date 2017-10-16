package com.denizenscript.depenizen.bukkit.events.libsdisguises;

import com.denizenscript.depenizen.bukkit.objects.dLibsDisguise;
import me.libraryaddict.disguise.events.DisguiseEvent;
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
// entity disguises
// entity disguise
//
// @Regex ^on entity [^\s]+ level changes( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable true
//
// @Triggers when a entity disguises.
//
// @Context
// <context.entity> returns the entity who disguised.
// <context.disguise> returns the disguise in use.
//
// @Plugin DepenizenBukkit, LibsDisguises
//
// -->

public class EntityDisguisesScriptEvent extends BukkitScriptEvent implements Listener {

    public EntityDisguisesScriptEvent() {
        instance = this;
    }

    public static EntityDisguisesScriptEvent instance;
    public DisguiseEvent event;
    public dEntity entity;
    public dLibsDisguise disguise;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("entity disguises")
                || lower.startsWith("entity disguise");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String disguiseName = CoreUtilities.getXthArg(1, lower);
        if (disguiseName.equals("disguise")) {
            return true;
        }
        dLibsDisguise ddisguise = dLibsDisguise.valueOf(disguiseName);
        return ddisguise != null && ddisguise.equals(disguise);
    }

    @Override
    public String getName() {
        return "DisguiseEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        DisguiseEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (aH.matchesInteger(lower)) {
            return true;
        }
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
    public void onEntityDisguise(DisguiseEvent event) {
        if (dEntity.isNPC(event.getEntity())) {
            return;
        }
        disguise = new dLibsDisguise(event.getDisguise());
        entity = new dEntity(event.getEntity());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
