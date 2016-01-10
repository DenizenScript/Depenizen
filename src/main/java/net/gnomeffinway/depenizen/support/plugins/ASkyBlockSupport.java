package net.gnomeffinway.depenizen.support.plugins;


import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.askyblock.ASkyBlockPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class ASkyBlockSupport extends Support {

    public ASkyBlockSupport() {
        registerProperty(ASkyBlockPlayerExtension.class, dPlayer.class);
    }
    
}
