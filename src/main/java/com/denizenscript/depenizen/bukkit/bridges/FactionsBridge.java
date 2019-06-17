package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.factions.FactionsPlayerNPCExtension;
import com.denizenscript.depenizen.bukkit.objects.dFaction;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.utilities.BridgeLoadException;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.denizenscript.depenizen.bukkit.extensions.factions.FactionsLocationExtension;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagManager;

public class FactionsBridge extends Bridge {

    @Override
    public void init() {
        if (plugin.getDescription().getVersion().startsWith("1.")) {
            throw new BridgeLoadException("Only Factions 1.x.x is supported.");
        }
        ObjectFetcher.registerWithObjectFetcher(dFaction.class);
        PropertyParser.registerProperty(FactionsPlayerNPCExtension.class, dNPC.class);
        PropertyParser.registerProperty(FactionsPlayerNPCExtension.class, dPlayer.class);
        PropertyParser.registerProperty(FactionsLocationExtension.class, dLocation.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                factionTagEvent(event);
            }
        },  "faction");
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "factions");
    }

    public void factionTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);
        // <--[tag]
        // @attribute <faction[<name>]>
        // @returns dFaction
        // @description
        // Returns the faction for the input name.
        // @Plugin DepenizenBukkit, Factions
        // -->
        String nameOrId = attribute.getContext(1);
        Faction f = FactionColl.get().getByName(nameOrId);
        if (f == null && FactionColl.get().containsId(nameOrId)) {
            f = FactionColl.get().get(nameOrId);
        }
        if (f != null) {
            event.setReplacedObject(new dFaction(f).getObjectAttribute(attribute.fulfill(1)));
        }

    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <factions.list_factions>
        // @returns dList(dFaction)
        // @description
        // Returns a list of all current factions.
        // @Plugin DepenizenBukkit, Factions
        // -->
        if (attribute.startsWith("list_factions")) {
            dList factions = new dList();
            for (Faction f : FactionColl.get().getAll()) {
                factions.addObject(new dFaction(f));
            }
            event.setReplacedObject(factions.getObjectAttribute(attribute.fulfill(1)));
        }

    }
}
