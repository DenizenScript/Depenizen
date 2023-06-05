package com.denizenscript.depenizen.bukkit.clientizen.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class DataSerializer {

    private final ByteBuf output;

    public DataSerializer() {
        output = Unpooled.buffer();
    }

    public DataSerializer writeBoolean(boolean bool) {
        output.writeBoolean(bool);
        return this;
    }

    public DataSerializer writeInt(int i) {
        output.writeInt(i);
        return this;
    }

    public DataSerializer writeBytes(byte[] bytes) {
        output.writeBytes(bytes);
        return this;
    }

    public DataSerializer writeByteArray(byte[] bytes) {
        return writeInt(bytes.length).writeBytes(bytes);
    }

    public DataSerializer writeString(String s) {
        return writeByteArray(s.getBytes(StandardCharsets.UTF_8));
    }

    public DataSerializer writeStringList(Collection<String> strings) {
        writeInt(strings.size());
        for (String s : strings) {
            writeString(s);
        }
        return this;
    }

    public DataSerializer writeStringMap(Map<String, String> stringMap) {
        writeInt(stringMap.size());
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            writeString(entry.getKey());
            writeString(entry.getValue());
        }
        return this;
    }

    public <T> DataSerializer writeNullable(T object, BiConsumer<DataSerializer, T> writeMethod) {
        if (object != null) {
            writeBoolean(true);
            writeMethod.accept(this, object);
        }
        else {
            writeBoolean(false);
        }
        return this;
    }

    public byte[] toByteArray() {
        return output.array();
    }
}
