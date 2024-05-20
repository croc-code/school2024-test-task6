package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Employee {

    private final String fullName;
    private final LocalDate date;
    private float hours;
    private final String uuid;

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public String getUuid() {
        return uuid;
    }

    Employee(String... el) {
        this.uuid = el[0];
        this.fullName = String.join(" ", el[1], el[2], el[3]);
        this.date = LocalDate.parse(el[4], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        this.hours = Float.parseFloat(el[5]);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Employee employee = (Employee) object;
        return Objects.equals(fullName, employee.fullName) && Objects.equals(uuid, employee.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, uuid);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "fullName='" + fullName + '\'' +
                ", date='" + date + '\'' +
                ", hours='" + hours + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
