package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.crackshot.*;
import com.denizenscript.depenizen.bukkit.properties.crackshot.CrackShotEntityProperties;
import com.denizenscript.depenizen.bukkit.properties.crackshot.CrackShotItemProperties;
import com.shampaggon.crackshot.CSUtility;
import org.bukkit.inventory.ItemStack;

public class CrackShotBridge extends Bridge {

    public static CrackShotBridge instance;
    public static CSUtility utility;

    @Override
    public void init() {
        instance = this;
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "crackshot");
        PropertyParser.registerProperty(CrackShotItemProperties.class, ItemTag.class);
        PropertyParser.registerProperty(CrackShotEntityProperties.class, EntityTag.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerTogglesWeaponAttachmentEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotWeaponDamageEntityEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotWeaponCausesExplosionEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerFiresAutomaticWeaponEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerFiresProjectileEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerStartsFiringWeaponEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerPlacesLandmineEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotLandmineTriggerEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerStartsReloadingWeaponEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerFinishesReloadingWeaponEvent.class);
        ScriptEvent.registerScriptEvent(CrackShotPlayerZoomsWeaponScopeEvent.class);
        utility = new CSUtility();
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <crackshot.weapon[<weapon_name>]>
        // @returns ItemTag
        // @plugin Depenizen, CrackShot
        // @description
        // Returns the ItemTag for the CrackShot weapon title specified, if it exists. <@link url https://github.com/Shampaggon/CrackShot/wiki/The-Complete-Guide-to-CrackShot#title>
        // -->
        if (attribute.startsWith("weapon") && attribute.hasParam()) {
            ItemStack weapon = utility.generateWeapon(attribute.getParam());
            if (weapon != null) {
                event.setReplacedObject(new ItemTag(weapon).getObjectAttribute(attribute.fulfill(1)));
            }
            else {
                attribute.echoError("Invalid weapon name specified.");
            }
        }
    }
}
