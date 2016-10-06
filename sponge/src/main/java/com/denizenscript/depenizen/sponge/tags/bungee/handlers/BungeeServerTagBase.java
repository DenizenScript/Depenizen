package com.denizenscript.depenizen.sponge.tags.bungee.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

public class BungeeServerTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base bungee_server[<BungeeServerTag>]
    // @Group Sponge Base Types
    // @ReturnType BlockTypeTag
    // @Returns the input as a BlockTypeTag.
    // -->

    @Override
    public String getName() {
        return "bungee_server";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return BungeeServerTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
