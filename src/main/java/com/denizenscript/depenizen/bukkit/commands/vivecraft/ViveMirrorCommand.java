package com.denizenscript.depenizen.bukkit.commands.vivecraft;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.*;
import com.denizenscript.depenizen.bukkit.bridges.ViveCraftBridge;
import com.denizenscript.depenizen.bukkit.objects.vivecraft.ViveCraftPlayerTag;
import org.bukkit.entity.Player;
import org.vivecraft.VSE;
import org.vivecraft.VivePlayer;

import java.util.Collections;
import java.util.List;

public class ViveMirrorCommand extends AbstractCommand {

    public ViveMirrorCommand() {
        setName("vivemirror");
        setSyntax("vivemirror [<npc>] [mirror:<vivecraftplayer>] (targets:{player}/<player>|...)");
        setRequiredArguments(2, 3);
        autoCompile();
    }

    // <--[command]
    // @Name ViveMirror
    // @Syntax vivemirror [<npc>] [mirror:<vivecraftplayer>] (targets:{player}/<player>|...)
    // @Required 2
    // @Maximum 3
    // @Short Mirrors a ViveCraftPlayers pose to the npc, once.
    // @Group Depenizen
    // @plugin Depenizen, ViveCraft
    //
    // @Tags
    // None
    //
    // @Description
    // Mirrors a ViveCraftPlayers pose to the npc, once.
    //
    // Ideally should run every tick.
    //
    // Specify a vivecraftplayer that will be mirrored to the NPC.
    // Optionally, specify a list of targets to show the NPCs pose to (targets must be in VR to see the effect).
    //
    // @Usage
    // # Use to mirror the current players pose.
    // - vivemirror <npc> mirror:<player.vivecraft>
    //
    // @Usage
    // # Use to show your dancing skills to other players.
    // - vivemirror <npc> mirror:<player.vivecraft> targets:<server.online_players>
    //
    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgLinear @ArgName("npc") @ArgRaw NPCTag npc,
                                   @ArgPrefixed @ArgName("mirror") ViveCraftPlayerTag mirror,
                                   @ArgPrefixed @ArgName("targets") @ArgDefaultNull @ArgSubType(PlayerTag.class) List<PlayerTag> targets) {
        if (!(npc.getEntity() instanceof Player)) {
            throw new InvalidArgumentsRuntimeException("NPC must be a PLAYER type NPC.");
        }
        if (targets == null) {
            if (!Utilities.entryHasPlayer(scriptEntry)) {
                throw new InvalidArgumentsRuntimeException("Missing player input.");
            }
            targets = Collections.singletonList(Utilities.getEntryPlayer(scriptEntry));
        }
        VivePlayer vp = new VivePlayer((Player) npc.getLivingEntity());
        VivePlayer copy = mirror.getVivePlayer();
        vp.worldScale = copy.worldScale;
        vp.heightScale = copy.heightScale;
        vp.hmdData = copy.hmdData;
        vp.controller0data = copy.controller0data;
        vp.controller1data = copy.controller1data;
        for (PlayerTag target : targets) {
            if (ViveCraftBridge.isViveCraftPlayer(target.getPlayerEntity())) {
                target.getPlayerEntity().sendPluginMessage(VSE.getPlugin(VSE.class), VSE.CHANNEL, vp.getUberPacket());
            }
        }
    }
}
