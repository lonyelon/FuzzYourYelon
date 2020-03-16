package com.fyy;

import com.fyy.utils.PageFile;
import com.fyy.utils.PageScanner;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        PageScanner ps = new PageScanner("http://vilagarcia.es");
        ps.getFilesUrl();
        ps.getFiles().clearUrl();
        ps.getFiles().print();
        System.out.println("Found " + ps.getFiles().getAllChildren().size() + " files.");

        for (PageFile x: ps.getFiles().getFilesExt("pdf")) {
            System.out.println(x.getUrl());
        }
    }
}
