package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.*;
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
        setSyntax("mythicskill [<skillname>] [<location>|.../<entity>|...] (power:<#.#>) (casters:<entity>|...) (trigger:<entity>) (origin:<location>) (parameters:<map>) (variables:<map>) (async)");
        setRequiredArguments(2, 9);
        autoCompile();
    }

    // <--[command]
    // @Name MythicSkill
    // @Syntax mythicskill [<skillname>] [<location>|.../<entity>|...] (power:<#.#>) (casters:<entity>|...) (trigger:<entity>) (origin:<location>) (parameters:<map>) (variables:<map>) (async)
    // @Group Depenizen
    // @Plugin Depenizen, MythicMobs
    // @Required 2
    // @Maximum 9
    // @Short Cast a MythicMob skill from an entity.
    //
    // @Description
    // This will cast a MythicMobs skill from any specified entity.
    // Optionally, you can have multiple entities cast the skill.
    // You may also specify multiple EntityTag(s) or LocationTag(s) for targets.
    // You may not have both the Entity and Location targets, one or the other.
    // The MythicMob configuration must be configured to account for this.
    // You may also specify an EntityTag as the trigger of the skill, an
    // origin of the skill, a Map of skill parameters to be set
    // for the skill, and a Map of skill variables to be set for the skill.
    // The value of the variables can be obtained from MythicMobs through
    // the placeholder <skill.var.x>, where x is the variable ID (the key of the map).
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
                                   @ArgLinear @ArgName("skill") ElementTag skill,
                                   @ArgLinear @ArgName("targets") ListTag targets,
                                   @ArgPrefixed @ArgName("power") @ArgDefaultText("1") ElementTag power,
                                   @ArgPrefixed @ArgName("casters") @ArgDefaultNull ListTag casters,
                                   @ArgPrefixed @ArgName("trigger") @ArgDefaultNull EntityTag trigger,
                                   @ArgPrefixed @ArgName("origin") @ArgDefaultNull LocationTag origin,
                                   @ArgPrefixed @ArgName("parameters") @ArgDefaultNull MapTag parameters,
                                   @ArgPrefixed @ArgName("variables") @ArgDefaultNull MapTag variables,
                                   @ArgLinear @ArgName("async") @ArgDefaultText("false") boolean async) {
        List<Entity> entityTargets = new ArrayList<>();
        List<Location> locationTargets = new ArrayList<>();
        for (ObjectTag object : targets.objectForms) {
            if (object.canBeType(EntityTag.class)) {
                entityTargets.add(((EntityTag) object).getBukkitEntity());
            }
            else if (object.canBeType(LocationTag.class)) {
                locationTargets.add((Location) object);
            }
        }
        if (!entityTargets.isEmpty() && !locationTargets.isEmpty()) {
            Debug.echoError("Cannot have both entity and location targets.");
            return;
        }

        for (EntityTag caster : casters != null ? casters.filter(EntityTag.class, scriptEntry) : Utilities.entryDefaultEntityList(scriptEntry, true)) {
            MythicMobsBridge.getAPI().castSkill(caster.getBukkitEntity(), skill.asString(), trigger != null ? trigger.entity : caster.getBukkitEntity(), origin, entityTargets, locationTargets, power.asFloat() , (metadata) -> {
                if (parameters != null) {
                    Map<String, String> parameterMap = new HashMap<>();
                    for (Map.Entry<StringHolder, ObjectTag> entry : parameters.map.entrySet()) {
                        parameterMap.put(entry.getKey().toString(), entry.getValue().toString());
                    }
                    metadata.getParameters().putAll(parameterMap);
                }
                if (variables != null) {
                    Map<String, Variable> variableMap = new HashMap<>();
                    for (Map.Entry<StringHolder, ObjectTag> entry : variables.map.entrySet()) {
                        variableMap.put(entry.getKey().toString(), Variable.ofType(VariableType.STRING, entry.getValue().toString()));
                    }
                    metadata.getVariables().putAll(variableMap);
                }
                metadata.setIsAsync(async);
            });
        }
    }
}
