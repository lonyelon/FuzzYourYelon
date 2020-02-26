package com.fyy.utils;

import java.util.ArrayList;

public class File {
    private String name;
    private File parent;
    private ArrayList<File> children;
    private int type;

    // ---- ---- Static methods ---- ----

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


    // ---- ---- Methods ---- ----

    private void clearUrl(int depth) {
        if (this.name.equals("..") && this.parent != null && this.parent.getParent() != null) {
            for (File c: this.children) {
                this.parent.getParent().addChildren(c);
            }
            parent.removeChild(this);
        } else {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).clearUrl(depth+1);
            }
        }
    }

    public void add(File f) {
        if (this.name.equals(f.getName())) {
            for (File c: f.getChildren()) {
                boolean found = false;
                File x = new File("");

                for (File c2: this.getChildren()) {
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

    public void print(int depth, boolean islast) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }

        System.out.println(this.name);

        for (int i = 0; i < this.children.size(); i++) {
            if (i == this.children.size() - 1)
                this.children.get(i).print(depth+1, true);
            else
                this.children.get(i).print(depth+1, false);
        }

        if (depth == 0) {
            System.out.println();
        }
    }

    // ---- ---- Getters and Setters ---- ----

    public String getUrl() {
        if (this.parent == null) {
            return this.getName();
        }
        return this.parent.getUrl() + "/" + this.getName();
    }

    public ArrayList<File> getChildren() {
        return this.children;
    }

    public void removeChild(File c) {
        this.children.remove(c);
    }

    public File getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public File(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void addChildren(File f) {

        this.children.add(f);
        f.setParent(this);
    }

    public void setParent(File f) {
        this.parent = f;
    }
}
