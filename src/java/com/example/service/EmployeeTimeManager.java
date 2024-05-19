package com.example.service;

import com.example.entity.Employee;
import com.example.entity.EmployeeHoursImbalance;

import java.util.List;

public interface EmployeeTimeManager {

    void addEmployee(Employee employee);

    boolean containsEmployee(String uuid);

    void addHoursToEmployee(String employeeUUID, float hours);

    List<EmployeeHoursImbalance> getEmployeesImbalance();
}
