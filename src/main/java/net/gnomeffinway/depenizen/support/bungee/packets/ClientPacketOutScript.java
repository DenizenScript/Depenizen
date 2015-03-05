package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizencore.scripts.ScriptEntry;

import java.util.List;
import java.util.Map;

public class ClientPacketOutScript extends Packet {

    private byte[] destination;
    private List<ScriptEntry> scriptEntries;
    private Map<String, String> definitions;

    public ClientPacketOutScript(String destination, List<ScriptEntry> scriptEntries, Map<String, String> definitions) {
        this.destination = destination.getBytes();
        this.scriptEntries = scriptEntries;
        this.definitions = definitions;
    }

    @Override
    public void serialize(ByteArrayDataOutput output) {
        output.writeInt(0x02);
        output.writeInt(destination.length);
        output.write(destination);

        ByteArrayDataOutput scriptOutput = ByteStreams.newDataOutput();
        scriptOutput.writeInt(scriptEntries.size());
        for (ScriptEntry scriptEntry : scriptEntries) {
            byte[] command = scriptEntry.getCommandName().getBytes();
            scriptOutput.writeInt(command.length);
            scriptOutput.write(command);
            List<String> arguments = scriptEntry.getOriginalArguments();
            scriptOutput.writeInt(arguments.size());
            for (String argument : arguments) {
                byte[] arg = argument.getBytes();
                scriptOutput.writeInt(arg.length);
                scriptOutput.write(arg);
            }
        }
        byte[] scriptData = scriptOutput.toByteArray();
        output.writeInt(scriptData.length);
        output.write(scriptData);

        ByteArrayDataOutput definitionsOutput = ByteStreams.newDataOutput();
        definitionsOutput.writeInt(definitions.size());
        for (Map.Entry<String, String> defVal : definitions.entrySet()) {
            byte[] def = defVal.getKey().getBytes();
            definitionsOutput.writeInt(def.length);
            definitionsOutput.write(def);
            byte[] val = defVal.getValue().getBytes();
            definitionsOutput.writeInt(val.length);
            definitionsOutput.write(val);
        }
        byte[] definitionsData = definitionsOutput.toByteArray();
        output.writeInt(definitionsData.length);
        output.write(definitionsData);
    }
}
