package com.fyy.utils;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class UrlTools {
    public static File getFilesFromUrl(String url) {
        File main = new File("");
        File last = main;

        String buffer = "";
        int slashCount = 0;

        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/' || i == url.length() - 1) {
                slashCount++;

                if (slashCount == 3) {
                    main = new File(buffer);
                    last = main;
                    buffer = "";
                } else if (slashCount > 3) {
                    if (i == url.length() - 1) {
                        buffer += url.charAt(i);
                    }
                    File tmp = new File(buffer);

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

        main.clearUrl(0);

        return main;
    }

    public static ArrayList<String> getHtml(String url) {
        ArrayList<String> result = new ArrayList<>();

        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.add(inputLine);
            in.close();
        } catch (IOException e) {
            System.out.println("UrlTools: IOException thrown with argument \"" + url + "\".");
        }

        return result;
    }

    public static ArrayList<String> getLinksFromHtml(ArrayList<String> contents) {
        ArrayList<String> result = new ArrayList<>();

        String urlRegEx = "\"([A-Za-z0-9_\\-\\.])";
        Pattern.compile("href=" + urlRegEx);

        for (String l: contents) {
            System.out.println(l);
        }

        return result;
    }
}
