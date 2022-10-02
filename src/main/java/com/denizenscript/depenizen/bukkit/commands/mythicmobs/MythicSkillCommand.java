package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import io.lumine.mythic.core.skills.variables.Variable;
import io.lumine.mythic.core.skills.variables.VariableType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class MythicSkillCommand extends AbstractCommand {

    public MythicSkillCommand() {
        setName("mythicskill");
        setSyntax("mythicskill [<skillname>] [<location>|.../<entity>|...] (power:<#.#>) (casters:<entity>|...) (trigger:<entity>) (origin:<location>) (parameters:<map>)");
        setRequiredArguments(2, 6);
    }

    // <--[command]
    // @Name MythicSkill
    // @Syntax mythicskill [<skillname>] [<location>|.../<entity>|...] (power:<#.#>) (casters:<entity>|...) (trigger:<entity>) (origin:<location>) (parameters:<map>)
    // @Group Depenizen
    // @Plugin Depenizen, MythicMobs
    // @Required 2
    // @Maximum 6
    // @Short Cast a MythicMob skill from an entity.
    //
    // @Description
    // This will cast a MythicMob skill from any specified entity.
    // Optionally, you can have multiple entities cast the skill.
    // You may also specify multiple EntityTag(s) or LocationTag(s) for targets.
    // You may not have both the Entity and Location targets, one or the other.
    // The MythicMob configuration must be configured to account for this.
    // You may also specify an EntityTag as the trigger of the skill, an
    // origin of the skill, and a Map of skill parameters to be set
    // for the skill.
    //
    // @Usage
    // Used to make the player use the MythicMob skill frostbolt on their target.
    // - mythicskill frostbolt <player.target> casters:<player>
    //
    // -->


    @Override
    public void addCustomTabCompletions(TabCompletionsBuilder tab) {
        tab.add((Set<String>) MythicMobsBridge.getSkillNames());
    }

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgLinear ElementTag skill,
                                   @ArgDefaultNull List<EntityTag> entity_targets,
                                   @ArgDefaultNull List<LocationTag> location_targets,
                                   @ArgPrefixed @ArgName("power") @ArgDefaultNull ElementTag power,
                                   @ArgPrefixed @ArgName("casters") @ArgDefaultNull List<EntityTag> casters,
                                   @ArgPrefixed @ArgName("trigger") @ArgDefaultNull EntityTag trigger,
                                   @ArgPrefixed @ArgName("origin") @ArgDefaultNull LocationTag origin,
                                   @ArgPrefixed @ArgName("parameters") @ArgDefaultNull MapTag parameters) {
        if (scriptEntry.hasObject("entityTargets") && scriptEntry.hasObject("locationTargets")) {
            Debug.echoError("Cannot have both entity and location targets.");
            return;
        }

        ArrayList<Entity> entityTargets = null;
        ArrayList<Location> locationTargets = null;
        if (entity_targets != null) {
            entityTargets = new ArrayList<>();
            for (EntityTag entity : entity_targets) {
                entityTargets.add(entity.getBukkitEntity());
            }
        }
        else {
            locationTargets = new ArrayList<>(location_targets);
        }

        for (EntityTag caster : casters) {
            MythicMobsBridge.getAPI().castSkill(caster.getBukkitEntity(), skill.asString(), trigger != null ? trigger.entity : caster.getBukkitEntity(), origin, entityTargets, locationTargets, power.asFloat() , (metadata) -> {
                if (parameters == null) {
                    return;
                }
                metadata.getVariables().put("variable_name", Variable.ofType(VariableType.STRING, ""));
                Map<String, String> parameterMap = new HashMap<>();
                for (Map.Entry<StringHolder, ObjectTag> entry : parameters.map.entrySet()) {
                    parameterMap.put(entry.getKey().toString(), entry.getValue().toString());
                }
                metadata.getParameters().putAll(parameterMap);
            });
        }
    }
}
