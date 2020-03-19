package com.fyy.utils;

import com.fyy.gui.Controller;
import com.fyy.misc.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;

public class PageScanner extends Thread {
    private Controller controller;
    private PageFile f;
    private boolean scanning;
    private boolean recursive;

    public PageScanner(String website) {
        super();

        this.recursive = false;
        this.f = UrlTools.urlToFile(website);
        this.scanning = false;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public void setController(Controller c) {
        this.controller = c;
    }

    public boolean isScanning() {
        return this.scanning;
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
            Printer.error("Scanner: Error reading " + f.getUrl());
        }
        return html;
    }

    public void run() {
        boolean found = true;
        int newfiles = 0;

        this.scanning = true;
        if (this.controller != null) {
            this.controller.updateProgressBar();
        }

        for (int i = 0; found; i++) {
            ArrayList<PageFile> files = new ArrayList<>();
            found = false;

            files = this.f.getFilesExt("html");
            files.addAll(this.f.getFilesExt("htm"));

            files.addAll(this.f.getFilesExt("asp"));
            files.addAll(this.f.getFilesExt("aspx"));

            files.addAll(this.f.getFilesExt("php"));

            files.addAll(this.f.getFilesExt(""));

            files.add(this.f);

            newfiles = files.size() - newfiles;

            Printer.println("Scanner: Found " + newfiles + " files on round " + (i + 1) + " (" + files.size() + " total web files).");

            for (PageFile a : files) {
                if (!a.isScanned()) {
                    this.findFilesInUrl(a);
                    found = true;
                }
            }

            newfiles = files.size();

            this.f.clearUrl();

            if (!this.recursive) {
                break;
            }
        }

        this.scanning = false;
        if (this.controller != null) {
            this.controller.updateList();
            this.controller.updateProgressBar();
        }
    }

    public void findFilesInUrl(PageFile PageFile) {
        ArrayList<String> a = getBody(PageFile);

        String urlPattern = "(http[s]{0,1}:\\/\\/){0,1}([A-Za-z0-9\\/_\\.\\-\\+%]+)";
        int groupNum = 2;

        Pattern p_action = Pattern.compile("action=\"" + urlPattern);
        Pattern p_src = Pattern.compile("src=\"" + urlPattern);
        Pattern p_href = Pattern.compile("href=\"" + urlPattern);

        Matcher m;

        for (String l : a) {
            m = p_action.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(groupNum)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(groupNum)));
            }
            m = p_src.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(groupNum)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(groupNum)));
            }
            m = p_href.matcher(l);
            for (int i = 0; m.find(); i++) {
                if (PageFile.getParent() != null)
                    PageFile.getParent().addChildren(UrlTools.urlToFile(m.group(groupNum)));
                else
                    PageFile.add(UrlTools.urlToFile(m.group(groupNum)));
            }
        }
        PageFile.setScanned(true);
    }

    public PageFile getFiles() {
        return this.f;
    }
}
