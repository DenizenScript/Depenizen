package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.essentials.*;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsItemProperties;
import com.denizenscript.depenizen.bukkit.properties.essentials.EssentialsPlayerProperties;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;

public class EssentialsBridge extends Bridge {

    static class EssentialsTagBase extends PseudoObjectTagBase<EssentialsTagBase> {

        public static EssentialsTagBase instance;

        public EssentialsTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(EssentialsTagBase.class, "essentials", (t) -> instance);
        }

        public void register() {

            // <--[tag]
            // @attribute <essentials.warp[<warp_name>]>
            // @returns LocationTag
            // @plugin Depenizen, Essentials
            // @description
            // Returns the location of the warp name.
            // -->
            tagProcessor.registerTag(LocationTag.class, ElementTag.class, "warp", (object, attribute, name) -> {
                try {
                    return new LocationTag(essentials.getWarps().getWarp(name.toString()));
                }
                catch (WarpNotFoundException e) {
                    Debug.echoError("Warp not found");
                }
                catch (InvalidWorldException e) {
                    Debug.echoError("Invalid world for getting warp");
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
            tagProcessor.registerTag(ListTag.class, "list_warps", (object, attribute) -> {
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
        PropertyParser.registerProperty(EssentialsItemProperties.class, ItemTag.class);
        essentials = (Essentials) plugin;
        new EssentialsTagBase();
    }

}
