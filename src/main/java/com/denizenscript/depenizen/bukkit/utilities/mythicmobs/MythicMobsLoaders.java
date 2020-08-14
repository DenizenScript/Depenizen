package com.denizenscript.depenizen.bukkit.utilities.mythicmobs;

import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions.DenizenCondition;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenEntityTargeter;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenLocationTargeter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsLoaders implements Listener {

    public void RegisterEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    // <--[language]
    // @name MythicMobs Additions
    // @group Depenizen External Additions
    // @description
    // Depenizen adds additional features to Mythic Mobs, 2 Targeters, and a Condition.
    //
    // Depenizen provides two additional targeters via the MythicMobs API.
    // @DenizenEntity is an entity-based targeter, @DenizenLocation is a location-based targeter.
    // Both targeters can parse tags; they accept input of either an EntityTag or LocationTag respectively.
    // Both targeters also support returning ListTags containing their respective tag types.
    // Both targeters provide <context.entity> as an EntityTag of the caster.
    //
    // Conditions provide different contexts depending on their usage.
    // The syntax for calling a Denizen tag as a condition is DenizenCondition.
    // The tag should return an ElementTag of a boolean value (true or false).
    // <context.location> is available for location-based checks,
    // <context.entity> is available for for entity- and caster-based checks,
    // and <context.target> is available for target-based checks.
    // NOTE: TriggerConditions are NOT currently supported.
    //
    // Usage Examples
    //
    // Exmaple 1: @DenizenEntity{tag=<context.entity.location.find.players.within[30].filter[has_flag[marked]]>}
    //
    // Example 2: @DenizenLocation{tag=<context.entity.location.find.surface_blocks.within[30].random[5]>}
    //
    // Example 3: @DenizenEntity{tag=<proc[SomeProcScript]>}
    //
    // Conditions:
    // - denizencondition{tag=<context.entity.location.find.players.within[30].is_empty.not>}
    // -->

    @EventHandler
    public void onMythicTargetersLoad(MythicTargeterLoadEvent event) {
        if (event.getTargeterName().toLowerCase().equals("denizenentity")) {
            event.register(new DenizenEntityTargeter(event.getConfig()));
        }
        else if (event.getTargeterName().toLowerCase().equals("denizenlocation")) {
            event.register(new DenizenLocationTargeter(event.getConfig()));
        }
    }

    @EventHandler
    public void onMythicConditionsLoad(MythicConditionLoadEvent event) {
        if (event.getConditionName().toLowerCase().equals("denizencondition")) {
            event.register(new DenizenCondition(event.getConfig().getLine(), event.getConfig()));
        }
    }
}
