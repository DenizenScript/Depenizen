package com.denizenscript.depenizen.bukkit.commands;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJob;

public class JobsCommands extends AbstractCommand {

    // <--[command]
    // @Name Jobs
    // @Syntax jobs [promote/demote/join/quit] [<job>] (<#>)
    // @Group Depenizen
    // @Plugin DepenizenBukkit, Jobs
    // @Required 2
    // @Stable untested
    // @Short Modifies the specified job of a player.
    // @Author Morphan1

    // @Description
    // This allows you to promote or demote a player's job level. This also allows you
    // to force a player to join or quit a job.

    // @Tags
    // <p@player.jobs[<job>]>

    // @Usage
    // Use to promote a player.
    // - jobs promote Woodcutter

    // @Usage
    // Use to demote a player multiple times.
    // - jobs demote Builder 3

    // @Usage
    // Use to make a player join a job.
    // - jobs join Worker player:Jeebiss

    // -->

    private enum Action {PROMOTE, DEMOTE, JOIN, QUIT}

    public JobsCommands() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));
            }

            else if (!scriptEntry.hasObject("job")
                    && arg.matchesArgumentType(JobsJob.class)) {
                scriptEntry.addObject("job", JobsJob.valueOf(arg.getValue()));
            }

            else if (!scriptEntry.hasObject("number")
                    && arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
                scriptEntry.addObject("number", new Element(arg.getValue()));
            }

        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify an action!");
        }
        if (!scriptEntry.hasObject("job")) {
            throw new InvalidArgumentsException("Must specify a job!");
        }
        if (!((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
            throw new InvalidArgumentsException("Must have a player attached to the queue.");
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        BukkitScriptEntryData scriptEntryData = (BukkitScriptEntryData) scriptEntry.entryData;

        Action action = (Action) scriptEntry.getObject("action");
        JobsJob job = (JobsJob) scriptEntry.getObject("job");
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
