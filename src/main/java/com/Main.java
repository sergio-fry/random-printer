package com.randomprinter;

import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import java.io.IOException;
import java.awt.print.PrinterException;

public class Main {

    static PDDocument document;

    public static void main(String[] args) throws InvalidPasswordException, IOException, PrinterException {
        print();
    }

    private static void print() throws IOException, NullPointerException, PrinterException {
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
