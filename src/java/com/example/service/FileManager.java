package com.example.service;

import java.io.IOException;
import java.util.List;

public interface FileManager {

    List<String> readLinesFromReportFile(String filePath) throws IOException;

    void writeLinesToResultFile(String filePath, List<String> lines) throws IOException;
}
