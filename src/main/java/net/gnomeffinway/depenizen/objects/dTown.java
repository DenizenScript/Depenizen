package net.gnomeffinway.depenizen.objects;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dTown implements dObject {
	
    /////////////////////
    //   OBJECT FETCHER
    /////////////////
	
	@ObjectFetcher("town")
    public static dTown valueOf(String string) {
        if (string == null) return null;

        ////////
        // Match town name

        string = string.replace("town@", "");
        try {
			return new dTown(TownyUniverse.getDataSource().getTown(string));
		} catch (NotRegisteredException e) {
			return null;
		}
    }
	
	public static boolean matches(String arg) {
		arg = arg.replace("town@", "");
		return TownyUniverse.getDataSource().hasTown(arg);
	}
	
    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////
	
	Town town = null;
	
	public dTown(Town town) {
		if (town != null)
			this.town = town;
		else
			dB.echoError("Town referenced is null!");
	}
	
    /////////////////////
    //   dObject Methods
    /////////////////
	
	private String prefix = "Town";
	
	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public dTown setPrefix(String prefix) {
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
		return "Town";
	}

	@Override
	public String identify() {
		return "town@" + town.getName();
	}

	@Override
	public String getAttribute(Attribute attribute) {
		
		if (attribute.startsWith("balance")) {
            try  {
            	return new Element(town.getHoldingBalance()).getAttribute(attribute.fulfill(1));
            }
            catch(EconomyException e) {
            	dB.echoError("Invalid economy response!");
            }
        }
        
        else if (attribute.startsWith("board"))
            return new Element(town.getTownBoard())
                	.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("isopen") || attribute.startsWith("is_open"))
        	return new Element(town.isOpen())
					.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("ispublic") || attribute.startsWith("is_public"))
        	return new Element(town.isPublic())
    				.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("mayor")) 
        	return dPlayer.valueOf(town.getMayor().getName())
    				.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("nation"))
            try {
				return new Element(town.getNation().getName())
						.getAttribute(attribute.fulfill(1));
			} catch (NotRegisteredException e) {}
		
		else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            return new Element(town.getNumResidents())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("size"))
            return new Element(town.getPurchasedBlocks())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("spawn")) {
			try {
				return new dLocation(town.getSpawn().getBlock().getLocation())
						.getAttribute(attribute.fulfill(1));
			} 
        	catch (TownyException e) {}
        }
		
		else if (attribute.startsWith("tag"))
            return new Element(town.getTag())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("taxes"))
            return new Element(town.getTaxes())
        			.getAttribute(attribute.fulfill(1));
		
		return new Element(identify()).getAttribute(attribute.fulfill(0));
        
	}

}
