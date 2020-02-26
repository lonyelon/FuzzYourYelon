package com.fyy;

import com.fyy.utils.File;

public class Main {
    public static void main(String[] args) {
        File f = File.getFilesFromUrl("method://www.domain.dns/d0/d0.0/../../f1");
        File p = File.getFilesFromUrl("method://www.domain.dns/d1/d1.0/d1.0.0/../../d1.1/f1.1.0");
        File d = File.getFilesFromUrl("method://www.domain.dns/d0/d0.1/d0.1.0/../../d0.2/f0.2.0");

        f.print(0, false);
        p.print(0, false);
        d.print(0, false);

        f.add(p);
        f.add(d);

        f.print(0, false);

        System.out.println(f.getChildren().get(0).getChildren().get(1).getChildren().get(0).getUrl());
    }
}
