package com.randomprinter;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import java.io.InputStream;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javafx.application.Platform;
import javafx.stage.WindowEvent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.layout.VBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;


import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.Random;

import java.awt.print.PageFormat;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class Main extends Application {

    static PDDocument document;
    static TextArea log_renderer;
    static Random randomGenerator;
    static TextField period_input;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Random Printer");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        
        primaryStage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        
        Scene scene = new Scene(new Group());
        VBox root = new VBox();

        Button button = new Button();
        button.setText("Start Print");

        button.setOnAction((ActionEvent e) -> {
            try {
                startPrintLoop();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                log(ex.getMessage());
            }
        });

        log_renderer = new TextArea();

        period_input = new TextField("2000");
        period_input.setPromptText("2000 means 2 seconds");
        
        
        root.getChildren().addAll(new Label("Period (in millisecods):"), period_input, button, log_renderer);
        
        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        randomGenerator = new Random();
        launch(args);
        log("Idling");

    }

    private static void log(String s) {
        log_renderer.appendText(s + "\n");
    }

    private static void startPrintLoop() throws InterruptedException {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

            @Override
            public void run() {
                try {
                    print();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    log(ex.getLocalizedMessage());
                }
            }
        }, 0, printPeriod());
    }
    
    private static Integer printPeriod() {
        if ("".equals(period_input.getText())) {
            return 2000;
        } else {
            return Integer.parseInt(period_input.getText());            
        }
    }

    private static void print() throws Exception {
        log("printing");

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(randomDocument()));
        job.setPrintService(randomPrintService());

        PrintRequestAttributeSet print_attrs = new HashPrintRequestAttributeSet();
        print_attrs.add(MediaSize.ISO.A5);

        job.print(print_attrs);
    }
    
    private static PDDocument randomDocument() throws IOException {
        PDDocument src = document();     
        PDDocument doc = new PDDocument();      
        doc.addPage(src.getPage(randomGenerator.nextInt(src.getNumberOfPages())));
        
        return doc;    
    }
    
    private static PDDocument document() throws IOException  {
        InputStream input = Main.class.getResourceAsStream("/document.pdf");
        File temp = File.createTempFile("document", ".pdf");
        FileUtils.copyInputStreamToFile(input, temp);

        return PDDocument.load(new File(temp.getAbsolutePath()));
    }
    

    private static PrintService randomPrintService() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        
        if (printServices.length > 0) {
            int index = randomGenerator.nextInt(printServices.length);
            return printServices[index];
        }

        return null;
    }
}
