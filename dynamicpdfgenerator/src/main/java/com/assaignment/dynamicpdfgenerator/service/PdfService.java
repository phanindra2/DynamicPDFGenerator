package com.assaignment.dynamicpdfgenerator.service;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);
    private final TemplateEngine templateEngine;
    private static final String PDF_STORAGE_PATH = "./pdf-storage/";

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        createPdfStorageDirectory();
    }

    public String generateOrRetrievePDF(Invoice request) throws Exception {
        String hash = hashRequestData(request);
        String filePath = PDF_STORAGE_PATH + hash + ".pdf";
        try {
            if (Files.exists(Paths.get(filePath))) {
                logger.info("Returning existing PDF file: {}", filePath);
                return filePath;
            }

            // Generate PDF using Thymeleaf template
            // context helps to access request data in html page (in template)
            Context context = new Context();
            context.setVariable("request", request);

            // Processing template with Thymeleaf
            // here we merge or pass the context(request data) to html template so it will process that template  into html content
            String htmlContent = templateEngine.process("template", context);

            // Writing the HTML content to a PDF file
            // converting html content to pdf file using 
            //using itextpdf 
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                HtmlConverter.convertToPdf(htmlContent, outputStream);
            }

            logger.info("Generated new PDF file: {}", filePath);
            return filePath;

        } catch (Exception e) {
            logger.error("Error generating PDF", e);
            throw new RuntimeException("Error while creating PDF :",e);
        }
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

    // Ensure the storage directory exists
    private void createPdfStorageDirectory() {
        Path storagePath = Paths.get(PDF_STORAGE_PATH);
        try {
            if (Files.notExists(storagePath)) {
                Files.createDirectories(storagePath);
                logger.info("Created PDF storage directory at: {}", PDF_STORAGE_PATH);
            }
        } catch (Exception e) {
            logger.error("Could not create PDF storage directory", e);
        }
    }
}
