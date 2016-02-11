package net.gnomeffinway.depenizen.extensions.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.griefprevention.GriefPreventionClaim;

public class GriefPreventionLocationExtension extends dObjectExtension {

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static GriefPreventionLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionLocationExtension((dLocation) object);
        }
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private GriefPreventionLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("grief_protection")
                || attribute.startsWith("gp")
                || attribute.startsWith("griefprotection")) {
            attribute = attribute.fulfill(1);

            // TODO: Meta
            if (attribute.startsWith("has_claim")) {
                Element ignoreHeight = Element.TRUE;
                if (attribute.hasContext(1)) {
                    ignoreHeight = new Element(attribute.getContext(1));
                    if (!ignoreHeight.asString().isEmpty() || !ignoreHeight.isBoolean()) {
                        ignoreHeight = Element.TRUE;
                    }
                }
                return new Element(dataStore.getClaimAt(location, ignoreHeight.asBoolean(), null) != null).getAttribute(attribute.fulfill(1));
            }

            // TODO: Meta
            else if (attribute.startsWith("get_claim")) {
                Claim claim = dataStore.getClaimAt(location, false, null);
                if (claim == null) {
                    return null;
                }
                return new GriefPreventionClaim(claim).getAttribute(attribute.fulfill(1));
            }

        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
