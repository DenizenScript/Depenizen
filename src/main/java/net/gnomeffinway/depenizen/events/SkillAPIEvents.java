package net.gnomeffinway.depenizen.events;

import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.sucy.skill.api.event.PlayerSkillDowngradeEvent;
import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;
import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class SkillAPIEvents implements Listener {

    // <--[event]
    // @Events
    // player levels up in skillapi
    // @Triggers when a player levels up in SkillAPI.
    // @Context
    // <context.level> returns the level the player went up to.
    // <context.gained> returns how many levels the player gained.
    // @Determine
    // NONE
    // @Plugin SkillAPI
    // -->
    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent event) {
        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("level", new Element(event.getLevel()));
        context.put("gained", new Element(event.getGainedLevels()));

        EventManager.doEvents(Arrays.asList("player levels up in skillapi"), null,
                dPlayer.mirrorBukkitPlayer(event.getPlayerData().getOfflinePlayer()), context);
    }

    // <--[event]
    // @Events
    // player upgrades skill in skillapi
    // player upgrades <skill> in skillapi
    // @Triggers when a player upgrades a skill in SkillAPI.
    // @Context
    // <context.cost> returns the cost of upgrading.
    // <context.skill_name> returns the name of the skill upgraded.
    // @Determine
    // "CANCELLED" to stop the player from upgrading the skill.
    // @Plugin SkillAPI
    // -->
    @EventHandler
    public void onSkillUpgrade(PlayerSkillUpgradeEvent event) {
        String skill = event.getUpgradedSkill().getName();

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("cost", new Element(event.getCost()));
        context.put("skill_name", new Element(skill));

        String determination = EventManager.doEvents(Arrays.asList(
                        "player upgrades skill in skillapi",
                        "player upgrades " + skill + " in skillapi"), null,
                dPlayer.mirrorBukkitPlayer(event.getPlayerData().getOfflinePlayer()), context).toUpperCase();

        if (determination.startsWith("CANCELLED"))
            event.setCancelled(true);
    }

    // <--[event]
    // @Events
    // player downgrades skill in skillapi
    // player downgrades <skill> in skillapi
    // @Triggers when a player downgrades a skill in SkillAPI.
    // @Context
    // <context.skill_name> returns the name of the skill downgraded.
    // @Determine
    // NONE
    // @Plugin SkillAPI
    // -->
    @EventHandler
    public void onSkillDowngrade(PlayerSkillDowngradeEvent event) {
        String skill = event.getDowngradedSkill().getName();

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("skill_name", new Element(skill));

        EventManager.doEvents(Arrays.asList(
                        "player downgrades skill in skillapi",
                        "player downgrades " + skill + " in skillapi"), null,
                dPlayer.mirrorBukkitPlayer(event.getPlayerData().getOfflinePlayer()), context);
    }

    // <--[event]
    // @Events
    // player unlocks skill in skillapi
    // player unlocks <skill> in skillapi
    // @Triggers when a player downgrades a skill in SkillAPI.
    // @Context
    // <context.skill_name> returns the name of the skill downgraded.
    // @Determine
    // NONE
    // @Plugin SkillAPI
    // -->
    @EventHandler
    public void onSkillUnlock(PlayerSkillUnlockEvent event) {
        String skill = event.getUnlockedSkill().getName();

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("skill_name", new Element(skill));

        EventManager.doEvents(Arrays.asList(
                        "player unlocks skill in skillapi",
                        "player unlocks " + skill + " in skillapi"), null,
                dPlayer.mirrorBukkitPlayer(event.getPlayerData().getOfflinePlayer()), context);
    }

}
