/**
 * Класс, представляющий запись о списании времени сотрудником.
 */
public class TimeEntry {
    private String uuid;
    private String lastName;
    private String firstName;
    private String middleName;
    private String date;
    private double hours;

    /**
     * Конструктор класса TimeEntry.
     *
     * @param uuid UUID сотрудника
     * @param lastName Фамилия сотрудника
     * @param firstName Имя сотрудника
     * @param middleName Отчество сотрудника
     * @param date Дата списания
     * @param hours Количество списанных часов
     */
    public TimeEntry(String uuid, String lastName, String firstName, String middleName, String date, double hours) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.date = date;
        this.hours = hours;
    }

    /**
     * Парсит строку в объект TimeEntry.
     *
     * @param line строка с данными о списании времени
     * @return объект TimeEntry
     */
    public static TimeEntry parse(String line) {
        String[] parts = line.split("\\s+");
        String uuid = parts[0];
        String lastName = parts[1];
        String firstName = parts[2];
        String middleName = parts[3];
        String date = parts[4];
        double hours = Double.parseDouble(parts[5]);
        return new TimeEntry(uuid, lastName, firstName, middleName, date, hours);
    }

    /**
     * Возвращает полное имя сотрудника в формате "Фамилия И.О.".
     *
     * @return полное имя сотрудника
     */
    public String getFullName() {
        return String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
    }

    /**
     * Возвращает количество списанных часов.
     *
     * @return количество списанных часов
     */
    public double getHours() {
        return hours;
    }
}