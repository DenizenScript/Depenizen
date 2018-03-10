package com.denizenscript.depenizen.bukkit.support;

import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Support {

    ///////////////////
    /// Registration Objects
    ////////

    private final List<Class<? extends dObject>> objects = new ArrayList<Class<? extends dObject>>();

    private final Map<Class<? extends Property>, Class<? extends dObject>> properties
            = new HashMap<Class<? extends Property>, Class<? extends dObject>>();

    private final List<Class<? extends Listener>> events = new ArrayList<Class<? extends Listener>>();

    private final List<ScriptEvent> scriptevents = new ArrayList<ScriptEvent>();

    private final List<String> additionalTags = new ArrayList<String>();

    private static Map<Class<? extends Support>, Plugin> plugins = new HashMap<Class<? extends Support>, Plugin>();

    public static <T extends Plugin> T getPlugin(Class<? extends Support> support) {
        return (T) plugins.get(support);
    }


    ///////////////////
    /// Registration Methods
    ////////

    public void registerObjects(Class<? extends dObject>... objects) {
        for (Class<? extends dObject> object : objects) {
            this.objects.add(object);
        }
    }

    public void registerProperty(Class<? extends Property> property, Class<? extends dObject> objects) {
        properties.put(property, objects);
    }

    public void registerEvents(Class<? extends Listener> event) {
        events.add(event);
    }

    public void registerScriptEvents(ScriptEvent event) {
        scriptevents.add(event);
    }

    public void registerAdditionalTags(String... tags) {
        for (String tag : tags) {
            additionalTags.add(tag);
        }
    }


    ///////////////////
    /// SupportManager Methods
    ////////

    public List<Class<? extends dObject>> getObjects() {
        return objects;
    }

    public Map<Class<? extends Property>, Class<? extends dObject>> getProperties() {
        return properties;
    }

    public List<Class<? extends Listener>> getEvents() {
        return events;
    }

    public List<ScriptEvent> getScriptEvents() {
        return scriptevents;
    }

    public List<String> getAdditionalTags() {
        return additionalTags;
    }

    public boolean hasObjects() {
        return !objects.isEmpty();
    }

    public boolean hasProperties() {
        return !properties.isEmpty();
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    public boolean hasScriptEvents() {
        return !scriptevents.isEmpty();
    }

    public boolean hasAdditionalTags() {
        return !additionalTags.isEmpty();
    }

    public static Support setPlugin(Class<? extends Support> support, Plugin p)
            throws IllegalAccessException, InstantiationException {
        plugins.put(support, p);
        return support.newInstance();
    }


    ///////////////////
    /// Tag Handlers
    ////////

    public String additionalTags(Attribute attribute, TagContext tagContext) {
        return null;
    }
}
