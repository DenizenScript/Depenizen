package net.gnomeffinway.depenizen.ancientrpg;

import net.gnomeffinway.depenizen.Depenizen;

public class AncientRPGSupport {

    public Depenizen depenizen;
    
    public AncientRPGSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        depenizen.getServer().getPluginManager().registerEvents(new AncientRPGListeners(), depenizen);
    }
}

