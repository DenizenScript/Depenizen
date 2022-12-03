package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.queues.ContextSource;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.targeters.IEntityTargeter;
import io.lumine.mythic.bukkit.BukkitAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DenizenEntityTargeter implements IEntityTargeter {

    final String tag;
    ContextSource.SimpleMap source;
    HashMap<String, ObjectTag> context;

    public DenizenEntityTargeter(MythicLineConfig mlc) {
        tag = mlc.getString("tag");
        context = new HashMap<>();
        source = new ContextSource.SimpleMap();
        source.contexts = new HashMap<>();
    }

    @Override
    public HashSet<AbstractEntity> getEntities(SkillMetadata skillMetadata) {
        TagContext tagContext = CoreUtilities.noDebugContext.clone();
        tagContext.contextSource = source;
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()).getDenizenObject());
        ObjectTag object = TagManager.tagObject(tag, tagContext);
        List<EntityTag> list = object.asType(ListTag.class, tagContext).filter(EntityTag.class, tagContext);
        HashSet<AbstractEntity> entities = new HashSet<>();
        for (EntityTag entity : list) {
            entities.add(BukkitAdapter.adapt(entity.getBukkitEntity()));
        }
        return entities;
    }
}
