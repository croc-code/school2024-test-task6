package com.example.service.impl;

import com.example.entity.Employee;
import com.example.entity.EmployeeFileRecord;
import com.example.service.EmployeeTimeManager;
import com.example.service.FileManager;
import com.example.service.ReportManager;
import com.example.util.EmployeeRecordUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class ReportManagerImpl implements ReportManager {

    private EmployeeTimeManager employeeTimeManager;
    private final FileManager fileManager;
    private final float imbalancePercent;

    public ReportManagerImpl(float imbalancePercent) {
        this.imbalancePercent = imbalancePercent;
        this.fileManager = new FileManagerImpl();
    }

    @Override
    public void loadDataFromFile(String filePath) throws Exception {
        try {
            var data = fileManager.readLinesFromReportFile(filePath);

            if (data.isEmpty()) {
                throw new Exception("File is empty");
            } else if (data.size() == 1) {
                throw new Exception("No records found in file");
            }

            var weeklyHoursWork = Float.parseFloat(data.getFirst());
            this.employeeTimeManager = new EmployeeTimeManagerImpl(weeklyHoursWork, imbalancePercent);
            var records = EmployeeRecordUtil.parseEmployeeRecords(
                    data.subList(1, data.size()));
            loadEmployeesToManager(records);

        } catch (IOException e) {
            throw new Exception("File not found: " + filePath);
        } catch (NumberFormatException e) {
            throw new Exception("Unknown number format!");
        } catch (ParseException e) {
            throw new Exception("Error while parsing file!");
        }
    }

    private void loadEmployeesToManager(List<EmployeeFileRecord> records) throws ParseException {
        for (var record: records) {
            if (employeeTimeManager.containsEmployee(record.uuid())) {
                employeeTimeManager.addHoursToEmployee(record.uuid(), record.hoursWorked());
            } else {
                var employee = new Employee(record.uuid(),
                        record.name(), record.surname(), record.patronymic(),
                        record.hoursWorked());
                employeeTimeManager.addEmployee(employee);
            }
        }
    }

    @Override
    public void writeImbalanceToFile(String filePath) throws Exception {
        try {
            var imbalances = employeeTimeManager.getEmployeesImbalance();
            var lines = EmployeeRecordUtil.employeesImbalancesToLines(imbalances);
            fileManager.writeLinesToResultFile(filePath, lines);
        } catch (IOException e) {
            throw new Exception("Error while writing to File");
        }
    }
}
