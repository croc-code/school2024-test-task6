package com.example;

import com.example.service.ReportManager;
import com.example.service.impl.ReportManagerImpl;
import com.example.util.CommandLineArgsUtil;


public class Main {

    public static void main(String[] args) {
        String inputPath = CommandLineArgsUtil.getInputFilePath(args);

        if (inputPath.isEmpty()) {
            System.out.println("Input file path is not specified!");
            System.exit(0);
        }

        String outputPath = CommandLineArgsUtil.getOutputFilePath(args);

        ReportManager reportManager = new ReportManagerImpl(10.0f);

        try {
            reportManager.loadDataFromFile(inputPath);
            reportManager.writeImbalanceToFile(outputPath.isEmpty() ? "result.txt": outputPath);
            System.out.println("Success!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
