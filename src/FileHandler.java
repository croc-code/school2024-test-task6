import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileHandler {

    public static List<Employee> readFile(Path readPath) throws IOException {
        //два набора stream необходимы, т.к. каждый из них можно использовать лишь один раз.
        Stream<String> dataLines = Files.lines(readPath);
        Stream<String> dataLines2 = Files.lines(readPath);
        List<String> normalTimeString = dataLines2.toList();
        TimeAnalyzer.timeNorm = Integer.parseInt(normalTimeString.get(0));

        //преобразование данных из файла в record Employee
        return dataLines.skip(1).map(
                (String employeeString) -> {
                    String[] empData = employeeString.split(" ");
                    return new Employee(empData[0], empData[1], empData[2], empData[3], Double.parseDouble(empData[5]));
                }).toList();

    }


    public static void writeFile(List<String> sortedData, String writePath){
        try (BufferedWriter writer = new BufferedWriter (new FileWriter(writePath))){
            for (String data:sortedData) {
                writer.write(data);
                writer.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}