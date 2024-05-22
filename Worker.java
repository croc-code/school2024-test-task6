public class Worker {
    private final String fistName;
    private final String lastName;
    private final String surname;

    private float hours;

    public Worker(String fistName, String lastName, String surname, float hours) {
        this.fistName = fistName;
        this.lastName = lastName;
        this.surname = surname;
        this.hours = hours;
    }

    public void addHours (float hours) {
        this.hours += hours;
    }

    public boolean checkImbalance (float plannedHours) {
        hours -= plannedHours;

        return Math.abs(hours) >= plannedHours * 0.1f;
    }

    public String toString () {
        String answer = lastName + " " + fistName.charAt(0) + "." + surname.charAt(0) + ". ";

        if (hours > 0) {
            answer += "+";
        }
        else {
            answer += "-";
        }

        answer += Math.abs(hours);

        return answer;
    }
}
