package com.assaignment.dynamicpdfgenerator.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.assaignment.dynamicpdfgenerator.model.Item;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceTest {

    private PdfService pdfService;

    @Mock
    private TemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfService = new PdfService(templateEngine);
    }

    @Test
    void testGenerateOrRetrievePDF_NewFile() throws Exception {
        // Arrange
        Invoice request = createInvoiceRequest();
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Test PDF</html>");

        String pdfPath = pdfService.generateOrRetrievePDF(request);
        assertNotNull(pdfPath, "PDF path should not be null");
        assertTrue(new File(pdfPath).exists(), "PDF file should exist after generation");
    }

    @Test
    void testGenerateOrRetrievePDF_ReusestExistingFile() throws Exception {
        // Arrange
        Invoice request = createInvoiceRequest();
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Test PDF</html>");

        // Act
        String firstPdfPath = pdfService.generateOrRetrievePDF(request);
        String secondPdfPath = pdfService.generateOrRetrievePDF(request);

        // Assert
        assertEquals(firstPdfPath, secondPdfPath, "The same file path should be returned for identical requests");
    }
    
    @Test
    void testGenerateOrRetrive_RequestNewFile() throws Exception {
    	  Invoice request = createInvoiceRequest();
          when(templateEngine.process(anyString(), any())).thenReturn("<html>Test PDF</html>");

          String firstPdfPath = pdfService.generateOrRetrievePDF(request);
          request.setBuyer("Phanimdra");
          String secondPdfPath = pdfService.generateOrRetrievePDF(request);
          assertNotEquals(firstPdfPath, secondPdfPath, "Different file paths should be returned for change requests");
    	
    }

    private Invoice createInvoiceRequest() {
        Invoice request = new Invoice();
        request.setSeller("Test Seller");
        request.setSellerGstin("29AABBCCDD121ZD");
        request.setSellerAddress("123 Test Address");
        request.setBuyer("Test Buyer");
        request.setBuyerGstin("29AABBCCDD131ZD");
        request.setBuyerAddress("456 Buyer Address");

        Item item = new Item();
        item.setName("Test Item");
        item.setQuantity("5 Nos");
        item.setRate(100.0);
        item.setAmount(500.0);
        request.setItems(Collections.singletonList(item));

        return request;
    }
}
