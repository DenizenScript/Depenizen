package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.commands.BattleNightCommands;
import net.gnomeffinway.depenizen.tags.BattleNightTags;

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
