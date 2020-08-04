package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.targeters;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DenizenEntityTargeter extends IEntitySelector {
    final String tag;

    public DenizenEntityTargeter(MythicLineConfig mlc) {
        super(mlc);
        tag = mlc.getString("tag");
    }

    @Override
    public HashSet<AbstractEntity> getEntities(SkillMetadata skillMetadata) {
        BukkitTagContext context = new BukkitTagContext(null, null, null, false, null);
        OldEventManager.OldEventContextSource source = new OldEventManager.OldEventContextSource();
        source.contexts = new HashMap<>();
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        context.contextSource = source;
        ObjectTag object = TagManager.tagObject( tag , context);
        List<EntityTag> list = (object.asType(ListTag.class, context)).filter(EntityTag.class, context);
        HashSet<AbstractEntity> entities = new HashSet<AbstractEntity>();
        for (EntityTag entity : list) {
            entities.add(BukkitAdapter.adapt(entity.getBukkitEntity()));
        }
        return entities;
    }
}
