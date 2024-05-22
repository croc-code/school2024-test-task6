package solution;

/**
 * Класс TSREntity представляет сущность отчёта о проделанной работе сотрудника.
 * Состоит из количества отработанных часов сотрудника и его полного имени.
 */
public class TSREntity {
    /** Количество отработанных часов сотрудника. */
    private final double hours;

    /** Полное имя сотрудника. */
    private final String fullName;

    /**
     * Конструирует объект TSREntity с предоставленными данными.
     *
     * @param lastName Фамилия сотрудника.
     * @param firstName Имя сотрудника.
     * @param middleName Отчество сотрудника.
     * @param hours Количество отработанных часов сотрудника.
     */
    public TSREntity(String lastName, String firstName, String middleName, double hours) {
        this.hours = hours;
        this.fullName = String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
    }

    /**
     * Парсит строку входных данных для создания экземпляра TSREntity.
     *
     * @param line Строка входных данных, содержащая информацию о сущности.
     * @return Новый объект TSREntity, полученный из строки входных данных.
     * @throws IllegalArgumentException если строка входных данных недопустима.
     */
    public static TSREntity mapToInstance(String line) {
        if(line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Invalid input line");
        }
        String[] parts = line.split("\\s+");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid input format");
        }
        String lastName = parts[1];
        String firstName = parts[2];
        String middleName = parts[3];
        double hours = Double.parseDouble(parts[5]);
        return new TSREntity(lastName, firstName, middleName, hours);
    }
    /**
     * Теттер для полного имени сотрудника.
     *
     * @return Полное имя сотрудника.
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Геттер для количества отработанных часов .
     *
     * @return Количество отработанных часов.
     */
    public double getHours() {
        return hours;
    }
}
