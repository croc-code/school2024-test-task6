package src;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileManager {
    //Считываем входной файл
    public static List<String> readFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    //Записываем данные в выходной файл
    public static void writeFile(String filePath, List<String> lines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    //Разбиваем на части входную строку и собираем в нужном формате
    public static void processLines(List<String> lines, Map<String, Double> employeeHours, Map<String, String> employeeNames) {
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            String id = parts[0];
            String lastName = parts[1];
            String firstName = parts[2];
            String middleName = parts[3];
            double hours = Double.parseDouble(parts[5]);

            String fullName = lastName + " " + firstName.charAt(0) + "." + middleName.charAt(0) + ".";
            employeeNames.put(id, fullName);
            employeeHours.put(id, employeeHours.getOrDefault(id, 0.0) + hours);
        }
    }
    //Ищем дизбаланс и добавляем в соотвтетсвующий список
    public static List<String> analyzeDisbalance(int weeklyNorm, Map<String, Double> employeeHours, Map<String, String> employeeNames) {
        List<String> underBalanced = new ArrayList<>();
        List<String> overBalanced = new ArrayList<>();

        for (Map.Entry<String, Double> entry : employeeHours.entrySet()) {
            String id = entry.getKey();
            double totalHours = entry.getValue();
            double balance = totalHours - weeklyNorm;
            double balancePercentage = Math.abs(balance) / weeklyNorm * 100;

            if (balancePercentage > 10) {
                String employee = employeeNames.get(id);
                if (balance < 0) {
                    underBalanced.add(employee + " -" + String.format("%.1f", Math.abs(balance)));
                } else {
                    overBalanced.add(employee + " +" + String.format("%.1f", balance));
                }
            }
        }

        Collections.sort(underBalanced);
        Collections.sort(overBalanced);

        List<String> result = new ArrayList<>();
        result.addAll(underBalanced);
        result.addAll(overBalanced);
        return result;
    }
}

