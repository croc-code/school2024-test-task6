import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TimeBalanceAnalyzer {
    private static final Logger LOGGER = Logger.getLogger(TimeBalanceAnalyzer.class.getName());

    private int weeklyNorm;
    private final Map<String, Double> timeSpent;
    private final Map<String, String> employeeNames;

    public TimeBalanceAnalyzer() {
        this.weeklyNorm = 0;
        this.timeSpent = new HashMap<>();
        this.employeeNames = new HashMap<>();
    }

    public void analyzeTimeBalance(String filename) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            weeklyNorm = readWeeklyNorm(reader);

            readTimeEntries(reader, timeSpent, employeeNames);
            Map<String, Double> imbalance = calculateImbalance(timeSpent, weeklyNorm);
            writeResults("result.txt", imbalance, employeeNames);
        } catch (IOException e) {
            LOGGER.severe("Error processing file " + filename + ": " + e.getMessage());
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

    private Map<String, Double> calculateImbalance(Map<String, Double> timeSpent, int weeklyNorm) {
        return timeSpent.entrySet().stream()
                .map(entry -> {
                    double hours = entry.getValue();
                    double normDiff = hours - weeklyNorm;
                    return Math.abs(normDiff) > (weeklyNorm * 0.1) ? Optional.of(Map.entry(entry.getKey(), normDiff)) : Optional.<Map.Entry<String, Double>>empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void writeResults(String filename, Map<String, Double> imbalance, Map<String, String> employeeNames) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            TreeMap<String, Double> sortedImbalance = sortImbalanceByName(imbalance, employeeNames);

            for (Map.Entry<String, Double> entry : sortedImbalance.entrySet()) {
                if (entry.getValue() < 0) {
                    writer.write(employeeNames.get(entry.getKey()) + " " + (int) Math.round(entry.getValue()) + "\n");
                }
            }

            for (Map.Entry<String, Double> entry : sortedImbalance.entrySet()) {
                if (entry.getValue() > 0) {
                    writer.write(employeeNames.get(entry.getKey()) + " +" + (int) Math.round(entry.getValue()) + "\n");
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error writing to file " + filename + ": " + e.getMessage());
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