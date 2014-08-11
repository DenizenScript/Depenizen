package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Support {

    ///////////////////
    /// Registration Objects
    ////////

    private final List<Class<? extends dObject>> objects = new ArrayList<Class<? extends dObject>>();

    private final Map<Class<? extends Property>, Class<? extends dObject>[]> properties
            = new HashMap<Class<? extends Property>, Class<? extends dObject>[]>();

    private final List<Class<? extends Listener>> events = new ArrayList<Class<? extends Listener>>();

    private final List<String> additionalTags = new ArrayList<String>();

    private Plugin plugin = null;

    public <T extends Plugin> T getPlugin() {
        return (T) plugin;
    }


    ///////////////////
    /// Registration Methods
    ////////

    public void registerObjects(Class<? extends dObject>... objects) {
        for (Class<? extends dObject> object : objects) {
            this.objects.add(object);
        }
    }

    public void registerProperty(Class<? extends Property> property, Class<? extends dObject>... objects) {
        properties.put(property, objects);
    }

    public void registerEvents(Class<? extends Listener> event) {
        events.add(event);
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

    public Map<Class<? extends Property>, Class<? extends dObject>[]> getProperties() {
        return properties;
    }

    public List<Class<? extends Listener>> getEvents() {
        return events;
    }

    public List<String> getAdditionalTags() {
        return additionalTags;
    }

    public boolean hasObjects() { return !objects.isEmpty(); }

    public boolean hasProperties() { return !properties.isEmpty(); }

    public boolean hasEvents() { return !events.isEmpty(); }

    public boolean hasAdditionalTags() { return !additionalTags.isEmpty(); }

    public Support setPlugin(Plugin plugin) {
        this.plugin = plugin;
        return this;
    }


    ///////////////////
    /// Tag Handlers
    ////////

    public String playerTags(dPlayer player, Attribute attribute) {
        return null;
    }

    public String locationTags(dLocation location, Attribute attribute) {
        return null;
    }

    public String npcTags(dNPC npc, Attribute attribute) {
        return null;
    }

    public String additionalTags(Attribute attribute) {
        return null;
    }

}
