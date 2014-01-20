package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dFaction;
import net.gnomeffinway.depenizen.tags.FactionsTags;
import org.bukkit.Bukkit;

public class FactionsSupport {

    public Depenizen depenizen;

    public FactionsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new FactionsTags(depenizen);

        try {
            ObjectFetcher.registerWithObjectFetcher(dFaction.class);
            ObjectFetcher._initialize();
        } catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Factions tags may not function correctly.");
        }
    }
}
