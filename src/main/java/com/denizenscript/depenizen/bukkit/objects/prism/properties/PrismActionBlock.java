package com.denizenscript.depenizen.bukkit.objects.prism.properties;

import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import me.botsko.prism.actions.Handler;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizen.utilities.blocks.OldMaterialsHelper;

public class PrismActionBlock implements Property {

    public static boolean describes(dObject obj) {
        return obj instanceof PrismAction;
    }

    public static PrismActionBlock getFrom(dObject action) {
        if (!describes(action)) {
            return null;
        }
        else {
            return new PrismActionBlock((PrismAction) action);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    PrismAction prismAction;
    Handler action;

    private PrismActionBlock(PrismAction action) {
        this.prismAction = action;
        this.action = action.getAction();
    }

    @Override
    public String getPropertyString() {
        return action.getBlockId() + "," + action.getBlockSubId() + "/"
                + action.getOldBlockId() + "," + action.getOldBlockSubId();
    }

    @Override
    public String getPropertyId() {
        return "block";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <prism@action.block>
        // @returns dMaterial
        // @description
        // Returns the main material for this action.
        // @Plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("block")) {
            int id = action.getBlockId();
            int subId = action.getBlockSubId();
            if (id == -1) {
                return null;
            }
            else {
                return OldMaterialsHelper.getMaterialFrom(OldMaterialsHelper.getLegacyMaterial(id), subId > -1 ? subId : 0)
                        .getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <prism@action.alt_block>
        // @returns dMaterial
        // @description
        // Returns the alternate material for this action.
        // @Plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("alt_block")) {
            int id = action.getOldBlockId();
            int subId = action.getOldBlockSubId();
            if (id == -1) {
                return null;
            }
            else {
                return OldMaterialsHelper.getMaterialFrom(OldMaterialsHelper.getLegacyMaterial(id), subId > -1 ? subId : 0)
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {

        // No documentation, internal only
        if (mechanism.matches("block") && mechanism.requireObject(Element.class)) {
            String[] materials = mechanism.getValue().asString().split("/");
            String[] material = materials[0].split(",");
            String[] old_material = materials[1].split(",");
            prismAction.setBlockData(Integer.parseInt(material[0]),
                    Integer.parseInt(material[1]),
                    Integer.parseInt(old_material[0]),
                    Integer.parseInt(old_material[1]));
        }

    }
}
