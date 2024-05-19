package com.example.util;

import com.example.entity.EmployeeFileRecord;
import com.example.entity.EmployeeHoursImbalance;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRecordUtil {

    public static List<EmployeeFileRecord> parseEmployeeRecords(List<String> lines) throws ParseException {
        List<EmployeeFileRecord> records = new ArrayList<>();

        for (var line : lines) {
            var employeeFileRecord = parseEmployeeRecord(line);
            records.add(employeeFileRecord);
        }

        return records;
    }

    private static EmployeeFileRecord parseEmployeeRecord(String line) throws ParseException {
        var data = line.split("\\s");

        if (data.length != 6) {
            throw new ParseException("Record is invalid!", data.length);
        }

        var uuid = data[0];
        var name = data[2];
        var surname = data[1];
        var patronymic = data[3];
        var hoursWorked = Float.parseFloat(data[5]);

        return new EmployeeFileRecord(uuid, name, surname, patronymic, hoursWorked);
    }

    public static List<String> employeesImbalancesToLines(List<EmployeeHoursImbalance> imbalances) {
        return imbalances.stream()
                .map(EmployeeHoursImbalance::toString)
                .collect(Collectors.toList());
    }
}
