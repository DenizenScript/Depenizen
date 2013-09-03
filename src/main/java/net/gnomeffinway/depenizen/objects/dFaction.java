package net.gnomeffinway.depenizen.objects;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.mcore.money.Money;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dFaction implements dObject {
    
    /////////////////////
    //   OBJECT FETCHER
    /////////////////
    
    @ObjectFetcher("faction")
    public static dFaction valueOf(String string) {
        if (string == null) return null;        
        
        ////////
        // Match faction name
        
        string = string.replace("faction@", "");
        for (FactionColl fc : FactionColls.get().getColls()) {
            for (Faction f : fc.getAll()) {
                if (f.getComparisonName().equalsIgnoreCase(string)) {
                    return new dFaction(f);
                }
            }
        }
        
        return null;
    }
    
    public static boolean matches(String arg) {
        if (valueOf(arg) != null)
            return true;
        
        return false;
    }
    
    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////
    
    Faction faction = null;
    
    public dFaction(Faction faction) {
        if (faction != null)
            this.faction = faction;
        else
            dB.echoError("Faction referenced is null!");
    }
    
    /////////////////////
    //   dObject Methods
    /////////////////
    
    private String prefix = "Faction";
    
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dObject setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getType() {
        return "Faction";
    }

    @Override
    public String identify() {
        return "faction@" + faction.getName();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("balance"))
            return new Element(Money.get(faction))
                    .getAttribute(attribute.fulfill(1));

        else if (attribute.startsWith("home"))
            return new dLocation(faction.getHome().asBukkitBlock().getLocation())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("isopen") || attribute.startsWith("is_open"))
            return new Element(faction.isOpen())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("ispeaceful") || attribute.startsWith("is_peaceful"))
            return new Element(faction.getFlag(FFlag.PEACEFUL))
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("ispermanent") || attribute.startsWith("is_permanent"))
            return new Element(faction.getFlag(FFlag.PERMANENT))
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("leader"))
            return new dPlayer(faction.getLeader().getPlayer())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("name"))
            return new Element(faction.getName())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            return new Element(faction.getUPlayers().size())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("power"))
            return new Element(faction.getPower())
                    .getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("relation")) {
            
            Faction to = null;
            
            for (FactionColl fc : FactionColls.get().getColls())
                for (Faction f : fc.getAll())
                    if (f.getComparisonName().equalsIgnoreCase(attribute.getContext(1))) {
                        to = f;
                        break;
                    }
            
            if(to != null) 
                return new Element(faction.getRelationTo(to).name())
                        .getAttribute(attribute.fulfill(1));
        
        }
        
        else if (attribute.startsWith("size"))
            return new Element(faction.getLandCount())
                    .getAttribute(attribute.fulfill(1));
        
        return new Element(identify()).getAttribute(attribute.fulfill(1));
        
    }

}
