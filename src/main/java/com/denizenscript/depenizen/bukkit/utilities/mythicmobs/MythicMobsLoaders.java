package com.denizenscript.depenizen.bukkit.utilities.mythicmobs;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions.DenizenCondition;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenEntityTargeter;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenLocationTargeter;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsLoaders implements Listener {

    public void RegisterEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Denizen.getInstance());
    }

    // <--[language]
    // @name MythicMobs Bridge
    // @group Depenizen Bridges
    // @plugin Depenizen, MythicMobs
    // @description
    // In addition to the the tags, commands, and events found by searching for "mythicmobs" throughout the meta documentation,
    // Depenizen adds additional features to Mythic Mobs: 2 Targeters, and a Condition.
    //
    // Depenizen provides two additional targeters via the MythicMobs API: @DenizenEntity is an entity-based targeter, @DenizenLocation is a location-based targeter.
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
    // Note as well that many parts of MythicMobs configurations allow usage of PlaceholderAPI, which means you can use the "denizen_" placeholder to read tags in any such parts,
    // refer to <@link language PlaceholderAPI Bridge> for more information.
    //
    // Usage Examples
    //
    // Example 1: @DenizenEntity{tag=<context.entity.location.find_players_within[30].filter[has_flag[marked]]>}
    //
    // Example 2: @DenizenLocation{tag=<context.entity.location.find.surface_blocks.within[30].random[5]>}
    //
    // Example 3: @DenizenEntity{tag=<proc[SomeProcScript]>}
    //
    // Conditions:
    // - denizencondition{tag=<context.entity.location.find_players_within[30].is_empty.not>}
    // -->

    @EventHandler
    public void onMythicTargetersLoad(MythicTargeterLoadEvent event) {
        if (CoreUtilities.toLowerCase(event.getTargeterName()).equals("denizenentity")) {
            event.register(new DenizenEntityTargeter(event.getConfig()));
        }
        else if (CoreUtilities.toLowerCase(event.getTargeterName()).equals("denizenlocation")) {
            event.register(new DenizenLocationTargeter(event.getConfig()));
        }
    }

    @EventHandler
    public void onMythicConditionsLoad(MythicConditionLoadEvent event) {
        if (CoreUtilities.toLowerCase(event.getConditionName()).equals("denizencondition")) {
            event.register(new DenizenCondition(event.getConfig().getLine(), event.getConfig()));
        }
    }
}
