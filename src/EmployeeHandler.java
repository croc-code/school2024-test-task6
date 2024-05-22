import java.util.*;

public class EmployeeHandler {
        public static List<Employee> groupEmployees(List<Employee> ungroupEmployees){
            HashMap<String, Double> employeeHashMap = new HashMap<>();
            //подсчет суммарного времени работы сотрудника в течение недели. Если запись уже есть - то идет суммирование с предыдущим значением
            for (Employee employee :  ungroupEmployees) {
                if (!employeeHashMap.containsKey(employee.id())){
                    employeeHashMap.put(employee.id(),employee.workedHours());
                } else {
                    employeeHashMap.put(employee.id(),employeeHashMap.get(employee.id())+employee.workedHours());
                }
            }
            //формирование списка новых record employee, т.к. нельзя обновить старые.
            List<Employee> groupedEmployees = new ArrayList<>();
            for (Employee employee : ungroupEmployees) {
                groupedEmployees.add(new Employee(employee.id(), employee.surname(), employee.name(), employee.patronymic(), employeeHashMap.get(employee.id())));
            }
            return groupedEmployees;
    }

    public static List<String> sortEmployees(HashMap<Employee, Double> unsortedMap){
            List<Employee> sortedList = new ArrayList<>();
            for (var entry : unsortedMap.entrySet()){
                sortedList.add(entry.getKey());
            }
            //сортировка идет по условию задачи: приоритет имеет разница с требуемым временем, далее же идет стандартная сорировка по ФИО
            sortedList.sort(Comparator.comparingDouble( (Employee emp) -> unsortedMap.get(emp))
                    .thenComparing(Employee::surname)
                    .thenComparing(Employee::name)
                    .thenComparing(Employee::patronymic));

            List<String> sortedData = new ArrayList<>();

            //приведение данных к необходимому формату вывода
            for (Employee employee : sortedList) {
                Double disbalanceTime = unsortedMap.get(employee);
                //задание формата данных для разницы времени: указание знака и отбрасывание нулей при необходимости
                String timeFormat = (disbalanceTime % 1 == 0) ? "%+.0f" : "%+.2f";
                sortedData.add(String.format("%s %s.%s. %s",
                        employee.surname(), employee.name().charAt(0),employee.patronymic().charAt(0),
                        String.format(Locale.ROOT, timeFormat, disbalanceTime)));
            }
            return sortedData;
    }

}
