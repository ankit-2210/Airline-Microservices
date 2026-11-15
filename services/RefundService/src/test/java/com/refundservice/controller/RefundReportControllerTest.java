package com.refundservice.controller;


import com.refundservice.service.RefundReportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RefundReportControllerTest {
    @Mock
    private RefundReportService refundReportService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        RefundReportController controller =
                new RefundReportController(refundReportService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void generateRefundsPdf_shouldReturnPdfResponse() throws Exception {
        byte[] pdfBytes = "%PDF-1.4 test pdf content"
                .getBytes();

        when(refundReportService.generateRefundsPdf())
                .thenReturn(pdfBytes);

        mockMvc.perform(get("/api/refunds/reports/pdf"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=refund-report.pdf"))
                .andExpect(content().bytes(pdfBytes))
                .andExpect(header().longValue(
                        "Content-Length",
                        pdfBytes.length));

        verify(refundReportService)
                .generateRefundsPdf();
    }

}
