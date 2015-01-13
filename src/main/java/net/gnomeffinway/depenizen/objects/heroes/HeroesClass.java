package net.gnomeffinway.depenizen.objects.heroes;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.Supported;
import net.gnomeffinway.depenizen.support.plugins.HeroesSupport;
import org.bukkit.Material;

import java.util.Set;

public class HeroesClass implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("hclass")
    public static HeroesClass valueOf(String string) {
        if (string == null) return null;

        string = string.replace("hclass@", "");

        Heroes heroes = Support.getPlugin(HeroesSupport.class);
        HeroClass heroClass = heroes.getClassManager().getClass(string);
        if (heroClass != null) {
            return new HeroesClass(heroClass);
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

    HeroClass heroClass;

    public HeroesClass(HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }


    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "HClass";

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
        return "Hero Class";
    }

    @Override
    public String identify() {
        return "hclass@" + heroClass.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <hclass@class.allowed_armor>
        // @returns dList(dMaterial)
        // @description
        // Lists the armor materials allowed in the class.
        // @plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("allowed_armor")) {
            Set<Material> allowed = heroClass.getAllowedArmor();
            dList list = new dList();
            for (Material armor : allowed) {
                list.add(dMaterial.getMaterialFrom(armor).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hclass@class.allowed_weapons>
        // @returns dList(dMaterial)
        // @description
        // Lists the weapon materials allowed in the class.
        // @plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("allowed_weapons")) {
            Set<Material> allowed = heroClass.getAllowedWeapons();
            dList list = new dList();
            for (Material armor : allowed) {
                list.add(dMaterial.getMaterialFrom(armor).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hclass@class.name>
        // @returns Element
        // @description
        // Returns the name of the hero class.
        // @plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("name")) {
            return new Element(heroClass.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hclass@class.type>
        // @returns Element
        // @description
        // Always returns 'Hero Class' for HeroesClass objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("type")) {
            return new Element("Hero Class").getAttribute(attribute.fulfill(1));
        }

        // TODO: more tags

        return new Element(identify()).getAttribute(attribute);

    }
}
