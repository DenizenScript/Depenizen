package net.gnomeffinway.depenizen.objects;

import com.herocraftonline.heroes.characters.Hero;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dHero implements dObject {

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern npc_pattern = Pattern.compile("(hero@)?npc-(\\d+)", Pattern.CASE_INSENSITIVE);

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("hero")
    public static dHero valueOf(String string) {
        if (string == null) return null;

        string = string.replace("hero@", "");
        if (dPlayer.matches(string))
            return new dHero(dPlayer.valueOf(string).getPlayerEntity());

        Matcher m = npc_pattern.matcher(string);
        if (m.matches()) {
            NPC npc = CitizensAPI.getNPCRegistry().getById(Integer.valueOf(m.group(2)));
            if (npc.getEntity() instanceof Player)
                return new dHero((Player) npc.getEntity(), npc.getId());
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
    int npcID = -1;

    public dHero(Player player) {
        this(player, -1);
    }

    public dHero(Player player, int npcID) {
        if (player != null) {
            hero = Depenizen.heroes.getCharacterManager().getHero(player);
            this.npcID = npcID;
        }
        else
            dB.echoError("Hero referenced is null!");
    }

    public dHero(Hero hero) {
        if (hero != null) {
            this.hero = hero;
            if (CitizensAPI.getNPCRegistry().isNPC(hero.getPlayer()))
                npcID = CitizensAPI.getNPCRegistry().getNPC(hero.getPlayer()).getId();
        }
        else
            dB.echoError("Hero referenced is null!");
    }

    public Hero getHero() { return hero; }

    public boolean isPlayer() {
        return npcID == -1;
    }

    public dPlayer getDenizenPlayer() {
        return new dPlayer(hero.getPlayer());
    }

    public boolean isNPC() {
        return npcID > -1;
    }

    public dNPC getDenizenNPC() {
        return dNPC.fromEntity(hero.getPlayer());
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
        return "hero@" + (npcID > -1 ? "npc-" + npcID : hero.getPlayer().getUniqueId());
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // TODO: add tags

        return new dEntity(hero.getPlayer()).getAttribute(attribute);

    }
}
