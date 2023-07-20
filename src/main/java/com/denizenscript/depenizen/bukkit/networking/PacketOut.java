package com.denizenscript.depenizen.bukkit.networking;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class PacketOut {

    public abstract void writeTo(ByteBuf buf);

    public void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public void writeStringMap(ByteBuf buf, Map<String, String> stringMap) {
        buf.writeInt(stringMap.size());
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            writeString(buf, entry.getKey());
            writeString(buf, entry.getValue());
        }
    }

    public <T> void writeNullable(ByteBuf buf, T object, BiConsumer<ByteBuf, T> writeMethod) {
        if (object != null) {
            buf.writeBoolean(true);
            writeMethod.accept(buf, object);
        }
        else {
            buf.writeBoolean(false);
        }
    }
}
