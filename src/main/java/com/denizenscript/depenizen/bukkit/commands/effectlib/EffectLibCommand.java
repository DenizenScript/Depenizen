package com.denizenscript.depenizen.bukkit.commands.effectlib;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bridges.EffectLibBridge;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.BleedEffect;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.Location;

public class EffectLibCommand extends AbstractCommand {

    public EffectLibCommand() {
        setName("effectlib");
        setSyntax("effectlib (type:<effect name>) (duration:<duration>) (target:<entity>/location:<location>)");
        setRequiredArguments(1, 3);
    }

    // <--[command]
    // @Name effectlib
    // @Syntax effectlib (type:<effect name>) (duration:<duration>) (target:<entity>/location:<location>)
    // @Group Depenizen
    // @Plugin Depenizen, EffectLib
    // @Required 1
    // @Maximum 3
    // @Short Show custom effects using EffectLib
    //
    // @Description
    // Use to show custom effects in a easier way using the config file in EffectLib.
    // The effect names comes from the config file.
    // Specify a location instead of a target to show the effect at a location instead.
    //
    // Note that most users should instead use <@link command playeffect>.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to show a effect on the attached player in queue.
    // - effectlib bleed duration:10s
    //
    // @Usage
    // Use to show a effect on a target entity.
    // - effectlib atom target:<player.target> duration:10s
    //
    // @Usage
    // Use to show effect at a position of the player.
    // - effectlib type:atom duration:10s location:<player.location>
    //
    // -->

    private enum Action {
        BLEED, ARC, ATOM
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(EntityTag.class));
            }
            else if (!scriptEntry.hasObject("duration")
                    && arg.matchesPrefix("duration")) {
                scriptEntry.addObject("duration", arg.asType(DurationTag.class));
            }
            else if (!scriptEntry.hasObject("location")
                    && arg.matchesPrefix("location")) {
                scriptEntry.addObject("location", arg.asType(LocationTag.class));
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
        EntityTag target = scriptEntry.getObjectTag("target");
        Action action = (Action) scriptEntry.getObject("action");
        DurationTag duration = scriptEntry.getObjectTag("duration");
        LocationTag location = scriptEntry.getObjectTag("location");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), (target != null ? target.debug() : "")
                    + (action != null ? action.toString() : "")
                    + (duration != null ? duration.debug() : "")
                    + (location != null ? location.debug() : ""));
        }
        if (target == null && location == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Target not found!");
            return;
        }
        if (action == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Effect type not specified!");
            return;
        }
        if (duration == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Duration not specified!");
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
                effect.callback = () -> scriptEntry.setFinished(true);
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
                effect.callback = () -> scriptEntry.setFinished(true);
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
                effect.callback = () -> scriptEntry.setFinished(true);
                effect.iterations = ticks;
                effect.start();
                return;
            }
            default: {
                Debug.echoError(scriptEntry.getResidingQueue(), "Effect type not found!");
            }
        }
    }
}
