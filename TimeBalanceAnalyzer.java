import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TimeBalanceAnalyzer {
    public static void main(String[] args) {
        // указание пути к входному и выходному файлам
        String inputFilePath = "report.txt";
        String outputFilePath = "result.txt";

        try {
            // чтение всех строк из входного файла
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
            // первая строка содержит недельную норму списания
            int weeklyNorm = Integer.parseInt(lines.get(0).trim());
            // хранение общего времени списаний по каждому сотруднику
            Map<String, Double> timeEntries = new HashMap<>();
            // хранение соответствия между ID и ФИО
            Map<String, String> employeeNames = new HashMap<>();

            // парсинг строк файла и суммирование времени списаний по сотрудникам
            for (int i = 1; i < lines.size(); i++) {
                processLine(lines.get(i), timeEntries, employeeNames);
            }

            // списки для сотрудников с недобором и перебором времени
            List<String> underBalanced = new ArrayList<>();
            List<String> overBalanced = new ArrayList<>();

            // определение сотрудников с дизбалансом времени
            for (Map.Entry<String, Double> entry : timeEntries.entrySet()) {
                String employee = employeeNames.get(entry.getKey());
                double balance = entry.getValue() - weeklyNorm;

                // проверка на превышение дизбаланса более чем на 10% от нормы
                if (Math.abs(balance) / weeklyNorm > 0.1) {
                    String formattedBalance = formatHours(balance);
                    if (balance > 0) {
                        overBalanced.add(employee + " +" + formattedBalance);
                    } else {
                        underBalanced.add(employee + " " + formattedBalance);
                    }
                }
            }

            // сортировка списков сотрудников по фамилии
            Collections.sort(underBalanced);
            Collections.sort(overBalanced);

            // объединение результатов в один список
            List<String> result = new ArrayList<>();
            result.addAll(underBalanced);
            result.addAll(overBalanced);

            // запись результатов в выходной файл
            Files.write(Paths.get(outputFilePath), result);

        } catch (NoSuchFileException e) {
            // обработка исключений отсутствия файла
            System.err.println("Файл не найден: " + inputFilePath);
        } catch (FileAlreadyExistsException e) {
            // обработка исключений существования файла
            System.err.println("Файл уже существует: " + outputFilePath);
        } catch (IOException e) {
            // обработка общих исключений ввода-вывода
            System.err.println("Ошибка при чтении или записи файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            // обработка исключений преобразования строки в число
            System.err.println("Ошибка при преобразовании строки в число: " + e.getMessage());
        } catch (Exception e) {
            // общая обработка всех остальных исключений
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    // метод для обработки одной строки данных
    private static void processLine(String line, Map<String, Double> timeEntries, Map<String, String> employeeNames) {
        String[] parts = line.split(" ");
        String id = parts[0];
        String employeeName = parts[1] + " " + parts[2].charAt(0) + "." + parts[3].charAt(0) + ".";
        double hours = Double.parseDouble(parts[5]);

        // сохранение ФИО в соответствии с ID
        employeeNames.put(id, employeeName);
        // суммирование и сохранение времени списаний для каждого сотрудника по ID
        timeEntries.put(id, timeEntries.getOrDefault(id, 0.0) + hours);
    }

    // метод для форматирования часов, чтобы целые часы выводились без дробной части
    private static String formatHours(double hours) {
        if (hours == (long) hours) {
            return String.format("%d", (long) hours);
        } else {
            return String.format("%.1f", hours);
        }
    }
}
