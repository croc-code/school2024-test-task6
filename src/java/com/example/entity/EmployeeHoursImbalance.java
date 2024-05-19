package com.example.entity;

import java.util.Locale;

public record EmployeeHoursImbalance(String fio,
                                     float imbalanceHours) {
    @Override
    public String toString() {
        if (imbalanceHours % 1 > 0.1) {
            return String.format(Locale.US, "%s %s%.1f",
                    fio, imbalanceHours < 0? "": "+", imbalanceHours);
        } else {
            return String.format("%s %s%d",
                    fio, imbalanceHours < 0? "": "+", (int) imbalanceHours);
        }
    }
}
