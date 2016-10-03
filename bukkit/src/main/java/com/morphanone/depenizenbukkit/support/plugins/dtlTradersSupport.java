package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.commands.dtltraders.TraderCommand;
import com.morphanone.depenizenbukkit.support.Support;

public class dtlTradersSupport extends Support {

    public dtlTradersSupport() {
        new TraderCommand().activate().as("trader").withOptions("trader [open/close] ({buy}/sell) ({stock}/relation)", 1);
    }
}
