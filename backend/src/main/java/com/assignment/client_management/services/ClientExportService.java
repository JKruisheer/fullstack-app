package com.assignment.client_management.services;

import com.assignment.client_management.controllers.model.ClientResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Service
public class ClientExportService {

    public void writeClientsAsCsv(List<ClientResponse> clients, PrintWriter writer) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("id", "fullName", "displayName", "email", "details", "location", "active")
                .get();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
            for (ClientResponse client : clients) {
                csvPrinter.printRecord(
                        client.id(),
                        client.fullName(),
                        client.displayName(),
                        client.email(),
                        client.details(),
                        client.location(),
                        client.active()
                );
            }
        }
    }
}
