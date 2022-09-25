package com.denizenscript.depenizen.bukkit.clientizen;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDeserializer {

    private final DataInput input;

    public DataDeserializer(byte[] bytes) {
        input = new DataInputStream(new ByteArrayInputStream(bytes));
    }

    public int readInt() {
        try {
            return input.readInt();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readInt()];
        try {
            input.readFully(bytes);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
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

    public Map<String, List<String>> readStringListMap() {
        int size = readInt();
        Map<String, List<String>> stringListMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = readString();
            List<String> value = readStringList();
            stringListMap.put(key, value);
        }
        return stringListMap;
    }
}
