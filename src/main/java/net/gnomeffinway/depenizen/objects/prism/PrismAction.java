package net.gnomeffinway.depenizen.objects.prism;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.Handler;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.objects.prism.fake.FakeHandler;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.plugins.PrismSupport;

import java.util.regex.Matcher;

public class PrismAction implements dObject, Adjustable {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static PrismAction valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("prism")
    public static PrismAction valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        Matcher m = ObjectFetcher.DESCRIBED_PATTERN.matcher(string);
        if (m.matches()) {
            return ObjectFetcher.getObjectFrom(PrismAction.class, string);
        }

        string = string.replace("prism@", "");
        Prism prism = Support.getPlugin(PrismSupport.class);
        ActionType actionType = prism.getActionRegistry().getAction(string);
        if (actionType != null) {
            return new PrismAction(actionType);
        }

        return null;
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null) {
            return true;
        }

        return false;
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    Handler action;

    public PrismAction(Handler action) {
        this.action = action;
    }

    public PrismAction(ActionType actionType) {
        this.action = new FakeHandler(actionType);
    }

    public Handler getAction() {
        return action;
    }

    public void setBlockData(int id, int subId, int oldId, int oldSubId) {
        action.setBlockId(id);
        action.setBlockSubId(subId);
        action.setOldBlockId(oldId);
        action.setOldBlockSubId(oldSubId);
    }

    public void setLocation(String worldName, double x, double y, double z) {
        action.setWorldName(worldName);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
    }

    public void setAggregateCount(int aggregateCount) {
        action.setAggregateCount(aggregateCount);
    }

    public void setPlayer(String name) {
        action.setPlayerName(name);
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "PrismAction";

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
        return "PrismAction";
    }

    @Override
    public String identify() {
        return "prism@" + action.getType().getName() + PropertyParser.getPropertiesString(this);
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <prism@action.action_type>
        // @returns Element
        // @description
        // Returns the action type of the logged action.
        // @plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("action_type")) {
            return new Element(action.getType().getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <prism@action.type>
        // @returns Element
        // @description
        // Always returns 'Prism Action' for PrismAction objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("type")) {
            return new Element("Prism Action").getAttribute(attribute.fulfill(1));
        }

        // Iterate through this object's properties' attributes
        for (Property property : PropertyParser.getProperties(this)) {
            String returned = property.getAttribute(attribute);
            if (returned != null) {
                return returned;
            }
        }

        return new Element(identify()).getAttribute(attribute);

    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        if (action.getWorldName() == null) {
            adjust(mechanism);
        }
        else {
            dB.echoError("Cannot adjust a PrismAction!");
        }
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // Iterate through this object's properties' mechanisms
        for (Property property : PropertyParser.getProperties(this)) {
            property.adjust(mechanism);
            if (mechanism.fulfilled()) {
                break;
            }
        }

        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }

    }
}
