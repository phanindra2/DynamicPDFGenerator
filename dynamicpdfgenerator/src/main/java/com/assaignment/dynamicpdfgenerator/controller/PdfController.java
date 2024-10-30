package com.assaignment.dynamicpdfgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.assaignment.dynamicpdfgenerator.service.PdfService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/invoice")
public class PdfController {
	
	
	@Autowired
    private PdfService pdfService;

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generatePDF(@Valid @RequestBody Invoice request) {
        try {
            String filePath = pdfService.generateOrRetrievePDF(request);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }

}
