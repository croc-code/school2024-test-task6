package src;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ProgramTests {

    @Test
    public void testProcessLines() {
        List<String> lines = Arrays.asList(
                "24",
                "a30b4d51-11b4-49b2-b356-466e92a66df7 Иванов Иван Иванович 06.05.2024 8",
                "0f0ccd5e-cd46-462a-b92d-69a6ff147465 Петров Алексей Сергеевич 06.05.2024 6",
                "75cd02d8-6b1f-42fb-9c59-7db675b28a2d Сорокина Анна Павловна 06.05.2024 8.5"
        );

        Map<String, Double> employeeHours = new HashMap<>();
        Map<String, String> employeeNames = new HashMap<>();

        FileManager.processLines(lines, employeeHours, employeeNames);

        assertEquals(3, employeeHours.size());
        assertEquals(3, employeeNames.size());
        assertEquals(8.0, employeeHours.get("a30b4d51-11b4-49b2-b356-466e92a66df7"));
        assertEquals("Иванов И.И.", employeeNames.get("a30b4d51-11b4-49b2-b356-466e92a66df7"));
    }

    @Test
    public void testAnalyzeDisbalance() {
        Map<String, Double> employeeHours = new HashMap<>();
        employeeHours.put("a30b4d51-11b4-49b2-b356-466e92a66df7", 20.0);
        employeeHours.put("0f0ccd5e-cd46-462a-b92d-69a6ff147465", 26.0);
        employeeHours.put("75cd02d8-6b1f-42fb-9c59-7db675b28a2d", 30.0);

        Map<String, String> employeeNames = new HashMap<>();
        employeeNames.put("a30b4d51-11b4-49b2-b356-466e92a66df7", "Иванов И.И.");
        employeeNames.put("0f0ccd5e-cd46-462a-b92d-69a6ff147465", "Петров А.С.");
        employeeNames.put("75cd02d8-6b1f-42fb-9c59-7db675b28a2d", "Сорокина А.П.");

        int weeklyNorm = 24;

        List<String> result = FileManager.analyzeDisbalance(weeklyNorm, employeeHours, employeeNames);

        assertEquals(2, result.size());
        assertEquals("Иванов И.И. -4,0", result.get(0));
        assertEquals("Сорокина А.П. +6,0", result.get(1));
    }
}
