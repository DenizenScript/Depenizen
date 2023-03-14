package com.denizenscript.depenizen.bukkit.commands.jobs;

import com.denizenscript.denizencore.objects.Argument;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;

public class JobsCommand extends AbstractCommand {

    public JobsCommand() {
        setName("jobs");
        setSyntax("jobs [promote/demote/join/quit] [<job>] (<#>)");
        setRequiredArguments(2, 3);
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
    // This allows you to promote or demote a player's job level. This also allows you
    // to force a player to join or quit a job.
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

    private enum Action {PROMOTE, DEMOTE, JOIN, QUIT}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.class)) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));
            }
            else if (!scriptEntry.hasObject("job")
                    && arg.matchesArgumentType(JobsJobTag.class)) {
                scriptEntry.addObject("job", JobsJobTag.valueOf(arg.getValue(), scriptEntry.context));
            }
            else if (!scriptEntry.hasObject("number")
                    && arg.matchesInteger()) {
                scriptEntry.addObject("number", new ElementTag(arg.getValue()));
            }
        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify an action!");
        }
        if (!scriptEntry.hasObject("job")) {
            throw new InvalidArgumentsException("Must specify a job!");
        }
        if (!Utilities.entryHasPlayer(scriptEntry)) {
            throw new InvalidArgumentsException("Must have a player attached to the queue.");
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        Action action = (Action) scriptEntry.getObject("action");
        JobsJobTag job = scriptEntry.getObjectTag("job");
        int number = (scriptEntry.hasObject("number") ? scriptEntry.getElement("number").asInt() : 0);
        JobsPlayer player = Jobs.getPlayerManager().getJobsPlayer(Utilities.getEntryPlayer(scriptEntry).getName());

        switch (action) {

            case PROMOTE:
                player.promoteJob(job.getJob(), number);
                break;

            case DEMOTE:
                player.demoteJob(job.getJob(), number);
                break;

            case JOIN:
                player.joinJob(job.getJob());
                break;

            case QUIT:
                player.leaveJob(job.getJob());
                break;

        }

    }
}
