package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.griefprevention.GriefPreventionPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.events.griefprevention.GPClaimEnterEvent;
import com.denizenscript.depenizen.bukkit.properties.griefprevention.GriefPreventionLocationProperties;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.properties.griefprevention.GriefPreventionWorldProperties;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(GriefPreventionClaimTag.class);
        PropertyParser.registerProperty(GriefPreventionPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(GriefPreventionLocationProperties.class, LocationTag.class);
        PropertyParser.registerProperty(GriefPreventionWorldProperties.class, WorldTag.class);
        ScriptEvent.registerScriptEvent(GPClaimEnterEvent.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "griefprevention");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <griefprevention.list_claims>
        // @returns ListTag(GriefPreventionClaimTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns a list of all GriefPrevention claims.
        // -->
        if (attribute.startsWith("list_claims")) {
            ListTag result = new ListTag();
            for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                result.addObject(new GriefPreventionClaimTag(claim));
            }
            event.setReplacedObject(result.getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
