import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TimeBalanceAnalyzer {
    private int weeklyNorm;
    private final Map<String, Double> timeSpent;
    private final Map<String, String> employeeNames;

    public TimeBalanceAnalyzer() {
        this.weeklyNorm = 0;
        this.timeSpent = new HashMap<>();
        this.employeeNames = new HashMap<>();
    }

    /**
     * Анализирует разницу списания времени сотрудниками за неделю и записывает результат в файл.
     *
     * @param filename имя файла, содержащего списки списаний времени сотрудников за неделю
     */
    public void analyzeTimeBalance(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            weeklyNorm = readWeeklyNorm(reader);

            readTimeEntries(reader, timeSpent, employeeNames);
            Map<String, Double> imbalance = calculateImbalance(timeSpent, weeklyNorm);
            writeResults("result.txt", imbalance, employeeNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readWeeklyNorm(BufferedReader reader) throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    private void readTimeEntries(BufferedReader reader, Map<String, Double> timeSpent, Map<String, String> employeeNames) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            processTimeEntry(line, timeSpent, employeeNames);
        }
    }

    private void processTimeEntry(String line, Map<String, Double> timeSpent, Map<String, String> employeeNames) {
        String[] parts = line.split(" ");
        String employeeId = parts[0];
        String employeeName = formatEmployeeName(parts[1], parts[2], parts[3]);
        double hoursSpent = Double.parseDouble(parts[parts.length - 1]);

        timeSpent.put(employeeId, timeSpent.getOrDefault(employeeId, 0.0) + hoursSpent);
        employeeNames.put(employeeId, employeeName);
    }

    private String formatEmployeeName(String lastName, String firstName, String middleName) {
        return lastName + " " + firstName.charAt(0) + "." + middleName.charAt(0) + ".";
    }

    /**
     * Вычисляет дисбаланс списания времени сотрудниками.
     *
     * @param timeSpent  мапа списанных временных часов сотрудниками
     * @param weeklyNorm недельная норма списания времени на сотрудника
     * @return мапа дисбаланса списания времени сотрудников
     */
    private Map<String, Double> calculateImbalance(Map<String, Double> timeSpent, int weeklyNorm) {
        Map<String, Double> imbalance = new HashMap<>();
        for (Map.Entry<String, Double> entry : timeSpent.entrySet()) {
            double hours = entry.getValue();
            double normDiff = hours - weeklyNorm;
            if (Math.abs(normDiff) > (weeklyNorm * 0.1)) {
                imbalance.put(entry.getKey(), normDiff);
            }
        }
        return imbalance;
    }

    //запись в файл
    private void writeResults(String filename, Map<String, Double> imbalance, Map<String, String> employeeNames) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            TreeMap<String, Double> sortedImbalance = sortImbalanceByName(imbalance, employeeNames);

            //с отрицательной разницей
            for (Map.Entry<String, Double> entry : sortedImbalance.entrySet()) {
                if (entry.getValue() < 0) {
                    writer.write(employeeNames.get(entry.getKey()) + " " + entry.getValue() + "\n");
                }
            }

            // с положительной разницей
            for (Map.Entry<String, Double> entry : sortedImbalance.entrySet()) {
                if (entry.getValue() > 0) {
                    writer.write(employeeNames.get(entry.getKey()) + " +" + entry.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TreeMap<String, Double> sortImbalanceByName(Map<String, Double> imbalance, Map<String, String> employeeNames) {
        TreeMap<String, Double> sortedImbalance = new TreeMap<>((id1, id2) -> {
            String name1 = employeeNames.get(id1);
            String name2 = employeeNames.get(id2);
            return name1.compareTo(name2);
        });
        sortedImbalance.putAll(imbalance);
        return sortedImbalance;
    }
}
