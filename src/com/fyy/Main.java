package com.fyy;

import com.fyy.utils.File;
import com.fyy.utils.UrlTools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("a(bc)");
        Matcher m = p.matcher("abc");
        for (int i = 0; m.find(); i++) {
            System.out.println(m.group(1));
        }
    }
}
