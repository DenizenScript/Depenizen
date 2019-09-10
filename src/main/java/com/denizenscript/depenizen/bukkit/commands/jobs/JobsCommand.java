package com.denizenscript.depenizen.bukkit.commands.jobs;

import com.denizenscript.denizencore.objects.Argument;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;

public class JobsCommand extends AbstractCommand {

    // <--[command]
    // @Name Jobs
    // @Syntax jobs [promote/demote/join/quit] [<job>] (<#>)
    // @Group Depenizen
    // @Plugin Depenizen, Jobs
    // @Required 2
    // @Short Modifies the specified job of a player.
    //
    // @Description
    // This allows you to promote or demote a player's job level. This also allows you
    // to force a player to join or quit a job.
    //
    // @Tags
    // <PlayerTag.jobs[<job>]>
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
    // Use to make a player join a job.
    // - jobs join Worker player:Jeebiss
    //
    // -->

    private enum Action {PROMOTE, DEMOTE, JOIN, QUIT}

    public JobsCommand() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (Argument arg : scriptEntry.getProcessedArgs()) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));
            }

            else if (!scriptEntry.hasObject("job")
                    && arg.matchesArgumentType(JobsJobTag.class)) {
                scriptEntry.addObject("job", JobsJobTag.valueOf(arg.getValue()));
            }

            else if (!scriptEntry.hasObject("number")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
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

        BukkitScriptEntryData scriptEntryData = (BukkitScriptEntryData) scriptEntry.entryData;

        Action action = (Action) scriptEntry.getObject("action");
        JobsJobTag job = (JobsJobTag) scriptEntry.getObject("job");
        int number = (scriptEntry.hasObject("number") ? scriptEntry.getElement("number").asInt() : 0);
        JobsPlayer player = Jobs.getPlayerManager().getJobsPlayer(scriptEntryData.getPlayer().getName());

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
