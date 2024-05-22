import java.util.HashMap;
import java.util.List;

public class TimeAnalyzer {
    static int timeNorm; //норма времени из первой строки файла
    private static final double disbalanceLimit = 0.1; //максимально допустимое процентное значение отклонения, жестко указано в задании

    public static HashMap<Employee, Double> disbalanceAnalyzer(List<Employee> employees){
        double limitHoursOfDisbalance = timeNorm * disbalanceLimit; //максимально допустимое значение (в часах)
        HashMap <Employee, Double> disbalanceEmployees = new HashMap<>();
        for (Employee employee: employees) {
            double disbalance = employee.workedHours() - timeNorm;
            if (Math.abs(disbalance) > limitHoursOfDisbalance){
                disbalanceEmployees.put(employee,disbalance);
            }
        }
        return disbalanceEmployees;
    }
}
