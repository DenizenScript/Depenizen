package com.denizenscript.depenizen.bukkit.events.breweryx;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BreweryRecipeTag;
import com.dre.brewery.api.events.brew.BrewModifyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryModifyBrewScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // brewery brew modify
    //
    // @Cancellable true
    //
    // @Triggers When a Brew has been created or modified.
    // Usually happens on filling from cauldron, distilling and aging.
    // Modifications to the Brew or the PotionMeta can be done now
    // Cancelling reverts the Brew to the state it was before the modification
    //
    // @Context
    // <context.player> Returns a PlayerTag of the player that modified the brew.
    // <context.recipe> Returns an BreweryRecipeTag of the recipe that the brew is based off of.
    // <context.item> Returns an ItemTag of the potion.
    // <context.type> Returns an ElementTag of the type of modification.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BreweryModifyBrewScriptEvent() {
        registerCouldMatcher("brewery brew modify");
    }

    public BrewModifyEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "recipe" -> new BreweryRecipeTag(event.getBrew().getCurrentRecipe());
            case "item" -> new ItemTag(event.getBrew().createItem(event.getBrew().getCurrentRecipe(), false));
            case "type" -> new ElementTag(event.getType());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onBrewModifyEvent(BrewModifyEvent event) {
        this.event = event;
        fire(event);
    }
}
