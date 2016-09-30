package net.gnomeffinway.depenizen.support.plugins;

import net.gnomeffinway.depenizen.commands.dtltraders.TraderCommand;
import net.gnomeffinway.depenizen.support.Support;

public class dtlTradersSupport extends Support {

    public dtlTradersSupport() {
        new TraderCommand().activate().as("trader").withOptions("trader [open/close] ({buy}/sell) ({stock}/relation)", 1);
    }
}
