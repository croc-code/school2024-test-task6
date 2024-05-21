package ru.croc.school_test.data_analyzer;

import ru.croc.school_test.model.Employee;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DataAnalyzer {

    private static int idealAmountOfHours;  // недельная норма списания, составляется из расчета 8-часового дня по ТЗ,
                                            // поэтому тип был выбран не в пользу float/double

    private static final double IMBALANCE_PORTION = 0.1;  // часть дисбаланса (10% по ТЗ)

    public static List<Employee> analyzeAmountOfHours(List<Employee> employees) {
        double limitHoursImbalance = idealAmountOfHours * IMBALANCE_PORTION;

        List<Employee> differentFromIdeal = new ArrayList<>();
        for (Employee employee: employees) {
            employee.setHoursImbalance(employee.getHoursThisWeek() - idealAmountOfHours);
            // так как дисбаланс должен составлять "более 10%" для вывода в файл
            // (а не "не менее 10%"), считаем знак строгим
            if (Math.abs(employee.getHoursImbalance()) > limitHoursImbalance) {
                differentFromIdeal.add(employee);
            }
        }

        return differentFromIdeal;
    }

    public static void sortEmployees(List<Employee> employees) {
        Collator collator = Collator.getInstance(new Locale("ru", "RU"));
        // Collator.SECONDARY позволяет различать буквы е, ё
        collator.setStrength(Collator.SECONDARY);

        // сначала производится сортировка по дисбалансу (согласно ТЗ у отрицательного дисбаланса наивысший приоритет)
        // далее список сортируется по ФИО отдельно в каждой группе по знаку дисбаланса
        employees.sort(Comparator
                .comparing((Employee e) -> e.getHoursImbalance() >= 0)
                .thenComparing(Employee::getLastName, collator)
                .thenComparing(Employee::getFirstName, collator)
                .thenComparing(Employee::getNameByFather, collator));
    }

    public static void setIdealAmountOfHours(int idealAmountOfHours) {
        DataAnalyzer.idealAmountOfHours = idealAmountOfHours;
    }
}
