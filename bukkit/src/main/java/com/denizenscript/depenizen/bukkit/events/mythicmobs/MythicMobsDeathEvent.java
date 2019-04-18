package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMob;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
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
// <context.killer> returns the dEntity that killed the MythicMob (if available).
// <context.level> Returns the level of the MythicMob.
// <context.drops> Returns a list of items dropped.
// <context.xp> Returns the xp dropped.
//
// @Determine
// Element(Number) to specify the new amount of XP to be dropped.
// dList(dItem) to specify new items to be dropped.
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
    public boolean matches(ScriptPath path) {
        String mob = path.eventArgLowerAt(1);

        if (!mob.equals("mob")
                && !mob.equals(CoreUtilities.toLowerCase(this.mob.getMobType().getInternalName()))) {
            return false;
        }

        // TODO: Remove the stupid from this...
        if ((path.eventArgLowerAt(3).equals("by") || path.eventArgLowerAt(4).equals("by"))
                && !tryEntity(killer, path.eventArgLowerAt(4))
                && !tryEntity(killer, path.eventArgLowerAt(5))) {
            return false;
        }

        if (!runInCheck(path, entity.getLocation())
                && !runInCheck(path, killer.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "MythicMobsDeath";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (aH.matchesInteger(lower)) {
            experience = new Element(lower);
            return true;
        }
        else if (aH.Argument.valueOf(determination).matchesArgumentList(dItem.class)) {
            List<dItem> items = dList.valueOf(determination).filter(dItem.class, container);
            for (dItem i : items) {
                newDrops.add(i.getItemStack());
            }
            return true;
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
        else if (name.equals("level")) {
            return level;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        mob = new MythicMobsMob(event.getMob());
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
        event.setExp(experience.asInt());
    }
}
