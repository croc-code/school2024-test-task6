package Solution;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Программа для анализа дизбаланса списаний времени сотрудников за неделю.
 */
public class Main {

    private final static String inputFilePath = "report.txt";
    private final static String outputFilePath = "result.txt";


    /**
     * Главный метод программы, который выполняет анализ дизбаланса и записывает результаты в файл.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) throws IOException {

        // Чтение входного файла
        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
        // Извлечение недельной нормы списания
        Integer weeklyNorm = Integer.parseInt(lines.getFirst().trim());
        // Парсинг записей о времени
        List<TimeEntry> entries = lines.stream()
                .skip(1)
                .map(TimeEntry::parse)
                .toList();
        // Подсчет общего количества списанных часов для каждого сотрудника
        Map<String, Double> totalHours = new HashMap<>();
        entries.forEach(entry -> {
            totalHours.put(entry.getFullName(),totalHours.getOrDefault(entry.getFullName(), 0.0) + entry.getHours());
        });
        // Вычисление дизбаланса для каждого сотрудника
        List<Disbalance> disbalances = totalHours.entrySet().stream()
                .map(e -> new Disbalance(e.getKey(), e.getValue() - weeklyNorm))
                .filter(d -> Math.abs(d.getDisbalance()) > weeklyNorm * 0.1)
                .toList();
        // Разделение дизбалансов на отрицательные и положительные
        List<Disbalance> negativeDisbalances = disbalances.stream()
                .filter(d -> d.getDisbalance() < 0)
                .sorted(Comparator.comparing(Disbalance::getFullName))
                .toList();

        List<Disbalance> positiveDisbalances = disbalances.stream()
                .filter(d -> d.getDisbalance() > 0)
                .sorted(Comparator.comparing(Disbalance::getFullName))
                .toList();

        // Запись результатов в выходной файл
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
            for (Disbalance d : negativeDisbalances) {
                writer.write(String.format("%s %.1f\n", d.getFullName(), d.getDisbalance()));
            }
            for (Disbalance d : positiveDisbalances) {
                writer.write(String.format("%s +%.1f\n", d.getFullName(), d.getDisbalance()));
            }
        }
    }
}
