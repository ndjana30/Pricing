package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Product;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
@Service
public class Writer {
    public void writeToCSV(List<Product> products, String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            // Write the header
            String[] header = { "ProductName", "Cost", "Date","Employee_id","Client_id" };
            writer.writeNext(header);

            // Write the data
            for (Product product : products) {
                String[] data = { product.getName(), String.valueOf(product.getCost()), String.valueOf(product.getDateTime()), String.valueOf(product.getEmployee_id()), String.valueOf(product.getClient_id())};
                writer.writeNext(data);
            }
        }
    }

}
