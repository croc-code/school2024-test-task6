package ru.croc.school_test.file_handler;

import ru.croc.school_test.data_analyzer.DataAnalyzer;
import ru.croc.school_test.model.Employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler {

    private static String filePathToRead;

    private static String filePathToWrite;

    public static List<Employee> readFile() throws IOException {
        Path path = Paths.get(filePathToRead);
        // будем запоминать уникальных сотрудников по UUID в мапе
        Map<String, Employee> employeeMapByUUID = new HashMap<>();

        try (Scanner scanner = new Scanner(path)) {
            // запоминаем недельную норму списания часов
            DataAnalyzer.setIdealAmountOfHours(Integer.parseInt(scanner.nextLine()));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] employeeInfo = line.split(" ");
                String UUID = employeeInfo[0];  // переменная для читаемости
                // если UUID нет в мапе, то нужно сохранить нового сотрудника
                if (!employeeMapByUUID.containsKey(UUID)) {
                    // так как UUID и дата списания больше не будут нужны в рамках данной задачи,
                    // то в сущности Employee они не используются
                    employeeMapByUUID.put(UUID, new Employee(
                            employeeInfo[1],  // фамилия
                            employeeInfo[2],  // имя
                            employeeInfo[3],  // отчество
                            Double.parseDouble(employeeInfo[5])));  // его первое списание
                } else {
                    // иначе - сотрудник уже был создан, нужно лишь увеличить количество проработанных часов
                    employeeMapByUUID.get(employeeInfo[0]).
                            increaseAmountOfHours(Double.parseDouble(employeeInfo[5]));
                }
            }
        }

        // возвращаем список уникальных сотрудников из мапы
        return new ArrayList<>(employeeMapByUUID.values());
    }

    public static void writeFile(List<Employee> list) throws IOException {
        List<Employee> employeesOutput = DataAnalyzer.analyzeAmountOfHours(list);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathToWrite))) {
            // перед вывоводом сотрудников с дисбалансом сортируем их согласно ТЗ
            DataAnalyzer.sortEmployees(employeesOutput);

            for (Employee employee : employeesOutput) {
                // метод toString переопределен согласно формату вывода, обозначенному в ТЗ
                writer.write(employee.toString());
                writer.append('\n');
            }
        }
    }

    public static void setFilePathToRead(String filePathToRead) {
        FileHandler.filePathToRead = filePathToRead;
    }

    public static void setFilePathToWrite(String filePathToWrite) {
        FileHandler.filePathToWrite = filePathToWrite;
    }
}
