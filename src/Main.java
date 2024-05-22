import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        //чтение данных из файла
        List<Employee> employees = FileHandler.readFile(Path.of(args[0]));
        //получение из всех данных сгруппированный по id набор, где каждая запись уникальна, а рабочее время сложено из всех повторений
        employees = EmployeeHandler.groupEmployees(employees);
        //получение из данных пары работник - его отклонение от нормы
        HashMap<Employee,Double> employeeDoubleHashMap =  TimeAnalyzer.disbalanceAnalyzer(employees);
        //сортировка работников по условиям задания и преобразование их к данным на вывод
        List<String> outputData = EmployeeHandler.sortEmployees(employeeDoubleHashMap);
        //запись данных в файл
        FileHandler.writeFile(outputData, args[1]);
    }
}