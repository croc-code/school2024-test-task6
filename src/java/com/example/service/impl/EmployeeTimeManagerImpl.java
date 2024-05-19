package com.example.service.impl;

import com.example.entity.Employee;
import com.example.entity.EmployeeHoursImbalance;
import com.example.service.EmployeeTimeManager;

import java.util.*;
import java.util.stream.Stream;

public class EmployeeTimeManagerImpl implements EmployeeTimeManager {

    private final Map<String, Employee> employees = new HashMap<>();
    private final float weeklyHours;
    private final float imbalancePercent;

    public EmployeeTimeManagerImpl(float weeklyHours, float imbalancePercent) {
        this.weeklyHours = weeklyHours;
        this.imbalancePercent = imbalancePercent;
    }

    @Override
    public void addEmployee(Employee employee) {
        if (employee != null && !employees.containsKey(employee.getUUID())) {
            var newEmployee = new Employee(employee);
            employees.putIfAbsent(newEmployee.getUUID(), newEmployee);
        }
    }

    @Override
    public boolean containsEmployee(String uuid) {
        return employees.containsKey(uuid);
    }

    public void addHoursToEmployee(String employeeUUID, float hours) {
        if (employeeUUID != null && employees.containsKey(employeeUUID) && hours != 0f) {
            var employee = employees.get(employeeUUID);
            employee.addHoursWorked(hours);
        }
    }

    @Override
    public List<EmployeeHoursImbalance> getEmployeesImbalance() {
        List<EmployeeHoursImbalance> imbalancesNegative = new ArrayList<>();
        List<EmployeeHoursImbalance> imbalancesPositive = new ArrayList<>();

        for (Employee employee: employees.values()) {
            float imbalancePercentOfWeekHours = Math.abs(1 - employee.getHoursWorked() / weeklyHours) * 100.0f;
            if (imbalancePercentOfWeekHours >= imbalancePercent) {
                String fio = String.format("%s %c.%c.", employee.getSurname(),
                        employee.getName().charAt(0), employee.getPatronymic().charAt(0));
                float hoursWorked = employee.getHoursWorked() - weeklyHours;
                var imbalance = new EmployeeHoursImbalance(fio, hoursWorked);
                if (hoursWorked < 0) {
                    imbalancesNegative.add(imbalance);
                } else {
                    imbalancesPositive.add(imbalance);
                }
            }
        }

        imbalancesNegative.sort(Comparator.comparing(EmployeeHoursImbalance::fio));
        imbalancesPositive.sort(Comparator.comparing(EmployeeHoursImbalance::fio));

        return Stream.concat(
                imbalancesNegative.stream(),
                imbalancesPositive.stream())
                .toList();
    }
}
