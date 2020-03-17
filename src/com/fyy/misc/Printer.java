package com.fyy.misc;

import com.fyy.misc.Color;

public class Printer {
    public static void println() {
        Printer.println("");
    }

    public static void println(String msg) {
        System.out.println(msg + Color.RESET);
    }

    public static void print() {
        Printer.print("");
    }

    public static void print(String msg) {
        System.out.print(msg + Color.RESET);
    }

    public static void success(String msg) {
        System.out.println(Color.GREEN + msg + Color.RESET);
    }

    public static void error(String error) {
        System.out.println(Color.RED + error + Color.RESET);
    }
}
