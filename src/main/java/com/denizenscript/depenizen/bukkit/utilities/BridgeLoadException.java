package com.denizenscript.depenizen.bukkit.utilities;

public class BridgeLoadException extends RuntimeException {

    private static final long serialVersionUID = 1159105944857392268L;
    public String message = null;

    public BridgeLoadException(String msg) {
        message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
