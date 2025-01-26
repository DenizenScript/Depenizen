package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.essentials.*;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsItemExtensions;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsPlayerProperties;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;

public class EssentialsBridge extends Bridge {

    static class EssentialsTagBase extends PseudoObjectTagBase<EssentialsTagBase> {

        public static EssentialsTagBase instance;

        public EssentialsTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(EssentialsTagBase.class, "essentials", (t) -> instance);
        }

        @Override
        public void register() {

            // <--[tag]
            // @attribute <essentials.warp[<warp_name>]>
            // @returns LocationTag
            // @plugin Depenizen, Essentials
            // @description
            // Returns the location of the warp name.
            // -->
            tagProcessor.registerTag(LocationTag.class, ElementTag.class, "warp", (attribute, object, name) -> {
                try {
                    return new LocationTag(essentials.getWarps().getWarp(name.toString()));
                }
                catch (WarpNotFoundException e) {
                    attribute.echoError("Warp not found");
                }
                return null;
            });

            // <--[tag]
            // @attribute <essentials.list_warps>
            // @returns ListTag
            // @plugin Depenizen, Essentials
            // @description
            // Returns a list of all Warp names.
            // -->
            tagProcessor.registerTag(ListTag.class, "list_warps", (attribute, object) -> {
                return new ListTag(essentials.getWarps().getList(), true);
            });
        }
    }

    public static EssentialsBridge instance;
    public static Essentials essentials;

    @Override
    public void init() {
        instance = this;
        ScriptEvent.registerScriptEvent(PlayerAFKStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerGodModeStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerJailStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerMuteStatusScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerBalanceChangeScriptEvent.class);
        PropertyParser.registerProperty(EssentialsPlayerProperties.class, PlayerTag.class);
        EssentialsItemExtensions.register();
        essentials = (Essentials) plugin;
        new EssentialsTagBase();
    }

}
