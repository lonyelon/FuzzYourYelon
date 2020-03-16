package com.fyy.utils;


public class UrlTools {
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
