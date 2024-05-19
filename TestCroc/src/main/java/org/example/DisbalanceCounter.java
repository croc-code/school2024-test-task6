package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.List;

public class DisbalanceCounter{

    private static double weeklyNorm;
    private static String reportPath;
    private static String resultPath;

    public static void main(String[] args) {
        Properties properties = new Properties();

        //взятие путей к файлам из конфига и обработка ошибок
        try(InputStream input = DisbalanceCounter.class.getClassLoader().getResourceAsStream("config.properties");) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //инициализация путей к файлам
        reportPath = properties.getProperty("reportPath");
        resultPath = properties.getProperty("resultPath");
        //доп проверка
        if (reportPath == null || resultPath == null) {
            System.out.println("конфигурация должна содержать файлы ввода и вывода");
            System.exit(1);
        }

        try {
            //инициализация переменных, для работы с файлами и данными
            File reportFile = new File(reportPath);
            File resultFile = new File(resultPath);

            Path resultPath = resultFile.toPath();
            Path reportPath = reportFile.toPath();

            //чтение каждой строки из файла в список
            List<String> textLines = Files.readAllLines(reportPath);
            Map<String, Employee> employeeMap = new HashMap<>();

            //сохранение недельной нормы(первая строка файла)
            weeklyNorm = Double.parseDouble(textLines.get(0));

            //цикл со 2 строки по списку из строк файла report.txt
            //каждая строка разбивается на отдельные данные
            //собирается полное имя в соответсвии с выходными данными
            //работник помещается в мапу с (K, V) -> (UID, Employee), Employee - статический вложенный класс
            for(int i = 1; i < textLines.size(); i++) {
                String[] parts = textLines.get(i).split(" ");
                String id = parts[0];
                String lastName = parts[1];
                String firstName = parts[2];
                String patronymic = parts[3];
                String date = parts[4];
                double hours = Double.parseDouble(parts[5]);
                String fullName = lastName + " " + firstName.charAt(0) + "." + patronymic.charAt(0) + ".";
                employeeMap.putIfAbsent(id, new Employee(id, fullName));
                employeeMap.get(id).addHours(hours);
            }

            //создаются два отдельных списка для работников с позитивным и негативным дизбалансом
            //открывается поток из мапы, список фильтруется по тз
            //сортируется по имени
            //к каждому объекту класса Employee применяется метод toString()
            //данные собираются в созданный список
            List<String> negativeDisbalance = employeeMap.values().stream()
                    .filter(e -> e.getHours() <= weeklyNorm - (0.1 * weeklyNorm))
                    .sorted(Comparator.comparing(Employee::getName))
                    .map(Employee::toString)
                    .collect(Collectors.toList());

            List<String> positiveDisbalance = employeeMap.values().stream()
                    .filter(e -> e.getHours() >= weeklyNorm + (0.1 * weeklyNorm))
                    .sorted(Comparator.comparing(Employee::getName))
                    .map(Employee::toString)
                    .collect(Collectors.toList());

            //результирующий список
            List<String> results = new ArrayList<>();
            results.addAll(negativeDisbalance);
            results.addAll(positiveDisbalance);

            //запись списка в файл result.txt с опцией append(файл не будет перезаписываться)
            Files.write(resultPath, results, StandardOpenOption.APPEND);

            //обработка исключений
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //вспомогательный класс
    static class Employee{
        private String UID;
        private String name;
        private double totalHours;

        public Employee(String UID, String name) {
            this.UID = UID;
            this.name = name;
            this.totalHours = 0.0;
        }

        public void addHours(double hours) {
            this.totalHours += hours;
        }

        public String getName() {
            return name;
        }

        public String getUID() {
            return UID;
        }

        public double getHours() {
            return totalHours;
        }

        public double getDisbalance() {
            return totalHours - weeklyNorm;
        }

        @Override
        public String toString() {
            double disbalance = getDisbalance();
            String sign = disbalance > 0 ? "+" : "-";
            return String.format("%s %s%.2f", name, sign, Math.abs(disbalance));
        }
    }
}



