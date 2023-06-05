package com.denizenscript.depenizen.bukkit.clientizen.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DataDeserializer {

    private final ByteBuf input;

    public DataDeserializer(byte[] bytes) {
        input = Unpooled.wrappedBuffer(bytes);
    }

    public int readInt() {
        return input.readInt();
    }

    public boolean readBoolean() {
        return input.readBoolean();
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readInt()];
        input.readBytes(bytes);
        return bytes;
    }

    public String readString() {
        return new String(readByteArray(), StandardCharsets.UTF_8);
    }

    public List<String> readStringList() {
        int size = readInt();
        List<String> stringList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            stringList.add(readString());
        }
        return stringList;
    }

    public Map<String, String> readStringMap() {
        int size = readInt();
        Map<String, String> stringMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = readString();
            String value = readString();
            stringMap.put(key, value);
        }
        return stringMap;
    }

    public <T> T readNullable(Supplier<T> readMethod) {
        return readBoolean() ? readMethod.get() : null;
    }
}
