package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.sentinel.SentinelAttackScriptEvent;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.DenizenCore;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.scripts.ScriptBuilder;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.core.DetermineCommand;
import net.aufdemrand.denizencore.scripts.containers.core.ProcedureScriptContainer;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.entity.LivingEntity;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelPlugin;
import org.mcmonkey.sentinel.SentinelUtilities;

import java.util.List;

public class SentinelSupport extends Support {

    public SentinelSupport() {
        registerScriptEvents(new SentinelAttackScriptEvent());
        SentinelPlugin.integrations.add(new HeldScriptItemTarget());
        SentinelPlugin.integrations.add(new DenizenProcedureTarget());
    }

    public class HeldScriptItemTarget extends SentinelIntegration {

        @Override
        public String getTargetHelp() {
            return "held_denizen_item:DENIZEN_ITEM_NAME";
        }

        @Override
        public boolean isTarget(LivingEntity ent, String text) {
            try {
                if (text.startsWith("held_denizen_item:") && ent.getEquipment() != null) {
                    String targetItemRegex = text.substring("held_denizen_item:".length());
                    if (SentinelUtilities.regexFor(targetItemRegex).matcher(new dItem(SentinelUtilities.getHeldItem(ent)).identifySimple().replace("i@", "")).matches()) {
                        return true;
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public class DenizenProcedureTarget extends SentinelIntegration {

        @Override
        public String getTargetHelp() {
            return "denizen_proc:PROCEDURE_SCRIPT_NAME";
        }

        @Override
        public boolean isTarget(LivingEntity ent, String text) {
            try {
                if (text.startsWith("denizen_proc:") && ent.getEquipment() != null) {
                    String procName = text.substring("denizen_proc:".length());
                    dScript script = dScript.valueOf(procName);
                    if (script == null) {
                        dB.echoError("Invalid procedure script name '" + procName + "' (non-existent) in a Sentinel NPC target.");
                        return false;
                    }
                    if (!(script.getContainer() instanceof ProcedureScriptContainer)) {
                        dB.echoError("Invalid procedure script name '" + procName + "' (not a procedure) in a Sentinel NPC target.");
                        return false;
                    }
                    List<ScriptEntry> entries = script.getContainer().getBaseEntries(DenizenCore.getImplementation().getEmptyScriptEntryData());
                    if (entries.isEmpty()) {
                        return false;
                    }
                    long id = DetermineCommand.getNewId();
                    ScriptBuilder.addObjectToEntries(entries, "reqid", id);
                    InstantQueue queue = InstantQueue.getQueue(ScriptQueue.getNextId(script.getContainer().getName()));
                    queue.addEntries(entries);
                    queue.setReqId(id);
                    String def_name = "entity";
                    if (script.getContainer().getContents().contains("definitions")) {
                        List<String> definition_names = CoreUtilities.split(script.getContainer().getString("definitions"), '|');
                        if (definition_names.size() >= 1) {
                            def_name = definition_names.get(0);
                        }
                    }
                    queue.addDefinition(def_name, new dEntity(ent).getDenizenObject());
                    queue.start();
                    if (DetermineCommand.hasOutcome(id)) {
                        return CoreUtilities.toLowerCase(DetermineCommand.getOutcome(id).get(0)).equals("true");
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
