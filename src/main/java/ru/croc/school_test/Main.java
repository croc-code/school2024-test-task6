package ru.croc.school_test;

import ru.croc.school_test.data_analyzer.DataAnalyzer;
import ru.croc.school_test.file_handler.FileHandler;
import ru.croc.school_test.model.Employee;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // отдельно считываем пути к файлам с командной строки (для читаемости кода)
        String inputFileName = args[0];
        String outputFileName = args[1];

        FileHandler.setFilePathToRead(inputFileName);
        FileHandler.setFilePathToWrite(outputFileName);

        // чтение файла
        List<Employee> employees = FileHandler.readFile();

        // получение списка сотрудников с дисбалансом более 10%
        List<Employee> newEmployees = DataAnalyzer.analyzeAmountOfHours(employees);

        // запись в файл
        FileHandler.writeFile(newEmployees);

    }

}
