package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.BukkitTagContext;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIBridge extends Bridge {

    @Override
    public void init() {
        if (!new PlaceholderHook().register()) {
            dB.echoError("Failed to register placeholder for identifier 'denizen'!" +
                    " Denizen PlaceholderAPI placeholders will not function.");
        }
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "placeholder");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();
        // <--[tag]
        // @attribute <placeholder[<name>]>
        // @returns Element
        // @description
        // Returns the value of the placeholder.
        // @Plugin DepenizenBukkit, PlaceholderAPI
        // -->
        String placeholder = CoreUtilities.toLowerCase(attribute.getContext(1));
        if (placeholder.startsWith("denizen_")) {
            dB.echoError("Cannot use <placeholder[]> tags with 'denizen' prefix!");
            return;
        }
        Player player = ((BukkitTagContext) event.getContext()).player != null ?
                ((BukkitTagContext) event.getContext()).player.getPlayerEntity() : null;
        event.setReplacedObject(new Element(PlaceholderAPI.setPlaceholders(player, "%" + placeholder + "%"))
                .getObjectAttribute(attribute.fulfill(1)));
    }

    private static class PlaceholderHook extends PlaceholderExpansion {

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public String getAuthor() {
            return "The DenizenScript Team";
        }

        @Override
        public String getIdentifier() {
            return "denizen";
        }

        @Override
        public String getVersion() {
            return "2.0.0";
        }

        @Override
        public String onRequest(OfflinePlayer player, String identifier) {
            return TagManager.tag(identifier, new BukkitTagContext(dPlayer.mirrorBukkitPlayer(player), null, false, null, false, null));
        }
    }
}
