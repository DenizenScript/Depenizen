package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.commands.McMMOCommands;
import net.gnomeffinway.depenizen.tags.McMMOTags;

public class McMMOSupport {
    
    public Depenizen depenizen;
    
    public McMMOSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new McMMOTags(depenizen);
        new McMMOCommands().activate().as("MCMMO").withOptions("see documentation", 1);
    }
    
}
