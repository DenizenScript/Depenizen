package com.denizenscript.depenizen.bukkit.commands.heroes;

import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.HeroesSupport;
import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class HeroesXPCommand extends AbstractCommand {

    // <--[command]
    // @Name HeroesXP
    // @Syntax heroesxp [add/remove/set] [<heroesclass>] [quantity:<#.#>]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, Heroes
    // @Required 3
    // @Stable stable
    // @Short Manipulate Heroes' experience.
    // @Author Fortifier42

    // @Description
    // This command allows you add, remove or set experience of a player's Hero.
    // NOTE: Currently unable to change levels of Heroes' easily. (Maybe a TODO.)

    // @Tags
    // <player.heroes. * >

    // @Usage
    // Use to add 100 xp to the player's warrior class.
    // - heroesxp add warrior quantity:100

    // @Usage
    // Use to remove 100 xp from the player's warrior class.
    // - heroesxp remove heroesclass@warrior quantity:100

    // @Usage
    // Use to set the xp of the player's warrior class to 100.
    // - heroesxp set heroesclass@warrior quantity:100

    // @Usage
    // Use to remove 10xp from another player's archer class.
    // - heroesxp remove archer quantity:10 player:p@mcmonkey4eva

    // -->

    public enum Action {
        ADD,
        REMOVE,
        SET
    }

    Heroes heroes = Support.getPlugin(HeroesSupport.class);

    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {
            if (!scriptEntry.hasObject("action") && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else if (!scriptEntry.hasObject("class")
                    && arg.matchesArgumentType(HeroesClass.class)) {
                scriptEntry.addObject("class", arg.asType(HeroesClass.class));
            }
            else if (!scriptEntry.hasObject("quantity")
                    && arg.matchesPrefix("q", "qty", "quantity")
                    && arg.matchesPrimitive(aH.PrimitiveType.Double)) {
                scriptEntry.addObject("quantity", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }

        if (!((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
            throw new InvalidArgumentsException("This command must have a player attached!");
        }
        else if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }
        else if (!scriptEntry.hasObject("class")) {
            throw new InvalidArgumentsException("Must specify a valid class!");
        }
        else if (!scriptEntry.hasObject("quantity")) {
            throw new InvalidArgumentsException("Must specify a valid quantity!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        BukkitScriptEntryData scriptEntryData = (BukkitScriptEntryData) scriptEntry.entryData;

        Element action = scriptEntry.getElement("action");
        HeroesClass hclass = (HeroesClass) scriptEntry.getObject("class");
        Element quantity = scriptEntry.getElement("quantity");

        dPlayer player = scriptEntryData.getPlayer();

        dB.report(scriptEntry, getName(),
                action.debug() + hclass.debug() + quantity.debug());

        if (!player.isOnline()) {
            dB.echoError(scriptEntry.getResidingQueue(), "Attached player must be online!");
            return;
        }

        Hero hero = heroes.getCharacterManager().getHero(player.getPlayerEntity());

        switch (Action.valueOf(action.asString().toUpperCase())) {
            case ADD:
                hero.addExp(quantity.asDouble(), hclass.getHeroClass(), player.getLocation());
                break;
            case REMOVE:
                hero.setExperience(hclass.getHeroClass(),
                        hero.getExperience(hclass.getHeroClass()) - quantity.asDouble());
                break;
            case SET:
                hero.setExperience(hclass.getHeroClass(), quantity.asDouble());
                break;
        }
    }
}
