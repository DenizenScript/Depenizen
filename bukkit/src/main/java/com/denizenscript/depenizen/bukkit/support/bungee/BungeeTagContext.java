package com.denizenscript.depenizen.bukkit.support.bungee;

import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.DefinitionProvider;

public class BungeeTagContext extends TagContext {

    public BungeeTagContext(boolean debug, DefinitionProvider definitionProvider) {
        super(false, debug, null, null, definitionProvider);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return null;
    }
}
