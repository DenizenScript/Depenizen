package com.denizenscript.depenizen.bukkit.commands.effectlib;

import com.denizenscript.depenizen.bukkit.bridges.EffectLibBridge;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.BleedEffect;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.Utilities;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Duration;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.Location;

public class EffectLibCommand extends AbstractCommand {
    // <--[command]
    // @Name effectlib
    // @Syntax effectlib (type:<effect name>) (duration:<duration>) (target:<entity>)
    // @Group Depenizen
    // @Plugin Depenizen, EffectLib
    // @Required 1
    // @Short Show custom effects using EffectLib

    // @Description
    // Use to show custom effects in a easier way using the config file in EffectLib.
    // The effect names comes from the config file.
    // Specify a location instead of a target to show the effect at a location instead.

    // @Tags
    // None

    // @Usage
    // Use to show a effect on the attached player in queue.
    // - effeclib bleed duration:10s

    // @Usage
    // Use to show a effect on a target entity.
    // - effeclib atom target:<player.target> duration:10s

    // @Usage
    // Use to show effect at a position of the player.
    // - effeclib type:atom duration:10s location:<player.location>

    // -->

    private enum Action {
        BLEED, ARC, ATOM
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(dEntity.class));
            }

            else if (!scriptEntry.hasObject("duration")
                    && arg.matchesPrefix("duration")) {
                scriptEntry.addObject("duration", arg.asType(Duration.class));
            }

            else if (!scriptEntry.hasObject("location")
                    && arg.matchesPrefix("location")) {
                scriptEntry.addObject("location", arg.asElement());
            }

            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));
            }

            else {
                arg.reportUnhandled();
            }

        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Effect not specified!");
        }

        if (!scriptEntry.hasObject("duration")) {
            throw new InvalidArgumentsException("Duration not specified!");
        }

        if (!scriptEntry.hasObject("target")) {
            if (Utilities.entryHasPlayer(scriptEntry)) {
                scriptEntry.addObject("target", Utilities.getEntryPlayer(scriptEntry).getDenizenEntity());
            }
        }

    }

    @Override
    public void execute(final ScriptEntry scriptEntry) {

        dEntity target = (dEntity) scriptEntry.getObject("target");
        Action action = (Action) scriptEntry.getObject("action");
        Duration duration = (Duration) scriptEntry.getObject("duration");
        dLocation location = (dLocation) scriptEntry.getObject("location");

        // Report to dB
        dB.report(scriptEntry, getName(), (target != null ? target.debug() : "")
                + (action != null ? action.toString() : "")
                + (duration != null ? duration.debug() : "")
                + (location != null ? location.debug() : ""));

        if (target == null && location == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Target not found!");
            return;
        }
        if (action == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Effect type not specified!");
            return;
        }
        if (duration == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Duration not specified!");
            return;
        }

        int ticks = duration.getTicksAsInt();
        EffectManager effectManager = new EffectManager(EffectLibBridge.instance.plugin);

        // TODO: Find a better way to handle all effects and add custom ones as well
        switch (action) {

            case BLEED: {
                BleedEffect effect = new BleedEffect(effectManager);
                if (location == null) {
                    effect.setEntity(target.getBukkitEntity());
                }
                else {
                    effect.setLocation(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()));
                }
                // Add a callback to the effect
                effect.callback = new Runnable() {

                    @Override
                    public void run() {
                        scriptEntry.setFinished(true);
                    }

                };

                effect.iterations = ticks;
                effect.start();
                return;
            }

            case ARC: {
                ArcEffect effect = new ArcEffect(effectManager);
                if (location == null) {
                    effect.setEntity(target.getBukkitEntity());
                }
                else {
                    effect.setLocation(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()));
                }
                // Add a callback to the effect
                effect.callback = new Runnable() {

                    @Override
                    public void run() {
                        scriptEntry.setFinished(true);
                    }

                };

                effect.iterations = ticks;
                effect.start();
                return;
            }

            case ATOM: {
                AtomEffect effect = new AtomEffect(effectManager);
                if (location == null) {
                    effect.setEntity(target.getBukkitEntity());
                }
                else {
                    effect.setLocation(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()));
                }
                // Add a callback to the effect
                effect.callback = new Runnable() {

                    @Override
                    public void run() {
                        scriptEntry.setFinished(true);
                    }

                };

                effect.iterations = ticks;
                effect.start();
                return;
            }

            default: {
                dB.echoError(scriptEntry.getResidingQueue(), "Effect type not found!");
            }

        }

    }
}
