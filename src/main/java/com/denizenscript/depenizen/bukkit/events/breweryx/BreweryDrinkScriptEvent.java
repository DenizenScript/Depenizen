package com.denizenscript.depenizen.bukkit.events.breweryx;


import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BPlayerTag;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BRecipeTag;
import com.dre.brewery.api.events.brew.BrewDrinkEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryDrinkScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // brewery drink
    //
    // @Cancellable true
    //
    // @Triggers when a player drinks a potion that is considered a BreweryX potion.
    //
    // @Context
    // <context.item> Returns an ItemTag of the potion that was drunk.
    // <context.recipe> Returns an BRecipeTag of the recipe that the brew is based off of.
    // <context.bplayer> Returns an BPlayerTag of the player that drank the brew.
    // <context.player> Returns a PlayerTag of the player that drank the brew.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BreweryDrinkScriptEvent() {
        registerCouldMatcher("brewery drink");
    }

    public BrewDrinkEvent event;
    public BRecipeTag recipeTag;
    public BPlayerTag bPlayerTag;

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
            case "recipe" -> recipeTag;
            case "bplayer" -> bPlayerTag;
            case "item" -> new ItemTag(event.getBrew().createItem(event.getBrew().getCurrentRecipe()));
            case "player" -> new PlayerTag(event.getPlayer());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onBrewDrinkEvent(BrewDrinkEvent event) {
        this.event = event;
        this.recipeTag = new BRecipeTag(event.getBrew().getCurrentRecipe());
        this.bPlayerTag = BPlayerTag.forPlayer(new PlayerTag(event.getPlayer()));
        fire(event);
    }
}
