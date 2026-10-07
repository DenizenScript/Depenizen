package com.denizenscript.depenizen.bukkit.commands.luckperms;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.*;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.*;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsGroupTag;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LuckPermsCommand extends AbstractCommand {

    public LuckPermsCommand() {
        setName("luckperms");
        setSyntax("luckperms ({set}/unset) [users:<player>|.../groups:<group>|...] [permission] (state:{true}/false) (duration:<duration>) (contexts:<map>)");
        setRequiredArguments(2, 6);
        autoCompile();
    }

    // <--[command]
    // @Name mcMMO
    // @Syntax luckperms ({set}/unset) [users:<player>|.../groups:<group>|...] [permission] (state:{true}/false) (duration:<duration>) (contexts:<map>)
    // @Group Depenizen
    // @Plugin Depenizen, mcMMO
    // @Required 2
    // @Maximum 6
    // @Short Edits LuckPerms permissions.
    //
    // @Description
    // This command allows you to add or unset permission nodes from players and LuckPerms groups.
    //
    // The 'duration' argument specifies how long a player or group will have the specified permission
    // before it is removed. Not using this argument will result in the permission staying until manually removed.
    //
    // The 'contexts' argument sets the contexts in which the permission and state specified is active.
    // Format is in MapTag format: 'type=value'.
    // The 'value' accepts a ListTag for multiple entries of the same key.
    //
    // @Tags
    // <PlayerTag.has_permission[<permission.node>]>
    // <PlayerTag.luckperms_permission_expiration[<permission.node>]>
    // <LuckPermsGroupTag.has_permission[<permission.node>]>
    // <LuckPermsGroupTag.permission_expiration[<permission.node>]>
    //
    // @Usage
    // Use to give two players the 'dscript.help' permission.
    // - luckperms set users:<[player]>|<[other_player]> dscript.help state:true
    //
    // @Usage
    // Use to give a player the 'dscript.warp' permission for 5 minutes in the main world.
    // - luckperms users:<player> dscript.warp duration:5m contexts:[world=world]
    //
    // @Usage
    // Use to disallow two groups access to the 'dscript.spawn' permission in the nether and end.
    // - luckperms groups:<[group]>|<[other_group]> dscript.spawn state:false contexts:[dimension_type=the_nether|the_end]
    //
    // @Usage
    // Use to unset the 'dscript.ban' permission back to the default for a player when in survival mode on the hub server.
    // - luckperms unset users:<player> dscript.ban contexts:[gamemode=survival;server=hub]
    //
    // -->

    public enum Action {SET, UNSET}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("action") @ArgDefaultText("set") Action action,
                                   @ArgName("users") @ArgPrefixed @ArgDefaultNull @ArgSubType(PlayerTag.class) List<PlayerTag> players,
                                   @ArgName("groups") @ArgPrefixed @ArgDefaultNull @ArgSubType(LuckPermsGroupTag.class) List<LuckPermsGroupTag> groups,
                                   @ArgName("permission") @ArgLinear String permission,
                                   @ArgName("state") @ArgPrefixed @ArgDefaultText("true") boolean state,
                                   @ArgName("duration") @ArgPrefixed @ArgDefaultNull DurationTag duration,
                                   @ArgName("contexts") @ArgPrefixed @ArgDefaultNull MapTag contexts) {
        if (players == null && groups == null) {
            throw new InvalidArgumentsRuntimeException("Must specify either players or groups!");
        }
        if (players != null && groups != null) {
            throw new InvalidArgumentsRuntimeException("Cannot specify both players and groups!");
        }
        NodeBuilder<?, ?> nodeBuilder = Node.builder(permission).value(state);
        if (duration != null) {
            nodeBuilder = nodeBuilder.expiry((long) duration.getSeconds(), TimeUnit.SECONDS);
        }
        if (contexts != null) {
            for (Map.Entry<StringHolder, ObjectTag> entry : contexts.entrySet()) {
                for (String value : entry.getValue().asType(ListTag.class, scriptEntry.context)) {
                    nodeBuilder = nodeBuilder.withContext(entry.getKey().low, value);
                }
            }
        }
        Node node = nodeBuilder.build();
        switch (action) {
            case SET -> {
                if (players != null) {
                    for (PlayerTag player : players) {
                        User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
                        if (user == null) {
                            Debug.echoError("User " + player.getUUID() + " does not exist, have they joined the server before?");
                            continue;
                        }
                        user.data().add(node);
                        LuckPermsBridge.luckPermsInstance.getUserManager().saveUser(user);
                    }
                }
                else {
                    for (LuckPermsGroupTag group : groups) {
                        group.getGroup().data().add(node);
                        LuckPermsBridge.luckPermsInstance.getGroupManager().saveGroup(group.getGroup());
                    }
                }
            }
            case UNSET -> {
                if (players != null) {
                    for (PlayerTag player : players) {
                        User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
                        if (user == null) {
                            Debug.echoError("User " + player.getUUID() + " does not exist, have they joined the server before?");
                            continue;
                        }
                        user.data().remove(node);
                        LuckPermsBridge.luckPermsInstance.getUserManager().saveUser(user);
                    }
                }
                else {
                    for (LuckPermsGroupTag group : groups) {
                        group.getGroup().data().remove(node);
                        LuckPermsBridge.luckPermsInstance.getGroupManager().saveGroup(group.getGroup());
                    }
                }
            }
        }
    }
}
