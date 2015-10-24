package net.gnomeffinway.depenizen.support.bungee;

import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.gnomeffinway.depenizen.Settings;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeTag implements Listener {

    public BungeeTag(JavaPlugin depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
        TagManager.registerTagEvents(this);
    }

    @TagManager.TagEvents
    public void bungeeTag(ReplaceableTagEvent event) {
        if (!event.matches("bungee") || event.replaced()) return;
        Attribute attribute = event.getAttributes();
        attribute = attribute.fulfill(1);

        // <--[tag]
        // @attribute <bungee.server.name>
        // @returns Element
        // @description
        // returns the current server name (as defined in Depenizen config.yml).
        // -->
        if (attribute.startsWith("server.name")) {
            event.setReplaced(new Element(CoreUtilities.toLowerCase(Settings.socketName())).getAttribute(attribute.fulfill(2)));
            return;
        }
    }
}
