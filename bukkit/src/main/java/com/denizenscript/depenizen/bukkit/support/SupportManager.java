package com.denizenscript.depenizen.bukkit.support;

import com.denizenscript.depenizen.bukkit.Depenizen;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.depends.Depends;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class SupportManager {

    private final Depenizen depenizen;
    private final PropertyParser propertyParser;
    private final Map<String, Support> additionalTags;
    private boolean hasNewObjects = false;

    public SupportManager(Depenizen depenizen) {
        this.depenizen = depenizen;
        this.propertyParser = DenizenAPI.getCurrentInstance().getPropertyParser();
        this.additionalTags = new HashMap<String, Support>();
        TagManager.registerTagEvents(this);
    }

    public void register(Support support) {
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
                    if (obj.equals(dNPC.class) && Depends.citizens == null) {
                        continue;
                    }
                    propertyParser.registerProperty(prop.getKey(), obj);
                }
            }
        }
        if (support.hasEvents()) {
            for (Class<? extends Listener> event : support.getEvents()) {
                try {
                    depenizen.getServer().getPluginManager().registerEvents(event.newInstance(), depenizen);
                }
                catch (Exception e) {
                    dB.echoError(e);
                }
            }
        }
        if (support.hasScriptEvents()) {
            for (ScriptEvent event : support.getScriptEvents()) {
                try {
                    ScriptEvent.registerScriptEvent(event);
                }
                catch (Exception e) {
                    dB.echoError(e);
                }
            }
        }
        if (support.hasAdditionalTags()) {
            for (String tag : support.getAdditionalTags()) {
                this.additionalTags.put(CoreUtilities.toLowerCase(tag), support);
            }
        }
    }

    public void registerNewObjects() {
        if (hasNewObjects) {
            try {
                ObjectFetcher._initialize();
            }
            catch (Exception e) {
                dB.echoError(e);
            }
            hasNewObjects = false;
        }
    }

    @TagManager.TagEvents
    public void tagListener(ReplaceableTagEvent event) {
        if (event.replaced()) {
            return;
        }
        Attribute attribute = event.getAttributes();
        String replaced = null;
        String name = CoreUtilities.toLowerCase(event.getName());
        for (String tag : additionalTags.keySet()) {
            if (name.startsWith(tag)) {
                replaced = additionalTags.get(tag).additionalTags(attribute, event.getContext());
                break;
            }
        }
        if (replaced != null) {
            event.setReplaced(replaced);
        }
    }
}
