package com.fyy.misc;

import com.fyy.gui.Controller;
import com.fyy.misc.Color;

public class Printer {
    public static Controller controller = null;

    private static void out(String s, int newline) {
        int cli = 1;
        if (Printer.controller != null) {
            cli = 0;

            s = String.join("", s.split("\u001B\\[[0-9]{2}m"));
        }

        switch (cli + newline * 2) {
            case 0: // GUI without newline
                Printer.controller.addOutput(s, false);
                break;
            case 2: // GUI with newline
                Printer.controller.addOutput(s, true);
                break;
            case 1: // GUI without newline
                System.out.print(s);
                break;
            case 3: // GUI without newline
                System.out.println(s);
                break;
        }
    }

    public static void println() {
        Printer.println("");
    }

    public static void println(String msg) {
        Printer.out(msg + Color.RESET, 1);
    }

    public static void print(String msg) {
        Printer.out(msg + Color.RESET, 0);
    }

    public static void success(String msg) {
        Printer.out(Color.GREEN + msg + Color.RESET, 1);
    }

    public static void error(String error) {
        Printer.out(Color.RED + error + Color.RESET, 1);
    }

    public static void done() {
        Printer.success("DONE!");
    }
}
