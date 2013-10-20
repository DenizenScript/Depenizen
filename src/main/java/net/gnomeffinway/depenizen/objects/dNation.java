package net.gnomeffinway.depenizen.objects;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dNation implements dObject {

	/////////////////////
	//   OBJECT FETCHER
	/////////////////

	@ObjectFetcher("nation")
	public static dNation valueOf(String string) {
		if (string == null) return null;

		////////
		// Match town name

		string = string.replace("nation@", "");
		try {
			return new dNation(TownyUniverse.getDataSource().getNation(string));
		} catch (NotRegisteredException e) {
			return null;
		}
	}

	public static boolean matches(String arg) {
		arg = arg.replace("nation@", "");
		return TownyUniverse.getDataSource().hasNation(arg);
	}
	
    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////
	
	Nation nation = null;
	
	public dNation(Nation nation) {
		if (nation != null)
			this.nation = nation;
		else
			dB.echoError("Nation referenced is null!");
	}
	
    /////////////////////
    //   dObject Methods
    /////////////////
	
	private String prefix = "Nation";
	
	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public dNation setPrefix(String prefix) {
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
	public String getObjectType() {
		return "Nation";
	}

	@Override
	public String identify() {
		return "nation@" + nation.getName();
	}

	@Override
	public String getAttribute(Attribute attribute) {
		
		if (attribute.startsWith("balance"))
        	try  {
            	return new Element(nation.getHoldingBalance()).getAttribute(attribute.fulfill(1));
            }
            catch(EconomyException e) {
            	dB.echoError("Invalid economy response!");
            }
        
        else if (attribute.startsWith("capital"))
            if(nation.hasCapital())
                return new Element(nation.getCapital().getName())
            			.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("king"))
            return new Element(nation.getCapital().getMayor().getName())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("isneutral") || attribute.startsWith("is_neutral"))
            return new Element(nation.isNeutral())
        			.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            return new Element(nation.getNumResidents())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("relation")) {

    		try {
				Nation to = TownyUniverse.getDataSource().getNation(attribute.getContext(1));
				
				if(nation.hasAlly(to))
	                return new Element("allies").getAttribute(attribute.fulfill(1));
	            else if(nation.hasEnemy(to))
	            	return new Element("enemies").getAttribute(attribute.fulfill(1));
	            else
	            	return new Element("neutral").getAttribute(attribute.fulfill(1));
				
			} catch (NotRegisteredException e) {}
            
        }
        
        else if (attribute.startsWith("tag"))
            if(nation.hasTag())
                return new Element(nation.getTag())
            			.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("taxes"))
            return new Element(nation.getTaxes())
            		.getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("towncount") || attribute.startsWith("town_count"))
        	return new Element(nation.getNumTowns())
        			.getAttribute(attribute.fulfill(1));
		
		return new Element(identify()).getAttribute(attribute.fulfill(0));
		
	}

}
