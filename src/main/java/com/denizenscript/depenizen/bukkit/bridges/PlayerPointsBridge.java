package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.commands.playerpoints.PlayerPointsCommand;
import com.denizenscript.depenizen.bukkit.properties.playerpoints.PlayerPointsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.models.SortedPlayer;
import org.black_ixx.playerpoints.storage.StorageHandler;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

public class PlayerPointsBridge extends Bridge {

    public static PlayerPointsBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(PlayerPointsCommand.class,
                "PLAYERPOINTS", "playerpoints [set/give/take] [<#>]", 2);
        PropertyParser.registerProperty(PlayerPointsPlayerProperties.class, PlayerTag.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "playerpoints");
    }

    public ListTag getLeaders() {

        // This ridiculous mess copied from source of: `org.black_ixx.playerpoints.commands.LeadCommand`
        Collection<String> players = ((PlayerPoints) plugin).getModuleForClass(StorageHandler.class).getPlayers();
        TreeSet<SortedPlayer> sorted = new TreeSet<>();
        for (String name : players) {
            int points = ((PlayerPoints) plugin).getAPI().look(UUID.fromString(name));
            sorted.add(new SortedPlayer(name, points));
        }

        ListTag result = new ListTag();
        for (SortedPlayer player : sorted) {
            result.addObject(new PlayerTag(UUID.fromString(player.getName())));
        }
        return result;
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <playerpoints.leaders>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of all players known to PlayerPoints, sorted in order of point value.
        // Use like, for example, '<playerpoints.leaders.get[1].to[10]>' to get the top 10.
        // @Plugin Depenizen, PlayerPoints
        // -->
        if (attribute.startsWith("leaders")) {
            event.setReplacedObject(getLeaders().getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
