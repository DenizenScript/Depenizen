package com.denizenscript.depenizen.bukkit.events.breweryx;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BRecipeTag;
import com.dre.brewery.api.events.brew.BrewEvent;
import com.dre.brewery.api.events.brew.BrewModifyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryModifyBrewScriptEvent extends BukkitScriptEvent implements Listener {

    public BreweryModifyBrewScriptEvent() {
    }

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
    // <context.item> Returns an ItemTag of the potion that was drunk.
    // <context.recipe> Returns an BRecipeTag of the recipe that the brew is based off of.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BrewModifyEvent event;
    public BRecipeTag recipeTag;
    public String type;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("brewery modify brew");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "recipe" -> recipeTag;
            case "type" -> new ElementTag(type);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onBrewDrinkEvent(BrewModifyEvent event) {
        this.event = event;
        this.recipeTag = new BRecipeTag(event.getBrew().getCurrentRecipe());
        this.type = event.getType().name();
        fire(event);
    }
}
