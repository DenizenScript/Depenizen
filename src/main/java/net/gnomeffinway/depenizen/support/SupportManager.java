package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.objects.properties.PropertyParser;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.depends.Depends;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.event.*;

import java.util.*;

public class SupportManager implements Listener {

    private final Depenizen depenizen;
    private final PropertyParser propertyParser;
    public final Map<String, Support> supported;
    private final Map<String, Support> additionalTags;
    private boolean hasNewObjects = false;

    public SupportManager(Depenizen depenizen) {
        this.depenizen = depenizen;
        this.propertyParser = DenizenAPI.getCurrentInstance().getPropertyParser();
        this.supported = new HashMap<String, Support>();
        this.additionalTags = new HashMap<String, Support>();
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    public void register(Support support) {
        supported.put(support.getPlugin().getName().toUpperCase(), support);
        if (support.hasObjects()) {
            for (Class<? extends dObject> object : support.getObjects()) {
                ObjectFetcher.registerWithObjectFetcher(object);
            }
            hasNewObjects = true;
        }
        if (support.hasProperties()) {
            for (Map.Entry<Class<? extends Property>, Class<? extends dObject>[]> prop
                    : support.getProperties().entrySet()) {
                for (Class<? extends dObject> obj : prop.getValue()) {
                    if (obj.equals(dNPC.class) && Depends.citizens == null) continue;
                    propertyParser.registerProperty(prop.getKey(), obj);
                }
            }
        }
        if (support.hasEvents()) {
            for (Class<? extends Listener> event : support.getEvents()) {
                try {
                    depenizen.getServer().getPluginManager().registerEvents(event.newInstance(), depenizen);
                } catch (Exception e) {
                    dB.echoError(e);
                }
            }
        }
        if (support.hasAdditionalTags()) {
            for (String tag : support.getAdditionalTags()) {
                this.additionalTags.put(tag, support);
            }
        }
    }

    public void registerNewObjects() {
        if (hasNewObjects) {
            try { ObjectFetcher._initialize(); }
            catch (Exception e) {
                dB.echoError(e);
            }
            hasNewObjects = false;
        }
    }

    private String findPlayerTags(dPlayer player, Attribute attribute) {
        for (Support support : supported.values()) {
            String ret = support.playerTags(player, attribute);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    private String findNPCTags(dNPC npc, Attribute attribute) {
        for (Support support : supported.values()) {
            String ret = support.npcTags(npc, attribute);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    private String findLocationTags(dLocation location, Attribute attribute) {
        for (Support support : supported.values()) {
            String ret = support.locationTags(location, attribute);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    private String findAdditionalTags(String name, Attribute attribute) {
        for (String tag : additionalTags.keySet()) {
            if (name.equalsIgnoreCase(tag)) {
                return additionalTags.get(tag).additionalTags(attribute);
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void tagListener(ReplaceableTagEvent event) {

        if (event.replaced()) return;

        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());

        String replaced;

        if (event.matches("player, pl")) {

            dPlayer p = event.getPlayer();

            if (attribute.hasContext(1)) {
                if (dPlayer.matches(attribute.getContext(1))) {
                    p = dPlayer.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoDebug(event.getScriptEntry(), "Could not match '"
                            + attribute.getContext(1) + "' to a valid player!");
                    return;
                }
            }

            if (p == null || !p.isValid()) {
                dB.echoDebug(event.getScriptEntry(), "Invalid or missing player for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            replaced = findPlayerTags(p, attribute.fulfill(1));

        }

        else if (event.matches("npc, n") && Depends.citizens != null) {

            dNPC n = event.getNPC();

            if (attribute.hasContext(1)) {
                if (dNPC.matches(attribute.getContext(1)))
                    n = dNPC.valueOf(attribute.getContext(1));
                else {
                    dB.echoDebug(event.getScriptEntry(), "Could not match '" + attribute.getContext(1) + "' to a valid NPC!");
                    return;
                }
            }

            if (n == null || !n.isValid()) {
                dB.echoDebug(event.getScriptEntry(), "Invalid or missing NPC for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            replaced = findNPCTags(n, attribute.fulfill(1));

        }

        else if (event.matches("location, l")) {

            dLocation loc = null;

            if (event.hasNameContext() && dLocation.matches(event.getNameContext()))
                loc = dLocation.valueOf(event.getNameContext());
            else if (event.getScriptEntry().hasObject("location"))
                loc = (dLocation) event.getScriptEntry().getObject("location");

            if (loc == null) {
                event.setReplaced("null");
                return;
            }

            replaced = findLocationTags(loc, attribute.fulfill(1));

        }

        else {
            replaced = findAdditionalTags(event.getName(), attribute);
        }

        if (replaced != null) {
            event.setReplaced(replaced);
        }

    }

}
