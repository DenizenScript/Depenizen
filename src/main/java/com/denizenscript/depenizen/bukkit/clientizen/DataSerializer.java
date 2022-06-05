package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class DataSerializer {

    public DataOutput output;
    public ByteArrayOutputStream outputStream;

    public DataSerializer() {
        outputStream = new ByteArrayOutputStream();
        output = new DataOutputStream(outputStream);
    }

    public void writeInt(int i) {
        try {
            output.writeInt(i);
        }
        catch (IOException e) {
            Debug.echoError(new IllegalStateException(e));
        }
    }

    public void writeByteArray(@NotNull byte[] bytes) {
        try {
            writeInt(bytes.length);
            output.write(bytes);
        }
        catch (IOException e) {
            Debug.echoError(new IllegalStateException(e));
        }
    }

    public void writeString(@NotNull String s) {
        writeByteArray(s.getBytes(StandardCharsets.UTF_8));
    }

    public void writeStringList(@NotNull Collection<String> strings) {
        writeInt(strings.size());
        for (String s : strings) {
            writeString(s);
        }
    }

    public void writeStringListMap(@NotNull Map<String, Collection<String>> map) {
        writeInt(map.size());
        for (Map.Entry<String, Collection<String>> entry : map.entrySet()) {
            writeString(entry.getKey());
            writeStringList(entry.getValue());
        }
    }

    public void writeStringMap(Map<String, String> stringMap) {
            writeInt(stringMap.size());
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                writeString(entry.getKey());
                writeString(entry.getValue());
            }
    }

    public byte[] toByteArray() {
        return outputStream.toByteArray();
    }
}
