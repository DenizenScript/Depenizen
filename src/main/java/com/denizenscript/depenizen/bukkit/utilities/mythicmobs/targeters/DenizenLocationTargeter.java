package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.queues.ContextSource;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.targeters.ILocationTargeter;
import io.lumine.mythic.bukkit.BukkitAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DenizenLocationTargeter implements ILocationTargeter {

    final String tag;
    ContextSource.SimpleMap source;
    HashMap<String, ObjectTag> context;

    public DenizenLocationTargeter(MythicLineConfig mlc) {
        tag = mlc.getString("tag");
        context = new HashMap<>();
        source = new ContextSource.SimpleMap();
        source.contexts = new HashMap<>();
    }

    @Override
    public HashSet<AbstractLocation> getLocations(SkillMetadata skillMetadata) {
        TagContext tagContext = CoreUtilities.noDebugContext.clone();
        tagContext.contextSource = source;
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()).getDenizenObject());
        ObjectTag object = TagManager.tagObject( tag , tagContext);
        List<LocationTag> list = (object.asType(ListTag.class, tagContext)).filter(LocationTag.class, tagContext);
        HashSet<AbstractLocation> locations = new HashSet<>();
        for (LocationTag location : list) {
            locations.add(BukkitAdapter.adapt(location));
        }
        return locations;
    }
}
