package net.gnomeffinway.depenizen.commands;

import me.zford.jobs.Jobs;
import me.zford.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.gnomeffinway.depenizen.objects.dJob;

public class JobsCommands extends AbstractCommand {

    private enum Action { PROMOTE, DEMOTE, JOIN, QUIT }
    
    public JobsCommands() {
        
    }
    
    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        
        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {
            
            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            }
            
            else if (!scriptEntry.hasObject("job")
                    && arg.matchesArgumentType(dJob.class)) {
                scriptEntry.addObject("job", dJob.valueOf(arg.getValue()));
            }
            
            else if (!scriptEntry.hasObject("number")
                    && arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
                scriptEntry.addObject("number", new Element(arg.getValue()));
            }
            
        }
        
        if (!scriptEntry.hasObject("action"))
            throw new InvalidArgumentsException("Must specify an action!");
        if (!scriptEntry.hasObject("job"))
            throw new InvalidArgumentsException("Must specify a job!");
        
    }
    
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        
        Action action = (Action) scriptEntry.getObject("action");
        dJob job = (dJob) scriptEntry.getObject("job");
        int number = (scriptEntry.hasObject("number") ? scriptEntry.getElement("number").asInt() : 0);
        JobsPlayer player = Jobs.getPlayerManager().getJobsPlayer(scriptEntry.getPlayer().getName());
        
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
