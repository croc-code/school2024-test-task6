package ru.croc.school_test.model;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class Employee {

    private final String lastName;  // фамилия

    private final String firstName;  // имя

    private final String nameByFather;  // отчество

    private double hoursThisWeek;  // количество списанных (проработанных) часов

    private double hoursImbalance;  // дисбаланс по сравнению с недельной нормой

    public Employee(String lastName, String firstName, String nameByFather, double hoursThisWeek) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.nameByFather = nameByFather;
        this.hoursThisWeek = hoursThisWeek;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNameByFather() {
        return nameByFather;
    }

    public double getHoursThisWeek() {
        return hoursThisWeek;
    }

    public double getHoursImbalance() {
        return hoursImbalance;
    }

    public void setHoursImbalance(double hoursImbalance) {
        this.hoursImbalance = hoursImbalance;
    }

    @Override
    public String toString() {
        // флаг "+" требует вывода знака числа (+ или -)
        // если у дисбаланса нули в дробной части, то оно выводится как целое число (5 вместо 5.00)
        String formatImbalanceOutput = (hoursImbalance % 1 == 0) ? "%+.0f" : "%+.2f";

        // вывод в формате <Фамилия И.О.> <дисбаланс с указанием знака>
        // Locale.ROOT позволяет выводить точку вместо запятой у числа double
        return String.format("%s %s.%s. %s",
                lastName,
                firstName.charAt(0),
                nameByFather.charAt(0),
                String.format(Locale.ROOT, formatImbalanceOutput, hoursImbalance));
    }

    public void increaseAmountOfHours(double hours) {
        this.hoursThisWeek += hours;
    }

}
