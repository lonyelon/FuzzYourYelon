package com.fyy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageScanner {
    private PageFile f;

    public PageScanner(String website) {
        this.f = UrlTools.urlToFile(website);
    }

    public static ArrayList<String> getBody(PageFile f) {
        ArrayList<String> html = new ArrayList<>();
        try {
            URL url = new URL(f.getUrl());

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                html.add(inputLine);
            in.close();
        } catch (IOException e) {
            System.out.println("Excepción de IO al leer el HTML de " + f.getUrl());
        }
        return html;
    }

    public void getFilesUrl() {
        boolean found = true;
        for (int i = 0; found; i++) {
            found = false;

            ArrayList<PageFile> files = this.f.getFilesExt("html");
            files.addAll(this.f.getFilesExt("htm"));

            files.addAll(this.f.getFilesExt("asp"));
            files.addAll(this.f.getFilesExt("aspx"));

            files.addAll(this.f.getFilesExt("php"));

            files.add(this.f);

            for (PageFile a : files) {
                if (!a.isScanned()) {
                    this.findFilesInUrl(a);
                    found = true;
                }
            }

            this.f.clearUrl();
        }
    }

    public void findFilesInUrl(PageFile PageFile) {
        ArrayList<String> a = getBody(PageFile);

        String urlPattern = "[A-Za-z0-9/_\\.-]*";

        Pattern p_action = Pattern.compile("action=\"([" + urlPattern + "]+)");
        Pattern p_src = Pattern.compile("src=\"([" + urlPattern + "]+)");
        Pattern p_href = Pattern.compile("href=\"([" + urlPattern + "]+)");

        Matcher m;

        for (String l : a) {
            m = p_action.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(1)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(1)));
            }
            m = p_src.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(1)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(1)));
            }
            m = p_href.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(1)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(1)));
            }
        }
        PageFile.setScanned(true);
    }

    public PageFile getFiles() {
        return this.f;
    }
}
