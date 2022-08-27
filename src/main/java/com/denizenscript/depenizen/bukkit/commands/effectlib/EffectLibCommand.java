package com.denizenscript.depenizen.bukkit.commands.effectlib;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.denizencore.utilities.debugging.Warning;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.EffectLibBridge;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.DynamicLocation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.HashMap;
import java.util.Map;

public class EffectLibCommand extends AbstractCommand implements Holdable {

    public EffectLibCommand() {
        setName("effectlib");
        setSyntax("effectlib [type:<effect>] (origin:<entity>/<location>) (target:<entity>/<location>) (for:<player>) (effect_data:<map>)");
        setRequiredArguments(1, 5);
        autoCompile();
    }

    // <--[command]
    // @Name effectlib
    // @Syntax effectlib [type:<effect>] (origin:<entity>/<location>) (target:<entity>/<location>) (for:<player>) (effect_data:<map>)
    // @Group Depenizen
    // @Plugin Depenizen, EffectLib
    // @Required 1
    // @Maximum 5
    // @Short Show custom effects using EffectLib
    // @Description
    // Displays EffectLib effects.
    //
    // Specify which effect to display using the 'type:' argument, note that effect names are CASE-SENSITIVE.
    //
    // The origin is the entity/location to play the effect at. If an entity is specified, the effect will follow it.
    // Defaults to the linked player if unspecified.
    //
    // Some effects (such as the 'Line' effect) require an origin and a target, an effect's target can be a location or an entity.
    // If an entity is specified, the effect will follow it.
    //
    // You can optionally use the 'for:' argument to show the effect to one player only.
    //
    // EffectLib effects can take additional data to change how the effect looks/works, you can specify that data in the form of a MapTag using the 'effect_data:' argument.
    // See EffectLib's docs for further information on the available options for each effect: <@link url https://reference.elmakers.com/#effectlib>
    //
    // Note that this should only be used for displaying EffectLib's advanced effect shapes. For more general particle usage, use <@link command playeffect>.
    //
    // The effectlib command is ~waitable. Refer to <@link language ~waitable>.
    // When ~waited for, the command will wait until the effect is done playing.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to show a helix of end rod sparks below the linked player for 5 seconds.
    // - effectlib type:Helix effect_data:[duration=<duration[5s].in_milliseconds>;particle=end_rod;offset=0,-1.5,0]
    //
    // @Usage
    // Use to show a line of electric sparks between 2 entities for 1 minute.
    // - effectlib type:Line origin:<[originEntity]> target:<[targetEntity]> effect_data:[duration=<duration[1m].in_milliseconds>;particle=electric_spark]
    //
    // @Usage
    // Use to show an atom effect at a location, and narrate a message after it's done playing.
    // - ~effectlib type:Atom origin:<[location]>
    // - narrate "It's done!"
    //
    // -->

    public static Warning durationArgument = new SlowWarning("effectlibCommandDuration", "The 'duration:<duration>' argument for the 'effectlib' command is deprecated in favor of the 'duration' key in the 'effect_data' map, refer to the meta docs for more information.");
    public static Warning oldTargetArguments = new SlowWarning("effectlibCommandOldTarget", "The 'target:<entity>'/'location:<location>' arguments for the 'effectlib' command are deprecated in favor of the 'origin:<entity>/<location>' argument.");

    public static HashMap<String, String> nameCasings = new HashMap<>();
    public static void registerNameCasing(String s) {
        nameCasings.put(CoreUtilities.toLowerCase(s), s);
    }
    static { // Effect names that have more than 1 capital letter
        registerNameCasing("AnimatedBall");
        registerNameCasing("BigBang");
        registerNameCasing("ColoredImage");
        registerNameCasing("DiscoBall");
        registerNameCasing("SkyRocket");
    }

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgPrefixed @ArgName("type") String effectInput,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("origin") ObjectTag originInput,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("target") ObjectTag targetInput,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("for") PlayerTag forPlayer,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("effect_data") MapTag effectData,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("duration") DurationTag duration,
                                   @ArgDefaultNull @ArgPrefixed @ArgName("location") LocationTag oldLocation) {
        if (originInput == null && oldLocation != null) {
            oldTargetArguments.warn(scriptEntry);
            originInput = oldLocation;
            targetInput = null;
        }
        if (originInput == null && targetInput != null && targetInput.shouldBeType(EntityTag.class)) {
            oldTargetArguments.warn(scriptEntry);
            originInput = targetInput;
            targetInput = null;
        }
        if (originInput == null && Utilities.entryHasPlayer(scriptEntry)) {
            originInput = Utilities.getEntryPlayer(scriptEntry);
        }
        if (originInput == null) {
            Debug.echoError(scriptEntry, "Must specify an origin location or entity!");
            return;
        }
        // EffectLib is case-sensitive, so correct for casing as best as possible
        if (Character.isLowerCase(effectInput.charAt(0))) {
            effectInput = Character.toUpperCase(effectInput.charAt(0)) + effectInput.substring(1);
            String fixed = nameCasings.get(CoreUtilities.toLowerCase(effectInput));
            if (fixed != null) {
                effectInput = fixed;
            }
        }
        Effect effect;
        if (effectData == null) {
            effect = EffectLibBridge.instance.effectManager.getEffectByClassName(effectInput);
            if (effect != null) {
                effect.setDynamicOrigin(getEffectLibLocationFrom(originInput, scriptEntry.context));
                effect.setDynamicTarget(getEffectLibLocationFrom(targetInput, scriptEntry.context));
                effect.setTargetPlayer(forPlayer != null ? forPlayer.getPlayerEntity() : null);
            }
        }
        else {
            ConfigurationSection effectLibData = new MemoryConfiguration();
            for (Map.Entry<StringHolder, ObjectTag> entry : effectData.map.entrySet()) {
                effectLibData.set(entry.getKey().str, CoreUtilities.objectTagToJavaForm(entry.getValue(), false, true));
            }
            effect = EffectLibBridge.instance.effectManager.getEffect(
                    effectInput,
                    effectLibData,
                    getEffectLibLocationFrom(originInput, scriptEntry.context),
                    getEffectLibLocationFrom(targetInput, scriptEntry.context),
                    null,
                    forPlayer != null ? forPlayer.getPlayerEntity() : null);
        }
        if (effect == null) {
            Debug.echoError(scriptEntry, "Invalid effect specified: " + effectInput);
            return;
        }
        effect.callback = () -> scriptEntry.setFinished(true);
        if (duration != null) {
            durationArgument.warn(scriptEntry);
            effect.iterations = duration.getTicksAsInt();
        }
        effect.start();
    }

    public static DynamicLocation getEffectLibLocationFrom(ObjectTag object, TagContext context) {
        if (object == null) {
            return null;
        }
        if (object.shouldBeType(LocationTag.class)) {
           return new DynamicLocation(object.asType(LocationTag.class, context));
        }
        else {
            EntityTag entity = object.asType(EntityTag.class, context);
            if (entity == null) {
                Debug.echoError(context, "Invalid input specified '" + object + "': must be a valid location or entity.");
                return null;
            }
             return new DynamicLocation(entity.getBukkitEntity());
        }
    }
}
