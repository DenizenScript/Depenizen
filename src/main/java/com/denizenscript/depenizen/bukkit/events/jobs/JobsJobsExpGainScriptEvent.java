package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsExpGainScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns exp for <'job'>
    //
    // @Switch action:<action> to only process the event if it came from a specified action.
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to earn Jobs exp for a certain job.
    //
    // @Context
    // <context.job> Returns a JobsJobTag of the job that the player is gaining exp for.
    // <context.experience> Returns an ElementTag(Decimal) of the amount of exp the player will earn.
    // <context.action> Returns an ElementTag of the name of the action being paid for, which can be any of the strings from: <@link url https://github.com/Zrips/Jobs/blob/master/src/main/java/com/gamingmesh/jobs/container/ActionType.java>.
    // <context.entity> Returns an EntityTag of the entity involved with this event, if applicable.
    // <context.block> Returns a LocationTag of the block involved with this event, if applicable.
    //
    // @Determine
    // "EXP:<ElementTag(Decimal)>" to change the amount of Jobs exp this action should provide.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsExpGainScriptEvent() {
        registerCouldMatcher("jobs player earns exp for <'job'>");
        registerSwitches("action");
        this.<JobsJobsExpGainScriptEvent, ElementTag>registerOptionalDetermination("exp", ElementTag.class, (evt, context, determination) -> {
            if (determination.isDouble()) {
                evt.event.setExp(determination.asDouble());
                return true;
            }
            return false;
        });
    }

    public JobsExpGainEvent event;
    public JobsJobTag job;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryArgObject(5, job)) {
            return false;
        }
        if (!runGenericSwitchCheck(path, "action", event.getActionInfo().getType().getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "job" -> job;
            case "experience" -> new ElementTag(event.getExp());
            case "action" -> new ElementTag(event.getActionInfo().getType().getName(), true);
            case "entity" -> new EntityTag(event.getLivingEntity());
            case "block" -> event.getBlock() == null ? null : new LocationTag(event.getBlock().getLocation());
            default -> super.getContext(name);
        };
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onJobsExpGain(JobsExpGainEvent event) {
        job = new JobsJobTag(event.getJob(), Jobs.getPlayerManager().getJobsPlayer(event.getPlayer().getUniqueId()));
        this.event = event;
        fire(event);
    }
}
