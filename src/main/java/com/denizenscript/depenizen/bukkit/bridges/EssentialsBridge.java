package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.essentials.PlayerAFKStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerGodModeStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerJailStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerMuteStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsItemExtension;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsPlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;

public class EssentialsBridge extends Bridge {

    public static EssentialsBridge instance;

    @Override
    public void init() {
        instance = this;
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "essentials");
        ScriptEvent.registerScriptEvent(new PlayerAFKStatusScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerGodModeStatusScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerJailStatusScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerMuteStatusScriptEvent());
        PropertyParser.registerProperty(EssentialsPlayerExtension.class, dPlayer.class);
        PropertyParser.registerProperty(EssentialsItemExtension.class, dItem.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <essentials.warp[<warp name>]>
        // @returns dLocation
        // @description
        // Returns the location of the warp name.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("warp")) {
            if (attribute.hasContext(1)) {
                Essentials essentials = (Essentials) plugin;
                try {
                    Location loc = essentials.getWarps().getWarp(attribute.getContext(1));
                    event.setReplacedObject(new dLocation(loc).getObjectAttribute(attribute.fulfill(1)));
                }
                catch (WarpNotFoundException e) {
                    dB.echoError("Warp not found");
                }
                catch (InvalidWorldException e) {
                    dB.echoError("Invalid world for getting warp");
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
            Essentials essentials = (Essentials) plugin;
            dList warps = new dList();
            warps.addAll(essentials.getWarps().getList());
            event.setReplacedObject(warps.getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
