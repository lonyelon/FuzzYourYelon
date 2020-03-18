package com.fyy.utils;


import com.fyy.misc.Printer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlTools {

    public static void readRobots(PageFile f) {
        ArrayList<PageFile> found;

        PageFile robots = new PageFile("robots.txt");

        f.addChildren(robots);

        ArrayList<String> body = PageScanner.getBody(robots);

        Printer.print("Reading robots.txt... ");

        Pattern p = Pattern.compile("Disallow: ([A-Za-z\\/\\._\\-\\+]+)");
        for (String line: body) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                if (m.group(1).charAt(0) == '/') {
                    f.addChildren(new PageFile(m.group(1).substring(1)));
                } else {
                    f.addChildren(new PageFile(m.group(1)));
                }
            }
        }

        Printer.done();
    }

    public static PageFile urlToFile(String url) {
        PageFile main = new PageFile("");
        PageFile last = main;

        String buffer = "";
        int slashCount = 0;
        int maxSlash = 3;

        if (!url.contains("http")) {
            maxSlash = 1;
        }

        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/' || i == url.length() - 1) {
                slashCount++;

                if (slashCount == maxSlash) {
                    if (i == url.length() - 1) {
                        buffer += url.charAt(i);
                    }

                    main = new PageFile(buffer);
                    last = main;
                    buffer = "";
                } else if (slashCount > maxSlash) {
                    if (i == url.length() - 1) {
                        buffer += url.charAt(i);
                    }
                    PageFile tmp = new PageFile(buffer);

                    last.addChildren(tmp);
                    last = tmp;

                    buffer = "";
                } else {
                    buffer += url.charAt(i);
                }
            } else {
                buffer += url.charAt(i);
            }
        }

        main.clearUrl();

        return main;
    }
}
