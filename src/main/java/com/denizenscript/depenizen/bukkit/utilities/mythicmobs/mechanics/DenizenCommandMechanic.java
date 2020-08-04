package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.mechanics;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DenizenCommandMechanic extends SkillMechanic implements INoTargetSkill, ITargetedLocationSkill, ITargetedEntitySkill {
    final String command;
    OldEventManager.OldEventContextSource source;
    List<Object> entries = new ArrayList<>();
    List<ScriptEntry> scriptEntries;
    HashMap<String, ObjectTag> context;

    public DenizenCommandMechanic(String name, MythicLineConfig config) {
        super(name, config);
        this.ASYNC_SAFE = false;
        command = config.getString("cmd");
        entries.add(command);
        context = new HashMap<>();
        source = new OldEventManager.OldEventContextSource();
        scriptEntries = ScriptBuilder.buildScriptEntries(entries, null, new BukkitScriptEntryData(null, null));
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity entity) {
        context.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        context.put("target", new EntityTag(entity.getBukkitEntity()));
        return RunCommand();
    }

    @Override
    public boolean castAtLocation(SkillMetadata skillMetadata, AbstractLocation location) {
        context.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        context.put("target", new LocationTag((World) location.getWorld(), location.getX(), location.getY(), location.getZ()));
        return RunCommand();
    }

    @Override
    public boolean cast(SkillMetadata skillMetadata) {
        context.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        return RunCommand();
    }

    public boolean RunCommand() {
        InstantQueue queue = new InstantQueue("MythicMobsMechanic");
        source.contexts = context;
        queue.addEntries(scriptEntries);
        queue.setContextSource(source);
        queue.start();
        return true;
    }
}
