package com.denizenscript.depenizen.bukkit.events.breweryx;


import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.TimeTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BPlayerTag;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BRecipeTag;
import com.dre.brewery.api.events.brew.BrewDrinkEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryBrewDrinkScriptEvent extends BukkitScriptEvent implements Listener {

    public BreweryBrewDrinkScriptEvent() {
    }

    // <--[event]
    // @Events
    // brewery player drinks <'brew'>
    //
    // @Cancellable true
    //
    // @Triggers when a player drinks a potion that is considered a BreweryX potion.
    //
    // @Context
    // <context.item> Returns an ItemTag of the potion that was drunk.
    // <context.recipe> Returns an BRecipeTag of the recipe that the brew is based off of.
    // <context.bplayer> Returns an BPlayerTag of the player that drank the brew.
    //
    // @Determine
    // "RECIPE:<BRecipeTag>" to change the brew's recipe. Effectively changing the brew to another one.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BrewDrinkEvent event;
    public BRecipeTag recipeTag;
    public BPlayerTag bPlayerTag;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("brewery drink");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "recipe" -> recipeTag;
            case "bplayer" -> bPlayerTag;
            case "item" -> new ItemTag(event.getBrew().);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onBrewDrinkEvent(BrewDrinkEvent event) {
        this.event = event;
        fire(event);
    }
}
