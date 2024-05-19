package com.example.service;

public interface ReportManager {

    void loadDataFromFile(String filePath) throws Exception;

    void writeImbalanceToFile(String filePath) throws Exception;
}
