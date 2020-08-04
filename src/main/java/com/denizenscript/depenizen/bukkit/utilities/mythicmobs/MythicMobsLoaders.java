package com.denizenscript.depenizen.bukkit.utilities.mythicmobs;

import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions.DenizenCondition;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.mechanics.DenizenCommandMechanic;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenEntityTargeter;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenLocationTargeter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
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
    // Depenizen adds additional features to Mythic Mobs, such as Targeters, and Conditions.
    //
    // There are two Targeters added by Depenizen that you can use in MM skills
    // The first is @DenizenEntity, and the second is @DenizenLocation
    // Both of these can parse tags that return either an EntityTag, or LocationTag respectively.
    // They both also support returning ListTags, containing their respective tag types.
    // In both cases, <context.entity> is available as an EntityTag of the caster.
    //
    // Conditions are a bit more complex and depending on the type of system they're used in provide different context.
    // The syntax for calling a Denizen tag as a condition is DenizenCondition.
    // The tag should return and ElementTag of a boolean value (true or false).
    // Location based systems will have <context.location>.
    // Entity and Caster conditions provide <context.entity>.
    //
    // Usage Examples
    //
    // @DenizenEntity{tag=<context.entity.location.find.players.within[30].filter[has_flag[marked]]>}
    //
    // @DenizenLocation{tag=<context.entity.location.find.surface_blocks.within[30].random[5]>}
    //
    // @DenizenEntity{tag=<proc[SomeProcScript]>}
    //
    // Condition:
    //   - denizencondition{tag=<context.entity.location.find.players.within[30].is_empty.not>}
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

    @EventHandler
    public void onMythicMechanicsLoad(MythicMechanicLoadEvent event) {
        if (event.getMechanicName().toLowerCase().equals("denizencommand")) {
            event.register(new DenizenCommandMechanic(event.getConfig().getLine(), event.getConfig()));
        }
    }
}
