package com.denizenscript.depenizen.common.socket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDeserializer {

    private DataInputStream input;

    public DataDeserializer(byte[] data) {
        this.input = new DataInputStream(new ByteArrayInputStream(data));
    }

    public boolean readBoolean() {
        try {
            return input.readBoolean();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int readUnsignedByte() {
        try {
            return input.readUnsignedByte();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int readInt() {
        try {
            return input.readInt();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public long readLong() {
        try {
            return input.readLong();
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
        try {
            return input.readUTF();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String[] readStringArray() {
        String[] array = new String[readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = readString();
        }
        return array;
    }

    public List<String> readStringList() {
        List<String> stringList = new ArrayList<String>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            stringList.add(readString());
        }
        return stringList;
    }

    public Map<String, String> readStringMap() {
        Map<String, String> stringMap = new HashMap<String, String>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            String key = readString();
            String value = readString();
            stringMap.put(key, value);
        }
        return stringMap;
    }

    public Map<String, List<String>> readStringListMap() {
        Map<String, List<String>> stringListMap = new HashMap<String, List<String>>();
        int size = readInt();
        for (int i = 0; i < size; i++) {
            String key = readString();
            List<String> value = readStringList();
            stringListMap.put(key, value);
        }
        return stringListMap;
    }
}
