package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.events.sentinel.SentinelAttackScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.containers.core.ProcedureScriptContainer;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.events.sentinel.SentinelNoMoreTargetsScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.sentinel.SentinelNPCProperties;
import org.bukkit.entity.LivingEntity;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelPlugin;
import org.mcmonkey.sentinel.SentinelUtilities;

import java.util.List;

public class SentinelBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new SentinelAttackScriptEvent());
        ScriptEvent.registerScriptEvent(new SentinelNoMoreTargetsScriptEvent());
        SentinelPlugin.instance.registerIntegration(new DenizenSentinelTargets());
        PropertyParser.registerProperty(SentinelNPCProperties.class, NPCTag.class);
    }

    public class DenizenSentinelTargets extends SentinelIntegration {

        @Override
        public String getTargetHelp() {
            return "denizen_proc:PROCEDURE_SCRIPT_NAME, held_denizen_item:DENIZEN_ITEM_NAME";
        }

        @Override
        public String[] getTargetPrefixes() {
            return new String[] {"denizen_proc", "held_denizen_item"};
        }

        @Override
        public boolean isTarget(LivingEntity ent, String prefix, String value) {
            try {
                if (prefix.equals("held_denizen_item") && ent.getEquipment() != null) {
                    if (SentinelUtilities.regexFor(value).matcher(new ItemTag(SentinelUtilities.getHeldItem(ent)).identifySimple().replace("i@", "")).matches()) {
                        return true;
                    }
                }
                if (prefix.equals("denizen_proc")) {
                    String context = null;
                    int colon = value.indexOf(':');
                    if (colon > 0) {
                        context = value.substring(colon + 1);
                        value = value.substring(0, colon);
                    }
                    ScriptTag script = ScriptTag.valueOf(value, CoreUtilities.basicContext);
                    if (script == null) {
                        Debug.echoError("Invalid procedure script name '" + value + "' (non-existent) in a Sentinel NPC target.");
                        return false;
                    }
                    if (!(script.getContainer() instanceof ProcedureScriptContainer)) {
                        Debug.echoError("Invalid procedure script name '" + value + "' (not a procedure) in a Sentinel NPC target.");
                        return false;
                    }
                    List<ScriptEntry> entries = script.getContainer().getBaseEntries(DenizenCore.getImplementation().getEmptyScriptEntryData());
                    if (entries.isEmpty()) {
                        return false;
                    }
                    InstantQueue queue = InstantQueue.getQueue(ScriptQueue.getNextId(script.getContainer().getName()));
                    queue.addEntries(entries);
                    String def_name = "entity";
                    String context_name = "context";
                    if (script.getContainer().getContents().contains("definitions")) {
                        List<String> definition_names = CoreUtilities.split(script.getContainer().getString("definitions"), '|');
                        if (definition_names.size() >= 1) {
                            def_name = definition_names.get(0);
                        }
                        if (definition_names.size() >= 2) {
                            context_name = definition_names.get(1);
                        }
                    }
                    queue.addDefinition(def_name, new EntityTag(ent).getDenizenObject());
                    if (context != null) {
                        queue.addDefinition(context_name, new ElementTag(context));
                    }
                    queue.start();
                    if (queue.determinations != null && queue.determinations.size() > 0) {
                        return CoreUtilities.equalsIgnoreCase(queue.determinations.get(0), "true");
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
}
