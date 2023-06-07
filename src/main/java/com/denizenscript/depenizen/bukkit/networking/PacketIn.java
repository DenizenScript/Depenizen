package com.denizenscript.depenizen.bukkit.networking;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PacketIn {

    public abstract String getName();

    public abstract void process(ByteBuf data);

    public void fail(String reason) {
        Debug.echoError(reason);
    }

    public String readString(ByteBuf buf, String label) {
        if (buf.readableBytes() < 4) {
            fail("Invalid " + getName() + " Packet string '" + label + "' length bytes (needed 4)");
            return null;
        }
        int len = buf.readInt();
        if (buf.readableBytes() < len || len < 0) {
            fail("Invalid " + getName() + " Packet string '" + label + "' (bytes requested: " + len + ", bytes available: " + buf.readableBytes() + ")");
            return null;
        }
        byte[] strBytes = new byte[len];
        buf.readBytes(strBytes, 0, len);
        return new String(strBytes, StandardCharsets.UTF_8);
    }

    public Map<String, String> readStringMap(ByteBuf buf) {
        int size = buf.readInt();
        Map<String, String> stringMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = readString(buf, "Map key");
            String value = readString(buf, "Map value");
            if (key == null || value == null) {
                return null;
            }
            stringMap.put(key, value);
        }
        return stringMap;
    }

    public <T> T readNullable(ByteBuf buf, Supplier<T> readMethod) {
        return buf.readBoolean() ? readMethod.get() : null;
    }
}
