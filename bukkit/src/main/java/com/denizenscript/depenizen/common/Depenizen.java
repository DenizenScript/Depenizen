package com.denizenscript.depenizen.common;

public class Depenizen {

    private static DepenizenImplementation implementation;

    public static DepenizenImplementation getImplementation() {
        return implementation;
    }

    public static void init(DepenizenImplementation implementation) {
        Depenizen.implementation = implementation;
    }
}
