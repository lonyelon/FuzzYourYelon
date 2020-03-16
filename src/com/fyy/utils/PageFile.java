package com.fyy.utils;

import java.util.ArrayList;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageFile {
    private String name;
    private PageFile parent;
    private ArrayList<PageFile> children;
    boolean scanned;

    // ---- ---- ---- [CONSTRUCTOR] ---- ---- ----

    public PageFile(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.scanned = false;
    }

    // ---- ---- ---- [METHODS] ---- ---- ----

    // Removes all ".." and "." from the url.
    public void clearUrl() {
        if (this.name.equals("..") && this.parent != null) {
            PageFile pa = this.parent.getParent();

            if (pa == null) {
                pa = this.parent;
            }

            for (PageFile c : this.children) {
                pa.addChildren(c);
            }
            parent.removeChild(this);
        } else if (this.name.equals(".")) {
            PageFile pa = this.parent;

            if (pa == null) {
                pa = this;
            }

            for (PageFile c : this.children) {
                pa.addChildren(c);
            }
            this.removeChild(this);
        } else {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).clearUrl();
            }
        }
    }

    // Adds a file to the url
    public void add(PageFile f) {
        if (this.name.equals(f.getName())) {
            for (PageFile c : f.getChildren()) {
                boolean found = false;
                PageFile x = new PageFile("");

                for (PageFile c2 : this.getChildren()) {
                    if (c.getName().equals(c2.getName())) {
                        found = true;
                        x = c2;
                        break;
                    }
                }

                if (!found) {
                    this.add(c);
                } else {
                    x.add(c);
                }
            }
        } else {
            this.addChildren(f);
        }
    }

    // print() recursively prints all children, for
    // example "hello/hi/no" will be printed as:
    // hello
    //      hi
    //          no
    // there are two functions because one is the one
    // you use, and the other is the recursive one.
    public void print() {
        this.print(0, false);
    }

    protected void print(int depth, boolean islast) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }

        System.out.println(this.name);

        for (int i = 0; i < this.children.size(); i++) {
            if (i == this.children.size() - 1)
                this.children.get(i).print(depth + 1, true);
            else
                this.children.get(i).print(depth + 1, false);
        }

        if (depth == 0) {
            System.out.println();
        }
    }

    // Gets the URL for this file
    public String getUrl() {
        if (this.parent == null) {
            return this.getName();
        }
        return this.parent.getUrl() + "/" + this.getName();
    }

    // Adds a child to the url, if it exists,
    // it merges the nodes.
    public void addChildren(PageFile f) {
        boolean found = false;

        for (PageFile c : this.children) {
            if (c.getName().equals(f.getName())) {
                for (PageFile x : f.getChildren()) {
                    c.addChildren(x);
                }
                found = true;
                break;
            }
        }

        if (!found) {
            this.children.add(f);
            f.setParent(this);
        }
    }

     // Gets all children for the node, and their
     // children, and... you get the point.
    public ArrayList<PageFile> getAllChildren() {
        ArrayList<PageFile> r = new ArrayList<>();
        for (PageFile c : this.children) {
            r.add(c);
            ArrayList<PageFile> carray = c.getAllChildren();
            for (PageFile x : carray) {
                r.add(x);
            }
        }
        return r;
    }

    public void save() {
        File f = new File("found/" + this.getUrl().substring(6));
        if (this.children.size() != 0) {
            f.mkdirs();

            for (PageFile pf : this.children) {
                pf.save();
            }
        }
    }

    public ArrayList<PageFile> getFilesExt(String ext) {
        ArrayList<PageFile> files = new ArrayList<>();
        Pattern p_action = Pattern.compile("\\.(" + ext + ")$");
        for (PageFile pf: this.getAllChildren()) {
            Matcher m = p_action.matcher(pf.getUrl().toLowerCase());
            if (m.find()) {
                files.add(pf);
            }
        }
        return files;
    }

    // ---- ---- ---- [GETTERS AND SETTERS] ---- ---- ----

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public void setParent(PageFile f) {
        this.parent = f;
    }

    public ArrayList<PageFile> getChildren() {
        return this.children;
    }

    public void removeChild(PageFile c) {
        this.children.remove(c);
    }

    public PageFile getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    // ---- ---- ---- [OVERRIDES] ---- ---- ----

    @Override
    public String toString() {
        return this.name;
    }
}
