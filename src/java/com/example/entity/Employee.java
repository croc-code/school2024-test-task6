package com.example.entity;

public class Employee {

    private final String uuid;
    private final String name;
    private final String surname;
    private final String patronymic;
    private float hoursWorked;

    public Employee(String uuid, String name, String surname, String patronymic, float hoursWorked) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.hoursWorked = hoursWorked;
    }

    public Employee(Employee employee) {
        this.uuid = employee.uuid;
        this.name = employee.name;
        this.surname = employee.surname;
        this.patronymic = employee.patronymic;
        this.hoursWorked = employee.hoursWorked;
    }

    public String getName() {
        return name;
    }

    public String getUUID() {
        return uuid;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public float getHoursWorked() {
        return hoursWorked;
    }

    public void addHoursWorked(float hoursWorked) {
        this.hoursWorked += hoursWorked;
    }
}
