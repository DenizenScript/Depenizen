package com.denizenscript.depenizen.bukkit.support.bungee.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDeserializer {

    private ByteArrayDataInput input;

    public DataDeserializer(byte[] data) {
        this.input = ByteStreams.newDataInput(data);
    }

    public boolean readBoolean() {
        return input.readBoolean();
    }

    public int readInt() {
        return input.readInt();
    }

    public long readLong() {
        return input.readLong();
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readInt()];
        input.readFully(bytes);
        return bytes;
    }

    public String readString() {
        try {
            return new String(readByteArray(), "UTF-8");
        }
        catch (Exception e) {
            dB.echoError(e);
            return null;
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
}
