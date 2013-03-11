package net.gnomeffinway.depenizen.mcmmo;

import net.gnomeffinway.depenizen.Depenizen;

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
