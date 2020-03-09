package com.fyy.utils;

import java.util.ArrayList;

public class File {
    private String name;
    private File parent;
    private ArrayList<File> children;

    public File(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void clearUrl(int depth) {
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

    public void addChildren(File f) {
        this.children.add(f);
        f.setParent(this);
    }

    public void setParent(File f) {
        this.parent = f;
    }

    @Override public String toString() {
        return this.name;
    }
}
