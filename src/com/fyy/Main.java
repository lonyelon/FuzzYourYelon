package com.fyy;

import com.fyy.misc.Printer;
import com.fyy.utils.PageFile;
import com.fyy.utils.PageScanner;

public class Main {
    public static void main(String[] args) {
        PageScanner ps = new PageScanner("http://vilagarcia.es");
        ps.getFilesUrl();
        ps.getFiles().clearUrl();
        ps.getFiles().print();
        Printer.success("Found " + ps.getFiles().getAllChildren().size() + " files.");

        for (PageFile x: ps.getFiles().getFilesExt("pdf")) {
            Printer.println(x.getUrl());
        }
    }
}
