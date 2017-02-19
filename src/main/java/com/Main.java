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

import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.stage.WindowEvent;

;

public class Main extends Application {

    static PDDocument document;
    static TextArea log_renderer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Random Printer");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        Scene scene = new Scene(new Group());
        VBox root = new VBox();

        Button button = new Button();
        button.setText("Start Print");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    startPrintLoop();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    log(ex.getMessage());
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

    public static void startPrintLoop() throws InterruptedException {
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
        }, 0, 2000);
    }

    private static void print() throws Exception {
        log("printing");

        document = PDDocument.load(new File("./example.pdf"));

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(printService());
        job.print();
    }

    private static PrintService printService() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        if (printServices.length > 0) {
            return printServices[0];
        }

        return null;
    }
}
