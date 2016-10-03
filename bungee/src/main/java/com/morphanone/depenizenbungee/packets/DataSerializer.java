package com.morphanone.depenizenbungee.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.morphanone.depenizenbungee.dB;

import java.util.Collection;
import java.util.Map;

public class DataSerializer {

    private ByteArrayDataOutput output;

    public DataSerializer() {
        this.output = ByteStreams.newDataOutput();
    }

    public void writeBoolean(boolean b) {
        output.writeBoolean(b);
    }

    public void writeInt(int i) {
        output.writeInt(i);
    }

    public void writeLong(long l) {
        output.writeLong(l);
    }

    public void writeByteArray(byte[] bytes) {
        if (bytes == null) {
            writeInt(0);
        }
        else {
            writeInt(bytes.length);
            output.write(bytes);
        }
    }

    public void writeString(String string) {
        if (string == null) {
            writeInt(0);
        }
        else {
            try {
                writeByteArray(string.getBytes("UTF-8"));
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }

    public void writeStringList(Collection<String> stringList) {
        if (stringList == null) {
            writeInt(0);
        }
        else {
            writeInt(stringList.size());
            for (String string : stringList) {
                writeString(string);
            }
        }
    }

    public void writeStringMap(Map<String, String> stringMap) {
        if (stringMap == null) {
            writeInt(0);
        }
        else {
            writeInt(stringMap.size());
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                writeString(entry.getKey());
                writeString(entry.getValue());
            }
        }
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }
}
