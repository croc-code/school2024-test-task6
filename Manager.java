import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private BufferedReader input;
    private BufferedWriter output;

    private float plannedHours;

    private final Map<String, Worker> workerMap = new HashMap<>();

    public Manager() {
        try {
            input = Files.newBufferedReader(Paths.get("report.txt"));
            output = Files.newBufferedWriter(Paths.get("result.txt"));
        } catch (Exception ignored) {}
    }

    public void readTroughFile () {
        try {
            String line = input.readLine();
            System.out.println(line);
            plannedHours = Float.parseFloat(line);

            line = input.readLine();

            System.out.println(line);

            while (line != null) {
                String[] args = line.split(" ");
                if (workerMap.containsKey(args[0])) {
                    workerMap.get(args[0]).addHours(Float.parseFloat(args[5]));
                }
                else {
                    workerMap.put(args[0], new Worker(args[1], args[2], args[3], Float.parseFloat(args[5])));
                }
                line = input.readLine();
                System.out.println(line);
            }
        } catch (Exception ignored) {}
    }

    public void checkImbalance () {
        try {
            for (Worker worker : workerMap.values()) {
                if (worker.checkImbalance(plannedHours)) {
                    output.write(worker.toString());
                    output.newLine();
                }
            }
            output.close();
        } catch (Exception ignored) {}
    }
}