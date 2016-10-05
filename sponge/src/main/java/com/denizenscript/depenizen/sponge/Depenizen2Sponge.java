package com.denizenscript.depenizen.sponge;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.DepenizenImplementation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(id = Depenizen2Sponge.PluginID, name = Depenizen2Sponge.PluginName, version = Depenizen2Sponge.PluginVersionString)
public class Depenizen2Sponge implements DepenizenImplementation {

    public final static String PluginID = "depenizen2sponge";

    public final static String PluginName = "Depenizen2Sponge";

    public final static String PluginVersionString = PomData.VERSION + " (build " + PomData.BUILD_NUMBER + ")";

    public static PluginContainer plugin;

    public static Depenizen2Sponge instance;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Setup
        instance = this;
        plugin = Sponge.getPluginManager().getPlugin(PluginID).orElse(null);
        // Depenizen
        Depenizen.init(this);
    }

    @Override
    public void debugMessage(String message) {
        Denizen2Core.getImplementation().outputInfo(message);
    }

    @Override
    public void debugException(Exception exception) {
        Denizen2Core.getImplementation().outputException(exception);
    }

    @Override
    public void debugError(String error) {
        Denizen2Core.getImplementation().outputError(error);
    }
}
