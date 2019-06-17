package com.denizenscript.depenizen.bukkit.extensions;

import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;

public class dObjectExtension implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return getClass().getSimpleName();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
