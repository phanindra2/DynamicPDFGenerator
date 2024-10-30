package com.assaignment.dynamicpdfgenerator.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.assaignment.dynamicpdfgenerator.model.Invoice;
import com.assaignment.dynamicpdfgenerator.model.Item;
import com.assaignment.dynamicpdfgenerator.service.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void testGeneratePDF_ReturnsPDFFile() throws Exception {
        
        String mockFilePath = "./pdf-storage/test-invoice.pdf";
        when(pdfService.generateOrRetrievePDF(request)).thenReturn(mockFilePath);

        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));
        System.out.println(request);
        System.out.println(result);

        result.andExpect(status().isOk())
              .andExpect(header().string("Content-Disposition", "attachment; filename=" + "test-invoice.pdf"))
              .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void testGeneratePDF_InvalidRequest() throws Exception {
      
        request.setSeller("");

        ResultActions result = mockMvc.perform(post("/api/invoice/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)));

        result.andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
