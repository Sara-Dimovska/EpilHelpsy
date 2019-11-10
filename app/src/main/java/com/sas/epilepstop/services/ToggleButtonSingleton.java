package com.sas.epilepstop.services;

public class ToggleButtonSingleton {
    private static boolean on_off = true;
   // private static  started = false;

    private ToggleButtonSingleton(){
        on_off = true;
    }

    public static boolean getInstance() {
        return on_off;
    }

    public static void toggle(boolean change) {
        on_off = change;
    }
}
