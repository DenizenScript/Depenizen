package com.denizenscript.depenizen.bukkit.commands.bossshop;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bridges.BossShopBridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShop;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class BossShopCommand extends AbstractCommand {

    public BossShopCommand() {
        setName("bossshop");
        setSyntax("bossshop [<shop name>]");
        setRequiredArguments(1, 1);
    }

    // <--[command]
    // @Name bossshop
    // @Syntax bossshop [<shop name>]
    // @Group Depenizen
    // @Plugin Depenizen, BossShopPro
    // @Required 1
    // @Maximum 1
    // @Short Opens a BossShop inventory for a player.
    //
    // @Description
    // Use to open up a BossShop inventory for the linked player.
    // Useful for rewarding players using the BossShop plugin.
    // Shops are made with the BossShop system.
    //
    // @Tags
    // <InventoryTag.is_bossshop>
    //
    // @Usage
    // Use to open a bossshop inventory for the linked player.
    // - bossshop MyShop
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(PlayerTag.class));
                Debug.echoError("Don't use 'target:' for 'bossshop' command. Just use 'player:'.");
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
            if (Utilities.entryHasPlayer(scriptEntry)) {
                scriptEntry.addObject("target", Utilities.getEntryPlayer(scriptEntry));
            }
            else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        PlayerTag target = scriptEntry.getObjectTag("target");
        ElementTag dshop = scriptEntry.getObjectTag("shop");
        Debug.report(scriptEntry, getName(), target, dshop);
        if (target == null) {
            Debug.echoError(scriptEntry, "Target not found!");
            return;
        }
        if (dshop == null) {
            Debug.echoError(scriptEntry, "Shop not Specified!");
            return;
        }
        BossShop bs = (BossShop) BossShopBridge.instance.plugin;
        BSShop shop = bs.getAPI().getShop(dshop.asString());
        if (shop == null) {
            Debug.echoError(scriptEntry, "Shop not found!");
            return;
        }
        bs.getAPI().openShop(target.getPlayerEntity(), shop);
    }
}
