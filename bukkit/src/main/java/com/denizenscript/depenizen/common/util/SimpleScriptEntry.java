package com.denizenscript.depenizen.common.util;

import java.util.List;

public class SimpleScriptEntry {

    private String command;
    private List<String> arguments;

    public SimpleScriptEntry(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
