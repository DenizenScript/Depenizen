package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.protocolsupport.ProtocolSupportExtension;
import net.gnomeffinway.depenizen.support.Support;

public class ProtocolSupportSupport extends Support {

    public ProtocolSupportSupport() {
        registerProperty(ProtocolSupportExtension.class, dPlayer.class);
    }
}
