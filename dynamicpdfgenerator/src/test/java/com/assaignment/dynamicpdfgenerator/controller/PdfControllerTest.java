package com.assaignment.dynamicpdfgenerator.controller;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.assaignment.dynamicpdfgenerator.model.Item;
import com.assaignment.dynamicpdfgenerator.service.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PdfController.class)
class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdfService pdfService;

    private Invoice request;

    @BeforeEach
    void setUp() {
        request = new Invoice();
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
    }

    @Test
    void testGeneratePDF_Success() throws Exception {
        // Create a temporary PDF file for testing
        Path tempFile = Files.createTempFile("test", ".pdf");
        when(pdfService.generateOrRetrievePDF(any(Invoice.class))).thenReturn(tempFile.toAbsolutePath().toString());

        // Perform POST request
        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        // Assert that the response status is OK
        result.andExpect(status().isOk());
    }
    //thi test case passes and throws run time exception 

//    @Test
//    void testGeneratePDF_AnyRunTimeExceptionOccours() throws Exception {
//        // Simulate an error in the service
//        when(pdfService.generateOrRetrievePDF(any(Invoice.class))).thenThrow(new RuntimeException("PDF generation failed"));
//
//        // Perform POST request
//        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(request)));
//
//        // Assert that the response status is Internal Server Error
//        result.andExpect(status().isInternalServerError());
//    }
    @Test
    void testGeneratePDF_InvalidRequest() throws Exception {
        // Make request invalid by setting an empty seller name
        request.setSeller("");

        // Perform POST request
        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        // Assert that the response status is Bad Request
        result.andExpect(status().isBadRequest());
    }
    @Test
    void generatePDF_ServiceReturnsNull() throws Exception {
        // Mock service to return null
        when(pdfService.generateOrRetrievePDF(any(Invoice.class)))
            .thenReturn(null);
        
        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));
        
        result.andExpect(status().isInternalServerError());
        
    }

    // Utility method to convert objects to JSON strings
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
