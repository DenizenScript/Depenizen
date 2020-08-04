package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DenizenLocationTargeter extends ILocationSelector {
    final String tag;

    public DenizenLocationTargeter(MythicLineConfig mlc) {
        super(mlc);
        tag = mlc.getString("tag");
    }

    @Override
    public HashSet<AbstractLocation> getLocations(SkillMetadata skillMetadata) {
        BukkitTagContext context = new BukkitTagContext(null, null, null, false, null);
        OldEventManager.OldEventContextSource source = new OldEventManager.OldEventContextSource();
        source.contexts = new HashMap<>();
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        context.contextSource = source;
        ObjectTag object = TagManager.tagObject( tag , context);
        List<LocationTag> list = (object.asType(ListTag.class, context)).filter(LocationTag.class, context);
        HashSet<AbstractLocation> locations = new HashSet<AbstractLocation>();
        for (LocationTag location : list) {
            locations.add(BukkitAdapter.adapt(location));
        }
        return locations;
    }
}
