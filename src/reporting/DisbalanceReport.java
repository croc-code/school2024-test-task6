package reporting;

import abstractions.Report;
import entities.Employee;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.lang.Math.abs;
import static java.lang.String.format;

public class DisbalanceReport extends Report {

    private static final Comparator<Map.Entry<Employee, Double>> comparator = Comparator
            .<Map.Entry<Employee, Double>> comparingInt(entry -> entry.getValue() < 0 ? 0 : 1)
            .thenComparing(Map.Entry::getKey);

    private static final double COEFFICIENT_OF_NORMAL_DEVIATION = 0.1;

    private double normalDeviation;
    private List<String> contentReport;

    private int normOfHours;

    public DisbalanceReport(Stream<String> timeDeductionData, int normHours) {
        this.normOfHours = normHours;
        normalDeviation = COEFFICIENT_OF_NORMAL_DEVIATION * normHours;
        Map<Employee, Double> rawReportData = new HashMap<>();
        timeDeductionData.forEach(line -> addDataToMap(line, rawReportData));
        formReportContent(rawReportData);
    }

    private void addDataToMap(String line, Map<Employee, Double> employeeWorkedTimeMap) {
        try {
            String[] data = line.split(" ");
            Employee e = new Employee(data[0], data[1], data[2], data[3]);
            if (employeeWorkedTimeMap.containsKey(e))
                employeeWorkedTimeMap.put(e, employeeWorkedTimeMap.get(e) + parseDouble(data[5]));
            else employeeWorkedTimeMap.put(e, parseDouble(data[5]) - normOfHours);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Incorrect recording of time write-off data: " + line, e);
        }
    }

    private void formReportContent(Map<Employee, Double> rawReportData) {
        contentReport = rawReportData.entrySet().stream()
                .filter(e -> abs(e.getValue()) > normalDeviation)
                .sorted(comparator)
                .map(e -> format("%s %s",
                        e.getKey(),
                        e.getValue() > 0 ? format("+%s", e.getValue()) : e.getValue())
                ).toList();
    }

    @Override
    public void write(String filepath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath))) {
            if (contentReport == null) {
                writer.newLine();
                return;
            }
            for (String line : contentReport) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getNormalDeviation() {
        return normalDeviation;
    }


    public List<String> getContentReport() {
        return List.copyOf(contentReport);
    }

    public int getNormOfHours() {
        return normOfHours;
    }

    public void setNormOfHours(int normOfHours) {
        this.normOfHours = normOfHours;
        normalDeviation = COEFFICIENT_OF_NORMAL_DEVIATION * normOfHours;
    }

    public void printReport() {
        for (String s : contentReport)
            System.out.println(s);
    }
}
