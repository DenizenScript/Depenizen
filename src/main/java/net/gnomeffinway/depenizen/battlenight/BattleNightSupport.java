package net.gnomeffinway.depenizen.battlenight;

import net.gnomeffinway.depenizen.Depenizen;

public class BattleNightSupport {

    public Depenizen depenizen;
    
    public BattleNightSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new BattleNightTags(depenizen);
        new BattleNightCommands().activate().as("BN").withOptions("see documentation", 1);
    }
}
