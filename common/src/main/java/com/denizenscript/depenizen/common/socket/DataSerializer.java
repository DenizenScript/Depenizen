package com.denizenscript.depenizen.common.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataSerializer {

    private DataOutput output;
    private ByteArrayOutputStream byteArrayOutput;

    public DataSerializer() {
        this.byteArrayOutput = new ByteArrayOutputStream();
        this.output = new DataOutputStream(byteArrayOutput);
    }

    public void writeBoolean(boolean b) {
        try {
            output.writeBoolean(b);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeUnsignedByte(int b) {
        try {
            output.writeByte(b);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeInt(int i) {
        try {
            output.writeInt(i);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeLong(long l) {
        try {
            output.writeLong(l);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeByteArray(byte[] bytes) {
        if (bytes == null) {
            writeInt(0);
        }
        else {
            writeInt(bytes.length);
            try {
                output.write(bytes);
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void writeString(String string) {
        if (string == null) {
            writeInt(0);
        }
        else {
            try {
                writeByteArray(string.getBytes("UTF-8"));
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
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

    public void writeStringListMap(Map<String, List<String>> stringListMap) {
        if (stringListMap == null) {
            writeInt(0);
        }
        else {
            writeInt(stringListMap.size());
            for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
                writeString(entry.getKey());
                writeStringList(entry.getValue());
            }
        }
    }

    public byte[] toByteArray() {
        return byteArrayOutput.toByteArray();
    }
}
