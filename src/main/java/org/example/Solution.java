package org.example;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class Solution {
    private int hours;
    private final float percentage = 0.1f;
    private final HashMap<String, Employee> employees = new HashMap<>();

    private String inputFilePath = "report.txt";
    private String outputFilePath = "result.txt";

    Solution(String[] args) {
        parseArgs(args);
        readData();
        writeData(defineEmployees());
    }

    void parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "input file path");
        options.addOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                inputFilePath = cmd.getOptionValue("i");
            }
            if (cmd.hasOption("o")) {
                outputFilePath = cmd.getOptionValue("o");
            }
        } catch (ParseException e) {
            System.out.println("Unable to parse args");
        }
    }

    void readData() {
        try (BufferedReader fr = new BufferedReader(new FileReader(inputFilePath))) {
            // считывание недельной нормы списания на одного сотрудника
            hours = Integer.parseInt(fr.readLine());
            //
            String str;
            while ((str = fr.readLine()) != null) {
                String[] args = Collections.list(new StringTokenizer(str, " "))
                        .stream()
                        .map(token -> (String) token)
                        .toArray(String[]::new);

                // обновление поля отработанных часов для внесённого в employees
                if (employees.containsKey(args[0])) {
                    Employee employee = employees.get(args[0]);
                    employee.setHours(employee.getHours() + Float.parseFloat(args[5]));
                } else {
                    // добавление работника в employees
                    employees.put(args[0], new Employee(args));
                }
            }
        } catch (Exception e) {
            System.out.println("Reading error");
        }
    }

    List<Employee> defineEmployees() {
        // возвращает сотрудников, у которых дизбаланс списаний времени за неделю составляет более 10% в обе стороны
        return employees.values()
                .stream()
                .filter(employee -> Math.abs(hours - employee.getHours()) / hours > percentage)
                .sorted((o1, o2) -> {
                    // сначала сортировка по дизбалансу, потом - в алфавитном порядке
                    if (o1.getHours() < o2.getHours())
                        return -1;
                    else if (o1.getHours() > o2.getHours())
                        return 1;
                    else
                        return String.CASE_INSENSITIVE_ORDER.compare(o1.getFullName(), o2.getFullName());
                })
                .toList();
    }

    private void writeData(List<Employee> employees) {
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (var employee : employees) {
                String[] splittedFullName = employee.getFullName().split(" ");
                float diff = (employee.getHours() - hours);
                fw.write(String.format(
                        "%s %c.%c. %s\n",
                        splittedFullName[0],
                        splittedFullName[1].charAt(0),
                        splittedFullName[2].charAt(0),
                        (diff > 0) ? "+" + diff : diff
                ));
            }
        } catch (Exception e) {
            System.out.println("Writing error");
        }
    }
}
