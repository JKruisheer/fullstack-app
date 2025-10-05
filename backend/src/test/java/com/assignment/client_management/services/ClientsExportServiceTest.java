package com.assignment.client_management.services;

import com.assignment.client_management.controllers.model.ClientResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientsExportServiceTest {

    public static final String CSV_HEADER = "id,fullName,displayName,email,details,location,active";
    private final ClientsExportService clientsExportService = new ClientsExportService();

    @Test
    void testWriteClientsAsCsvWithNoClients() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        clientsExportService.writeClientsAsCsv(List.of(), printWriter);

        String csv = stringWriter.toString().trim();
        assertTrue(csv.endsWith(CSV_HEADER));
    }

    @Test
    void testWriteClientsAsCsvWithClients() throws IOException {
        final ClientResponse johnDoe = new ClientResponse(
                1L, "John Doe", "JDoe", "john@example.com", "details", true, "NL"
        );
        final ClientResponse johnSnow = new ClientResponse(
                2L, "John Snow", "JSnow", "johnSnow@example.com", "snowy", false, "UK"
        );

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        clientsExportService.writeClientsAsCsv(List.of(johnDoe, johnSnow), printWriter);

        String csv = stringWriter.toString().trim();
        assertTrue(csv.contains(CSV_HEADER));
        assertTrue(csv.contains("1,John Doe,JDoe,john@example.com,details,true,NL"));
        assertTrue(csv.contains("2,John Snow,JSnow,johnSnow@example.com,snowy,false,UK"));

    }

}
