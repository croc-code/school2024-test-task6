import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

public class TimeBalanceAnalyzerTest {

    TimeBalanceAnalyzer analyzer;

    @BeforeEach
    public void beforeEach() {
        analyzer = new TimeBalanceAnalyzer();
    }

    @Test
    public void testAnalyzeTimeBalance() {
        String testFileName = "test_report.txt";

        createTestFile(testFileName);

        analyzer.analyzeTimeBalance(testFileName);

        File resultFile = new File("result.txt");
        assertTrue(resultFile.exists());

        try {
            String resultContent = new String(Files.readAllBytes(resultFile.toPath()));
            StringBuilder sb = new StringBuilder();
            sb.append("Иванов И.И. -3\n");
            sb.append("Петров А.С. +4\n");
            String expectedContent = sb.toString();

            assertEquals(expectedContent, resultContent);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error reading result file");
        }

        resultFile.delete();
        new File(testFileName).delete();
    }

    private void createTestFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("24\n");
            writer.write("a30b4d51-11b4-49b2-b356-466e92a66df7 Иванов Иван Иванович 06.05.2024 21\n");
            writer.write("0f0ccd5e-cd46-462a-b92d-69a6ff147465 Петров Алексей Сергеевич 06.05.2024 28\n");
            writer.write("0f0ccd5e-cd46-462a-b92d-69a6ff147463 Петров Алексей Игоревич 06.05.2024 24\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
