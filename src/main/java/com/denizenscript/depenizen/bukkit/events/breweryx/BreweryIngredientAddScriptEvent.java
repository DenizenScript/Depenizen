package com.denizenscript.depenizen.bukkit.events.breweryx;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.dre.brewery.api.events.IngedientAddEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryIngredientAddScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // brewery ingredient add
    //
    // @Cancellable true
    //
    // @Triggers when a player adds an ingredient to a cauldron.
    //
    // @Context
    // <context.item> Returns an ItemTag of the ingredient that was added.
    // <context.location> Returns a LocationTag of the cauldron.
    // <context.player> Returns a PlayerTag of the player that added the ingredient.
    // <context.take_item> Returns an ElementTag of whether the item should be taken from the player.
    //
    // @Determine
    // ItemTag to set the ingredient that was added.
    // ElementTag(Boolean) to set whether the item should be taken from the player.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BreweryIngredientAddScriptEvent() {
        registerCouldMatcher("brewery ingredient add");
    }

    public IngedientAddEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "location" -> new LocationTag(event.getBlock().getLocation());
            case "player" -> new PlayerTag(event.getPlayer());
            case "item" -> new ItemTag(event.getIngredient());
            case "take_item" -> new ElementTag(event.willTakeItem());
            default -> super.getContext(name);
        };
    }

    @Override
    public boolean handleDetermination(ScriptPath path, String prefix, ObjectTag value) {
        switch (prefix) {
            case "item" -> {
                event.setIngredient(((ItemTag) value).getItemStack());
                return true;
            }
            case "take_item" -> {
                event.setTakeItem(((ElementTag) value).asBoolean());
                return true;
            }
        }
        return super.handleDetermination(path, prefix, value);
    }

    @EventHandler
    public void onIngredientAddEvent(IngedientAddEvent event) {
        this.event = event;
        fire(event);
    }
}
