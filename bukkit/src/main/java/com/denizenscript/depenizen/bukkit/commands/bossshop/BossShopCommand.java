package com.denizenscript.depenizen.bukkit.commands.bossshop;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.BossShopSupport;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShop;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class BossShopCommand extends AbstractCommand {
    // <--[command]
    // @Name bossshop
    // @Syntax bossshop [<shop name>] (target:<player>)
    // @Group Depenizen
    // @Plugin DepenizenBukkit, BossShop
    // @Required 1
    // @Stable untested
    // @Short Opens a BossShop inventory for a player.
    // @Author Mwthorn

    // @Description
    // Use to open up a BossShop inventory for either
    // the player attached to the queue or specified target.
    // Useful for rewarding players using the BossShop plugin.
    // Shops are made with the BossShop system.
    //
    // @Tags
    // <in@inventory.is_bossshop>

    // @Usage
    // Use to open a bossshop inventory for the player in the queue as a Player.
    // - bossshop "MyShop"

    // @Usage
    // Use to open a bossshop inventory for the target.
    // - bossshop "MyShop" target:<player>

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(dPlayer.class));
            }

            else if (!scriptEntry.hasObject("shop")) {
                scriptEntry.addObject("shop", arg.asElement());
            }

            else {
                arg.reportUnhandled();
            }

        }

        if (!scriptEntry.hasObject("shop")) {
            throw new InvalidArgumentsException("Shop not specified!");
        }

        if (!scriptEntry.hasObject("target")) {
            if (((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
                scriptEntry.addObject("target", ((BukkitScriptEntryData) scriptEntry.entryData).getPlayer());
            }
            else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        dPlayer target = scriptEntry.getdObject("target");
        Element dshop = scriptEntry.getdObject("shop");

        // Report to dB
        dB.report(scriptEntry, getName(),
                (target != null ? target.debug() : "")
                        + (dshop != null ? dshop.debug() : ""));

        if (target == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Target not found!");
            return;
        }

        if (dshop == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Shop not Specified!");
            return;
        }

        BossShop bs = Support.getPlugin(BossShopSupport.class);
        BSShop shop = bs.getAPI().getShop(dshop.asString());
        if (shop == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Shop not found!");
            return;
        }

        bs.getAPI().openShop(target.getPlayerEntity(), shop);

    }
}
