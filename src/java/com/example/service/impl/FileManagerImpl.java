package com.example.service.impl;

import com.example.service.FileManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManagerImpl implements FileManager {

    @Override
    public List<String> readLinesFromReportFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    @Override
    public void writeLinesToResultFile(String filePath, List<String> lines) throws IOException {
        try (var bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line: lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
