package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPacketInScript extends Packet {

    private List<ScriptEntry> scriptEntries;
    private Map<String, String> definitions;

    public ClientPacketInScript() {
    }

    public List<ScriptEntry> getScriptEntries() {
        return scriptEntries;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    @Override
    public void deserialize(ByteArrayDataInput input) {
        int scriptDataLength = input.readInt();
        byte[] scriptData = new byte[scriptDataLength];
        input.readFully(scriptData);

        ByteArrayDataInput scriptInput = ByteStreams.newDataInput(scriptData);
        List<ScriptEntry> scriptEntryList = new ArrayList<ScriptEntry>();
        int commandCount = scriptInput.readInt();
        for (int i = 0; i < commandCount; i++) {
            int commandLength = scriptInput.readInt();
            byte[] commandData = new byte[commandLength];
            scriptInput.readFully(commandData);
            String command = new String(commandData);
            int argumentCount = scriptInput.readInt();
            String[] arguments = new String[argumentCount];
            for (int a = 0; a < argumentCount; a++) {
                int argumentLength = scriptInput.readInt();
                byte[] argumentData = new byte[argumentLength];
                scriptInput.readFully(argumentData);
                arguments[a] = new String(argumentData);
            }
            try {
                scriptEntryList.add(new ScriptEntry(command, arguments, null));
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
        this.scriptEntries = scriptEntryList;

        int definitionsDataLength = input.readInt();
        byte[] definitionsData = new byte[definitionsDataLength];
        input.readFully(definitionsData);

        ByteArrayDataInput definitionsInput = ByteStreams.newDataInput(definitionsData);
        Map<String, String> definitionsMap = new HashMap<String, String>();
        int definitionsCount = definitionsInput.readInt();
        for (int i = 0; i < definitionsCount; i++) {
            int defLength = definitionsInput.readInt();
            byte[] defData = new byte[defLength];
            definitionsInput.readFully(defData);
            int valLength = definitionsInput.readInt();
            byte[] valData = new byte[valLength];
            definitionsInput.readFully(valData);
            definitionsMap.put(new String(defData), new String(valData));
        }
        this.definitions = definitionsMap;
    }
}
