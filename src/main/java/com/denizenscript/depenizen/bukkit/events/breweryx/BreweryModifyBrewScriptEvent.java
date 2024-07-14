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
    // <context.item> Returns an ItemTag of the potion that was drunk.
    // <context.recipe> Returns an BreweryRecipeTag of the recipe that the brew is based off of.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Group Depenizen
    //
    // -->
    public BreweryModifyBrewScriptEvent() {
        registerCouldMatcher("brewery brew modify");
    }

    public BrewModifyEvent event;
    public BreweryRecipeTag recipeTag;
    public String type;

    @Override
    public boolean matches(ScriptPath path) {
        return super.matches(path);
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
            case "item" -> new ItemTag(event.getBrew().createItem(event.getBrew().getCurrentRecipe()));
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onBrewModifyEvent(BrewModifyEvent event) {
        this.event = event;
        this.recipeTag = new BreweryRecipeTag(event.getBrew().getCurrentRecipe());
        this.type = event.getType().name();
        fire(event);
    }
}
