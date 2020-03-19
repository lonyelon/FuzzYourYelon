package com.fyy.gui;

import com.fyy.utils.PageFile;
import com.fyy.utils.PageScanner;
import com.fyy.misc.Printer;
import com.fyy.utils.UrlTools;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Controller {
    private String target;
    private PageScanner mainScanner;
    private PageScanner scanner;

    // ---- ---- ---- FX Elements ---- ---- ----

    public TextField txt_target;
    public TextField txt_filter;
    public ListView lst_files;
    public ChoiceBox sl_http;
    public TextFlow tf_console;
    public Button btn_scan;
    public Button btn_download;
    public ScrollPane sp_console;
    public CheckBox cb_recursive;
    public ProgressIndicator pg_scan;

    // ---- ---- ---- FXML Methods ---- ---- ----

    @FXML
    public void initialize() {
        Printer.controller = this;

        this.btn_scan.setDisable(true);
        this.btn_download.setDisable(true);

        this.sl_http.getItems().add("http://");
        this.sl_http.getItems().add("https://");
        this.sl_http.setValue("http://");
    }

    @FXML
    public void lockTarget() {
        String website = this.txt_target.getText();

        if (!website.contains("http")) {
            website = this.sl_http.getValue().toString() + website;
        }

        this.mainScanner = new PageScanner(website);
        this.mainScanner.setController(this);

        Printer.success("Target succesfully set.");

        UrlTools.readRobots(this.mainScanner.getFiles());
        this.updateList();

        Printer.println("Software ready to fuzz.");
    }

    @FXML
    public void scanUrl() {
        if (mainScanner == null) {
            return;
        }

        this.btn_scan.setDisable(true);

        mainScanner.start();
    }

    @FXML
    public void setRecursive() {
        this.mainScanner.setRecursive(this.cb_recursive.isSelected());
    }

    @FXML
    public void filterFiles() {
        this.updateList(this.txt_filter.getText());
    }

    @FXML
    public void fileListClick() {
        if (this.lst_files.getSelectionModel().getSelectedItems().size() != 0) {
            if (!this.mainScanner.isScanning()) {
                this.btn_scan.setDisable(false);
            }
            this.btn_download.setDisable(false);
        }
    }

    @FXML
    public void save() {
        ObservableList<String> selected = this.lst_files.getSelectionModel().getSelectedItems();
        ArrayList<PageFile> f = new ArrayList<>();

        for (PageFile a : this.mainScanner.getFiles().getAllChildren()) {
            for (String s : selected) {
                if (a.getUrl().equals(s)) {
                    f.add(a);
                    break;
                }
            }
        }

        for (PageFile a : f) {
            Printer.print("Downloading \"" + a.getName() + "\"...");
            a.save();
            Printer.done();
        }
    }

    // ---- ---- ---- Other Methods ---- ---- ----

    public void addOutput(String out, boolean newline) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        out = "[" + dateFormat.format(date) + "] " + out;

        final String text = out;

        Platform.runLater(() -> {
            Label lb = new Label(text);
            this.tf_console.getChildren().add(lb);

            if (newline) {
                this.tf_console.getChildren().add(new Text(System.lineSeparator()));
            }

            this.sp_console.setVvalue(this.sp_console.getVmax());
        });
    }

    public void updateList() {
        this.updateList("");
    }

    public void updateList(String RegEx) {
        Platform.runLater(() -> {
            try {
                this.txt_filter.getStyleClass().remove("error");

                Pattern p = Pattern.compile(RegEx);

                this.lst_files.getItems().clear();
                this.lst_files.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                for (PageFile a : this.mainScanner.getFiles().getAllChildren()) {
                    Matcher m = p.matcher(a.getUrl().toLowerCase());

                    if (m.find()) {
                        this.lst_files.getItems().add(a.getUrl());
                    }
                }
            } catch (PatternSyntaxException e) {
                this.txt_filter.getStyleClass().add("error");
            }
        });
    }

    public void updateProgressBar() {
        Platform.runLater(()-> {
            if (this.mainScanner != null && this.mainScanner.isScanning()) {
                this.pg_scan.setVisible(true);
            } else {
                this.pg_scan.setVisible(false);
            }
        });
    }
}
