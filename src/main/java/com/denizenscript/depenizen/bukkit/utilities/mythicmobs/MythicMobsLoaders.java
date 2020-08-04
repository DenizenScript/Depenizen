package com.denizenscript.depenizen.bukkit.utilities.mythicmobs;

import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenEntityTargeter;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters.DenizenLocationTargeter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsLoaders implements Listener {

    public void RegisterEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    // <--[language]
    // @name MythicMob Additions
    // @group Depenizen External Additions
    // @description
    // Depenizen adds additional features to Mythic Mobs, such as Targeters.
    //
    // There are two Targeters added by Depenizen that you can use in MM skills
    // The first is @DenizenEntity, and the second is @DenizenLocation
    // Both of these can parse tags that return either an EntityTag, or LocationTag respectively.
    // They both also support returning ListTags, containing their respective tag types.
    // In both cases, <context.entity> is available as an EntityTag of the caster.
    //
    // Usage:
    // @DenizenEntity{tag=<context.entity.location.find.players.within[30].filter[has_flag[marked]]>}
    //
    // @DenizenLocation{tag=<context.entity.location.find.surface_blocks.within[30].random[5]>}
    //
    // @DenizenEntity{tag=<proc[SomeProcScript]>}
    //
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
}
