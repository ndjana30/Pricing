package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.UserEntity;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UserWriter {
    public void ExportUsers(List<UserEntity> users, String fileName) throws IOException
    {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            // Write the header
            String[] header = { "UserName", "UserId", "Salary","Roles" };
            writer.writeNext(header);

            for (UserEntity user : users) {
                String[] data = {user.getUsername(), String.valueOf(user.getId()), String.valueOf(user.getSalary()), String.valueOf(user.getRoles())};
                writer.writeNext(data);
            }
        }
    }
}
