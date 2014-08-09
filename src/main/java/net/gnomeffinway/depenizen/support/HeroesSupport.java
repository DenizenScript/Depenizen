package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.HeroesEvents;
import net.gnomeffinway.depenizen.objects.heroes.HeroesClass;
import net.gnomeffinway.depenizen.objects.heroes.HeroesHero;
import net.gnomeffinway.depenizen.tags.heroes.HeroesPlayerNPCTags;
import org.bukkit.Bukkit;

public class HeroesSupport {

    public Depenizen depenizen;

    public HeroesSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new HeroesEvents(depenizen);
        DenizenAPI.getCurrentInstance().getPropertyParser().registerProperty(HeroesPlayerNPCTags.class, dNPC.class);
        DenizenAPI.getCurrentInstance().getPropertyParser().registerProperty(HeroesPlayerNPCTags.class, dPlayer.class);
        try {
            ObjectFetcher.registerWithObjectFetcher(HeroesClass.class);
            ObjectFetcher.registerWithObjectFetcher(HeroesHero.class);
            ObjectFetcher._initialize();
        } catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Heroes tags may not function correctly.");
        }
    }

}
