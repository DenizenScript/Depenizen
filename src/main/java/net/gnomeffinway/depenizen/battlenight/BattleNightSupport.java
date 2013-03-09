package net.gnomeffinway.depenizen.battlenight;

import net.gnomeffinway.depenizen.Depenizen;

public class BattleNightSupport {

    public Depenizen depenizen;
    
    public BattleNightSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new BattleNightTags(depenizen);
    }
}
