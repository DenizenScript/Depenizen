package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyChatPlayerProperties;
import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.ChannelsHolder;

public class TownyChatBridge extends Bridge {

    public static TownyChatBridge instance;
    public static ChannelsHolder channelsHolder;

    @Override
    public void init() {
        instance = this;
        channelsHolder = ((Chat) instance.plugin).getChannelsHandler();
        PropertyParser.registerProperty(TownyChatPlayerProperties.class, PlayerTag.class);
    }
}
