package com.assaignment.dynamicpdfgenerator.service;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

@Service
public class PdfService {
	
	private final TemplateEngine templateEngine;
    private static final String PDF_STORAGE_PATH = "./pdf-storage/";

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        createPdfStorageDirectory();
        
    }

    public String generateOrRetrievePDF(Invoice request) throws Exception {
        String hash = hashRequestData(request);
        String filePath = PDF_STORAGE_PATH + hash + ".pdf";
        
        // Check if PDF already exists
        if (Files.exists(Paths.get(filePath))) {
        	System.err.println("GENERATING SAME FILE");
            return filePath;
        }

        // Generate PDF using Thymeleaf
        Context context = new Context();
        context.setVariable("request", request);
        String htmlContent = templateEngine.process("template", context);

        // Use iText to write HTML to PDF
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
        }

        return filePath;
    }

    private String hashRequestData(Invoice request) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(request.toString().getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    void createPdfStorageDirectory() {
        try {
            Files.createDirectories(Paths.get(PDF_STORAGE_PATH));
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }
}
