package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.essentials.PlayerAFKStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerGodModeStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerJailStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerMuteStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsItemExtension;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;

public class EssentialsSupport extends Support {

    public EssentialsSupport() {
        registerAdditionalTags("essentials");
        registerScriptEvents(new PlayerAFKStatusScriptEvent());
        registerScriptEvents(new PlayerGodModeStatusScriptEvent());
        registerScriptEvents(new PlayerJailStatusScriptEvent());
        registerScriptEvents(new PlayerMuteStatusScriptEvent());
        registerProperty(EssentialsPlayerExtension.class, dPlayer.class);
        registerProperty(EssentialsItemExtension.class, dItem.class);
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("essentials")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <essentials.warp[<warp name>]>
            // @returns dLocation
            // @description
            // Returns the location of the warp name.
            // @Plugin DepenizenBukkit, Essentials
            // -->
            if (attribute.startsWith("warp")) {
                if (attribute.hasContext(1)) {
                    Essentials essentials = Support.getPlugin(EssentialsSupport.class);
                    try {
                        Location loc = essentials.getWarps().getWarp(attribute.getContext(1));
                        return new dLocation(loc).getAttribute(attribute.fulfill(1));
                    }
                    catch (WarpNotFoundException e) {
                        dB.echoError("Warp not found");
                        return null;
                    }
                    catch (InvalidWorldException e) {
                        dB.echoError("Invalid world for getting warp");
                        return null;
                    }
                }
            }

            // <--[tag]
            // @attribute <essentials.list_warps>
            // @returns dList(Element)
            // @description
            // Returns a list of all Warp names.
            // @Plugin DepenizenBukkit, Essentials
            // -->
            if (attribute.startsWith("list_warps")) {
                Essentials essentials = Support.getPlugin(EssentialsSupport.class);
                dList warps = new dList();
                warps.addAll(essentials.getWarps().getList());
                return warps.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
