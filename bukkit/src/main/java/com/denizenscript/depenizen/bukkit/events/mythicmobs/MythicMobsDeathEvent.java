package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMob;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

// <--[event]
// @Events
// mythicmob mob dies (by <entity>) (in <area>)
// mythicmob mob death (by <entity>) (in <area>)
// mythicmob mob killed (by <entity>) (in <area>)
// mythicmob <mob> dies (by <entity>) (in <area>)
// mythicmob <mob> death (by <entity>) (in <area>)
// mythicmob <mob> killed (by <entity>) (in <area>)

//
// @Regex ^on mythicmob [^\s]+ (dies|death|killed)( by [^\s]+)?( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable false
//
// @Triggers when a MythicMob dies.
//
// @Context
// <context.mob> Returns the MythicMob that has been killed.
// <context.entity> Returns the dEntity for the MythicMob.
// <context.level> Returns the level of the MythicMob.
// <context.drops> Returns a list of items dropped.
// <context.xp> Returns the xp dropped.
//
// @Plugin DepenizenBukkit, MythicMobs
//
// -->

public class MythicMobsDeathEvent extends BukkitScriptEvent implements Listener {

    public MythicMobsDeathEvent() {
        instance = this;
    }

    public static MythicMobsDeathEvent instance;
    public MythicMobDeathEvent event;
    public MythicMobsMob mob;
    public dEntity entity;
    public dEntity killer;
    public Element level;
    public dList oldDrops;
    public Element experience;
    public List<ItemStack> newDrops;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String cmd = CoreUtilities.getXthArg(2, lower);
        return lower.startsWith("mythicmob")
                && (cmd.equals("death") || cmd.equals("dies") || cmd.equals("killed"));
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String mob = CoreUtilities.getXthArg(1, lower);

        if (!mob.equals("mob")
                && !mob.equals(CoreUtilities.toLowerCase(this.mob.getMobType().getInternalName()))) {
            return false;
        }

        // TODO: Remove the stupid from this...
        if ((CoreUtilities.getXthArg(3, lower).equals("by") || CoreUtilities.getXthArg(4, lower).equals("by"))
                && !tryEntity(killer, CoreUtilities.getXthArg(4, lower))
                && !tryEntity(killer, CoreUtilities.getXthArg(5, lower))) {
            return false;
        }

        if (!runInCheck(scriptContainer, s, lower, entity.getLocation())
                && !runInCheck(scriptContainer, s, lower, killer.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "MythicMobsDeath";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MythicMobDeathEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        if (aH.Argument.valueOf(determination).matchesArgumentList(dItem.class)) {
            List<dItem> items = dList.valueOf(determination).filter(dItem.class);
            for (dItem i : items) {
                newDrops.add(i.getItemStack());
            }
        }
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(killer.isPlayer() ? killer.getDenizenPlayer() : null, killer.isNPC() ? killer.getDenizenNPC() : null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("mob")) {
            return mob;
        }
        else if (name.equals("killer")) {
            return killer;
        }
        else if (name.equals("entity")) {
            return entity;
        }
        else if (name.equals("xp")) {
            return experience;
        }
        else if (name.equals("drops")) {
            return oldDrops;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        mob = new MythicMobsMob(event.getMobInstance());
        entity = new dEntity(event.getEntity());
        killer = new dEntity(event.getKiller());
        level = new Element(event.getMobLevel());
        experience = new Element(event.getExp());
        oldDrops = new dList();
        newDrops = new ArrayList<ItemStack>();
        for (ItemStack i : event.getDrops()) {
            oldDrops.add(new dItem(i).identify());
        }
        this.event = event;
        fire();
        if (!newDrops.isEmpty()) {
            event.setDrops(newDrops);
        }
    }
}
