import reporting.DisbalanceReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class Main {

    private final static String PATH_TO_INPUT_FILE = "src/resources/report.txt";

    private final static String PATH_TO_OUTPUT_FILE = "src/resources/result.txt";

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_INPUT_FILE))) {
            int normOfHours = parseInt(reader.readLine());
            DisbalanceReport disbalanceReport = new DisbalanceReport(reader.lines(), normOfHours);
            disbalanceReport.write(PATH_TO_OUTPUT_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
