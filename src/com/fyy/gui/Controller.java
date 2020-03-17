package com.fyy.gui;

import com.fyy.utils.PageFile;
import com.fyy.utils.PageScanner;
import com.fyy.misc.Printer;
import com.fyy.utils.UrlTools;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import javax.swing.text.TableView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private String target;
    private PageScanner ps;

    // ---- ---- ---- FX Elements ---- ---- ----

    public TextField txt_target;
    public TextField txt_filter;
    public ListView lst_files;

    // ---- ---- ---- FXML Methods ---- ---- ----

    @FXML
    public void lockTarget() {
        this.ps = new PageScanner(txt_target.getText());
        UrlTools.readRobots(this.ps.getFiles());
        this.updateList();
    }

    @FXML
    public void scanUrl() {
        if (ps == null) {
            return;
        }

        ps.scan();

        this.updateList();
    }

    @FXML
    public void filterFiles() {
        this.updateList(this.txt_filter.getText());
    }

    @FXML
    public void save() {
        ObservableList<String> selected = this.lst_files.getSelectionModel().getSelectedItems();
        ArrayList<PageFile> f = new ArrayList<>();

        for (PageFile a: this.ps.getFiles().getAllChildren()) {
            for (String s: selected) {
                if (a.getUrl().equals(s)) {
                    f.add(a);
                    break;
                }
            }
        }

        for (PageFile a: f) {
            this.ps.getFiles().save();

            try {
                URL website = new URL(a.getUrl());
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("./found/" + a.getUrl().substring(a.getUrl().indexOf("//") + 2));
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (MalformedURLException e) {
                Printer.error("Malformed url in file " + a.getUrl());
            } catch (FileNotFoundException e) {
                Printer.error("File not found " + a.getUrl());
            } catch (IOException e) {
                Printer.error("IOExcaption at " + a.getUrl());
            }
        }
    }

    // ---- ---- ---- Other Methods ---- ---- ----

    public void updateList() {
        this.updateList("");
    }
    public void updateList(String RegEx) {
        Pattern p = Pattern.compile(RegEx);

        this.lst_files.getItems().clear();
        this.lst_files.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (PageFile a : this.ps.getFiles().getAllChildren()) {
            Matcher m = p.matcher(a.getUrl().toLowerCase());

            if (m.find()) {
                this.lst_files.getItems().add(a.getUrl());
            }
        }
    }
}
