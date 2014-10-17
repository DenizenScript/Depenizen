package net.gnomeffinway.depenizen.objects.heroes;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.depends.Depends;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.gnomeffinway.depenizen.support.Supported;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeroesHero implements dObject {

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern npc_pattern = Pattern.compile("(hero@)?npc-(\\d+)", Pattern.CASE_INSENSITIVE);

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("hero")
    public static HeroesHero valueOf(String string) {
        if (string == null) return null;

        string = string.replace("hero@", "");
        if (dPlayer.matches(string))
            return new HeroesHero(dPlayer.valueOf(string));

        if (Depends.citizens != null) {
            Matcher m = npc_pattern.matcher(string);
            if (m.matches()) {
                NPC npc = CitizensAPI.getNPCRegistry().getById(Integer.valueOf(m.group(2)));
                if (npc != null)
                    return new HeroesHero(dNPC.mirrorCitizensNPC(npc));
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
    //   CONSTRUCTORS
    /////////////////

    Hero hero = null;
    dObject denizenObj = null;

    public HeroesHero(dPlayer player) {
        denizenObj = player;
        if (player.isOnline()) {
            Heroes heroes = Supported.get("HEROES").getPlugin();
            hero = heroes.getCharacterManager().getHero(player.getPlayerEntity());
        }
    }

    public HeroesHero(dNPC npc) {
        denizenObj = npc;
        if (npc.isSpawned() && npc.getEntity() instanceof Player) {
            Heroes heroes = Supported.get("HEROES").getPlugin();
            hero = heroes.getCharacterManager().getHero((Player) npc.getEntity());
        }
    }

    public HeroesHero(Hero hero) {
        this.hero = hero;
        if (Depends.citizens != null && CitizensAPI.getNPCRegistry().isNPC(hero.getPlayer()))
            denizenObj = dNPC.fromEntity(hero.getPlayer());
        else
            denizenObj = dPlayer.mirrorBukkitPlayer(hero.getPlayer());
    }

    public Hero getHero() {
        return hero;
    }

    public boolean isPlayer() {
        return denizenObj instanceof dPlayer;
    }

    public boolean isNPC() {
        return denizenObj instanceof dNPC;
    }

    public dObject getDenizenObject() {
        return denizenObj;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Hero";

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
    public String getObjectType() {
        return "Hero";
    }

    @Override
    public String identify() {
        return "hero@" + (denizenObj instanceof dNPC ?
                "npc-" + ((dNPC) denizenObj).getId() :
                hero.getPlayer().getUniqueId());
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        if (hero == null) {
            dB.echoError("Specified Hero is null! Are they online/spawned?");
            return null;
        }

        // <--[tag]
        // @attribute <hero@hero.level[<class>]>
        // @returns Element(Number)
        // @description
        // Returns the level of the hero for the specified class.
        // If no class is specified, returns the hero's current highest level.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("level")) {
            if (attribute.hasContext(1)) {
                try {
                    return new Element(hero.getLevel(HeroesClass.valueOf(attribute.getContext(1)).getHeroClass()))
                            .getAttribute(attribute.fulfill(1));
                } catch (Exception e) {
                    dB.echoError("'" + attribute.getContext(1) + "' is not a valid Heroes class!");
                    return Element.NULL.getAttribute(attribute.fulfill(1));
                }
            }
            return new Element(hero.getLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hero@hero.primary_class>
        // @returns HeroesClass
        // @description
        // Returns the primary class for the hero.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("primary_class")) {
            return new HeroesClass(hero.getHeroClass()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hero@hero.secondary_class>
        // @returns HeroesClass
        // @description
        // Returns the secondary class for the hero.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("secondary_class")) {
            return new HeroesClass(hero.getHeroClass()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hero@hero.party.leader>
        // @returns dPlayer
        // @description
        // Returns the leader of the hero's party.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("party.leader")) {
            return dPlayer.mirrorBukkitPlayer(hero.getParty().getLeader().getPlayer())
                    .getAttribute(attribute.fulfill(2));
        }

        // <--[tag]
        // @attribute <hero@hero.party.members>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of players currently in the hero's party.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("party.members")) {
            dList members = new dList();
            for (Hero member : hero.getParty().getMembers()) {
                members.add(dPlayer.mirrorBukkitPlayer(member.getPlayer()).identify());
            }
            return members.getAttribute(attribute.fulfill(2));
        }

        return new Element(identify()).getAttribute(attribute);

    }
}
