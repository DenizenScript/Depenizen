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
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsPaymentScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns money for <'job'>
    //
    // @Switch action:<action> to only process the event if it came from a specified action.
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to be paid for a certain job.
    //
    // @Context
    // <context.job> Returns an JobsJobTag of the job that the player is being paid for.
    // <context.money> Returns an ElementTag(Decimal) of the amount of money the player will be paid.
    // <context.points> Returns an ElementTag(Decimal) of the amount of points the player will be paid.
    // <context.action> Returns an ElementTag the name of the action being paid for, which can be any of the strings from: <@link url https://github.com/Zrips/Jobs/blob/master/src/main/java/com/gamingmesh/jobs/container/ActionType.java>.
    // <context.entity> Returns an EntityTag of the entity involved with this event, if applicable.
    // <context.block> Returns a LocationTag of the block involved with this event, if applicable.
    //
    // @Determine
    // "MONEY:<ElementTag(Decimal)>" to change the amount of money this action should provide.
    // "POINTS:<ElementTag(Decimal)>" to change the amount of Jobs points this action should provide.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsPaymentScriptEvent() {
        registerCouldMatcher("jobs player earns money for <'job'>");
        registerSwitches("action");
        this.<JobsJobsPaymentScriptEvent, ElementTag>registerOptionalDetermination("money", ElementTag.class, (evt, context, input) -> {
            if (input.isDouble()) {
                evt.event.setAmount(input.asDouble());
                return true;
            }
            return false;
        });
        this.<JobsJobsPaymentScriptEvent, ElementTag>registerOptionalDetermination("points", ElementTag.class, (evt, context, input) -> {
            if (input.isDouble()) {
                evt.event.setPoints(input.asDouble());
                return true;
            }
            return false;
        });
    }

    public JobsPrePaymentEvent event;
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
            case "money" -> new ElementTag(event.getAmount());
            case "points" -> new ElementTag(event.getPoints());
            case "action" -> new ElementTag(event.getActionInfo().getType().getName(), true);
            case "entity" -> event.getLivingEntity() == null ? null : new EntityTag(event.getLivingEntity()).getDenizenObject();
            case "block" -> event.getBlock() == null ? null : new LocationTag(event.getBlock().getLocation());
            default -> super.getContext(name);
        };
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @EventHandler
    public void onJobsJobsPrepayment(JobsPrePaymentEvent event) {
        job = new JobsJobTag(event.getJob(), Jobs.getPlayerManager().getJobsPlayer(event.getPlayer().getUniqueId()));
        this.event = event;
        fire(event);
    }
}
