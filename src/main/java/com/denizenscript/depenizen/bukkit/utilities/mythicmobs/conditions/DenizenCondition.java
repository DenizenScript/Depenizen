package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.TagManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ICasterCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import org.bukkit.World;

import java.util.HashMap;

public class DenizenCondition extends SkillCondition implements IEntityCondition, ILocationCondition, ICasterCondition {
    final String tag;
    OldEventManager.OldEventContextSource source;
    HashMap<String, ObjectTag> context;
    BukkitTagContext tagContext;

    public DenizenCondition(String line, MythicLineConfig mlc) {
        super(line);
        tag = mlc.getString("tag");
        context = new HashMap<>();
        source = new OldEventManager.OldEventContextSource();
        source.contexts = new HashMap<>();
        BukkitTagContext tagContext = new BukkitTagContext(null, null, null, false, null);
        tagContext.contextSource = source;
    }

    @Override
    public boolean check(AbstractEntity entity) {
        source.contexts.put("entity", new EntityTag(entity.getBukkitEntity()));
        return runCheck();
    }

    @Override
    public boolean check(AbstractLocation location) {
        source.contexts.put("location", new LocationTag((World) location.getWorld(), location.getX(), location.getY(), location.getZ()));
        return runCheck();
    }

    @Override
    public boolean check(SkillCaster caster) {
        source.contexts.put("entity", new EntityTag(caster.getEntity().getBukkitEntity()));
        return runCheck();
    }
    public boolean runCheck() {
        ObjectTag object = TagManager.tagObject( tag , tagContext);
        return object.asType(ElementTag.class, tagContext).asBoolean();

    }
}
