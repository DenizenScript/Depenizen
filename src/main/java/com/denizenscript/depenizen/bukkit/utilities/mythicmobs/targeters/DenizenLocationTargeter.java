package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    OldEventManager.OldEventContextSource source;
    HashMap<String, ObjectTag> context;

    public DenizenLocationTargeter(MythicLineConfig mlc) {
        super(mlc);
        tag = mlc.getString("tag");
        context = new HashMap<>();
        source = new OldEventManager.OldEventContextSource();
        source.contexts = new HashMap<>();
    }

    @Override
    public HashSet<AbstractLocation> getLocations(SkillMetadata skillMetadata) {
        TagContext tagContext = CoreUtilities.noDebugContext.clone();
        tagContext.contextSource = source;
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        ObjectTag object = TagManager.tagObject( tag , tagContext);
        List<LocationTag> list = (object.asType(ListTag.class, tagContext)).filter(LocationTag.class, tagContext);
        HashSet<AbstractLocation> locations = new HashSet<AbstractLocation>();
        for (LocationTag location : list) {
            locations.add(BukkitAdapter.adapt(location));
        }
        return locations;
    }
}
