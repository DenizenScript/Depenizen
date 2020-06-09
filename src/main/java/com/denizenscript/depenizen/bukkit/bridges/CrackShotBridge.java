package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.crackshot.crackshotWeaponAttachmentToggleEvent;
import com.denizenscript.depenizen.bukkit.events.crackshot.crackshotWeaponDamageEntityEvent;
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
        ScriptEvent.registerScriptEvent(new crackshotWeaponAttachmentToggleEvent());
        ScriptEvent.registerScriptEvent(new crackshotWeaponDamageEntityEvent());
        utility = new CSUtility();
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <crackshot.get_weapon[<weapon name>]>
        // @returns ItemTag
        // @plugin Depenizen, CrackShot
        // @description
        // Returns the ItemTag for the CrackShot weapon title specified, if it exists.
        // https://github.com/Shampaggon/CrackShot/wiki/The-Complete-Guide-to-CrackShot#title
        // -->
        if (attribute.startsWith("get_weapon")) {
            if (attribute.hasContext(1)) {
                ItemStack weapon = utility.generateWeapon(attribute.getContext(1));
                if (weapon != null) {
                    event.setReplacedObject(new ItemTag(weapon).getObjectAttribute(attribute.fulfill(1)));
                }
                else {
                    Debug.echoError("Invalid weapon name specified.");
                }
            }
        }
    }
}
