package com.fyy;

import com.fyy.misc.Color;
import com.fyy.misc.Printer;
import com.fyy.utils.PageFile;
import com.fyy.utils.PageScanner;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Console {
    private boolean running;
    private Scanner s;
    private PageScanner ps;

    public Console() {
        this.running = true;
        this.s = new Scanner(System.in);
        this.ps = null;

        this.loop();
    }

    private void loop() {
        while (this.running) {
            if (this.ps != null) {
                Printer.print(Color.BLUE + "[" + this.ps.getFiles().getName() + "]>> ");
            } else {
                Printer.print(Color.BLUE + "[blank]>> ");
            }
            String input = this.s.nextLine().toLowerCase();

            String[] commands = input.split("\\ ");

            if (commands.length != 0) {
                switch (commands[0]) {
                    case "help":
                        Printer.println();
                        Printer.println("FuzzYourYelon v1.0");
                        Printer.println();
                        Printer.println("COMMAND\t\tDESCRIPTION");
                        Printer.println("-------\t\t-----------");
                        Printer.println("help\t\tShows this help menu.");
                        Printer.println("set\t\t\tSets a variable. Usage: \"set [variable] [value]\". " +
                                "\r\n\t\t\tThe most useful variable is \"target\", which is the url of the website" +
                                "\r\n\t\t\tthe fuzzer will attack.");
                        Printer.println("scan\t\tScans a given website. To set the website type \"set target [website]\".");
                        Printer.println("list\t\tLists files found. If a regular expression is specified, it will apply" +
                                "\r\n\t\t\tit by \"list [RegEx]\".");
                        Printer.println("exit\t\tCloses the software.");
                        Printer.println();
                        break;
                    case "scan":
                        if (this.ps == null) {
                            Printer.error("No target set, type \"set target [target]\" or look at \"help\".");
                            break;
                        }

                        ps.getFilesUrl();
                        break;
                    case "list":
                        if (this.ps == null) {
                            Printer.error("No files to list! try \"help\".");
                            break;
                        }
                        String RegEx = "";
                        if (commands.length == 2) {
                            RegEx = commands[1];
                        }

                        try {
                            Pattern p = Pattern.compile(RegEx);
                            for (PageFile a : this.ps.getFiles().getAllChildren()) {
                                Matcher m = p.matcher(a.getUrl().toLowerCase());
                                if (m.find()) {
                                    Printer.println(a.getUrl());
                                }
                            }
                        } catch (PatternSyntaxException e) {
                            Printer.error("\"" + RegEx + "\" is not a valid RegEx.");
                        }
                        break;
                    case "set":
                        if (commands.length != 3) {
                            Printer.error("Incorrect number of parameters. Type \"help\".");
                            break;
                        }

                        switch (commands[1]) {
                            case "target":
                                this.ps = new PageScanner(commands[2]);
                                break;
                        }
                        break;
                    case "exit":
                        this.running = false;
                        break;
                    case "":
                        break;
                    default:
                        Printer.error("Command \"" + commands[0] + "\" not found");
                }
            }
        }
    }
}
