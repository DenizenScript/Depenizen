package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.EssentialsEvents;
import net.gnomeffinway.depenizen.tags.essentials.EssentialsPlayerTags;

public class EssentialsSupport {

    public Depenizen depenizen;

    public EssentialsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new EssentialsEvents(depenizen);
        DenizenAPI.getCurrentInstance().getPropertyParser().registerProperty(EssentialsPlayerTags.class, dPlayer.class);
    }

}
