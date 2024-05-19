package src;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String inputFilePath = "src/resources/report.txt";  // Путь к входному файлу
        String outputFilePath = "src/resources/result.txt"; // Путь к выходному файлу

        try {
            List<String> lines = FileManager.readFile(inputFilePath);
            int weeklyNorm = Integer.parseInt(lines.get(0).trim());

            Map<String, Double> employeeHours = new HashMap<>();
            Map<String, String> employeeNames = new HashMap<>();

            FileManager.processLines(lines, employeeHours, employeeNames);

            List<String> result = FileManager.analyzeDisbalance(weeklyNorm, employeeHours, employeeNames);
            FileManager.writeFile(outputFilePath, result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

