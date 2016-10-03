package com.denizenscript.depenizen.common;

public interface DepenizenImplementation {

    void debugMessage(String message);

    void debugException(Throwable ex);

    void debugError(String error);
}
