package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.tags.worldedit.WorldEditPlayerTags;

public class WorldEditSupport {

    public Depenizen depenizen;

    public WorldEditSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        DenizenAPI.getCurrentInstance().getPropertyParser().registerProperty(WorldEditPlayerTags.class, dPlayer.class);
    }

}
