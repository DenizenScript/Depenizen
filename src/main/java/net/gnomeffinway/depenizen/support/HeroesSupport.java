package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.HeroesEvents;
import net.gnomeffinway.depenizen.objects.dHero;
import org.bukkit.Bukkit;

public class HeroesSupport {

    public Depenizen depenizen;

    public HeroesSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new HeroesEvents(depenizen);

        try {
            ObjectFetcher.registerWithObjectFetcher(dHero.class);
            ObjectFetcher._initialize();
        } catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Heroes tags may not function correctly.");
        }
    }

}
