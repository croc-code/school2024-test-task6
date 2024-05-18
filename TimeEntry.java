public class TimeEntry {
    private String uuid;
    private String lastName;
    private String firstName;
    private String middleName;
    private String date;
    private double hours;

    public TimeEntry(String uuid, String lastName, String firstName, String middleName, String date, double hours) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.date = date;
        this.hours = hours;
    }

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

    public String getFullName() {
        return String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
    }

    public double getHours() {
        return hours;
    }
}