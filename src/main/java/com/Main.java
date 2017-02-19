package com.randomprinter;

import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.layout.VBox;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import java.io.IOException;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    static PDDocument document;
    static TextArea log_renderer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Random Printer");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        Scene scene = new Scene(new Group());
        VBox root = new VBox();


        Button button = new Button();
        button.setText("Start Print");
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    print();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        });
        
        log_renderer = new TextArea();
        root.getChildren().addAll(button, log_renderer);
        log("Idling");

        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //InvalidPasswordException, IOException, PrinterException
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    private static void log(String s) {
        log_renderer.appendText(s + "\n");
    }

    private static void print() throws Exception {
        log("printing");
        
        document = PDDocument.load(new File("./example.pdf"));

        PrintService myPrintService = findPrintService("PDFwriter");

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
    }

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}
