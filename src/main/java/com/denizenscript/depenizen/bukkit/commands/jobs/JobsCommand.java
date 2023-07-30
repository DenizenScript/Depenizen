package com.denizenscript.depenizen.bukkit.commands.jobs;

import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.utilities.Deprecations;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.PlayerManager;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;

public class JobsCommand extends AbstractCommand {

    public JobsCommand() {
        setName("jobs");
        setSyntax("jobs [promote/demote/join/quit] [<job>] (<#>)");
        setRequiredArguments(2, 3);
        autoCompile();
    }

    // <--[command]
    // @Name Jobs
    // @Syntax jobs [promote/demote/join/quit] [<job>] (<#>)
    // @Group Depenizen
    // @Plugin Depenizen, Jobs
    // @Required 2
    // @Maximum 3
    // @Short Modifies the specified job of a player.
    //
    // @Description
    // This allows you to promote or demote a player's job level.
    // This also allows you to force a player to join or quit a job.
    //
    // @Tags
    // <PlayerTag.job[<job>]>
    // <PlayerTag.current_jobs>
    //
    // @Usage
    // Use to promote a player.
    // - jobs promote Woodcutter
    //
    // @Usage
    // Use to demote a player multiple times.
    // - jobs demote Builder 3
    //
    // @Usage
    // Use to make a different player join a job.
    // - jobs join Worker player:<[player]>
    //
    // -->


    @Override
    public void addCustomTabCompletions(TabCompletionsBuilder tab) {
        for (Job job : Jobs.getJobs()) {
            tab.add(job.getName());
        }
    }

    public enum Action {PROMOTE, DEMOTE, JOIN, QUIT}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("action") Action action,
                                   @ArgLinear @ArgName("job") ObjectTag jobObject,
                                   @ArgLinear @ArgDefaultNull @ArgName("number") ObjectTag numberObject) {
        if (!Utilities.entryHasPlayer(scriptEntry)) {
            throw new InvalidArgumentsRuntimeException("Missing linked player.");
        }
        if (numberObject != null && jobObject.asElement().isInt()) {
            Deprecations.outOfOrderArgs.warn(scriptEntry);
            ObjectTag jobObjectSwitch = jobObject;
            jobObject = numberObject;
            numberObject = jobObjectSwitch;
        }
        ElementTag numberElement = numberObject != null ? numberObject.asElement() : new ElementTag(0);
        if (!numberElement.isInt()) {
            throw new InvalidArgumentsRuntimeException("Invalid number '" + numberElement + "' specified: must be a valid non-decimal number.");
        }
        JobsJobTag job = jobObject.asType(JobsJobTag.class, scriptEntry.context);
        if (job == null) {
            throw new InvalidArgumentsRuntimeException("Invalid JobsJobTag specified: " + jobObject + '.');
        }
        PlayerManager playerManager = Jobs.getPlayerManager();
        JobsPlayer player = playerManager.getJobsPlayer(Utilities.getEntryPlayer(scriptEntry).getUUID());
        switch (action) {
            case PROMOTE -> playerManager.promoteJob(player, job.getJob(), numberElement.asInt());
            case DEMOTE -> playerManager.demoteJob(player, job.getJob(), numberElement.asInt());
            case JOIN -> playerManager.joinJob(player, job.getJob());
            case QUIT -> playerManager.leaveJob(player, job.getJob());
        }

    }
}
