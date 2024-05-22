package solution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс solution.FileHandler отвечает за чтение и запись строк из файлов.
 */
public class FileHandler {

    /**
     * Читает строки из файла "report.txt" и возвращает их в виде списка строк.
     *
     * @return Список строк, содержащий прочитанные строки из файла.
     * @throws IOException Если файл "report.txt" не найден или возникла ошибка при чтении файла.
     */
    public List<String> readLines() throws IOException {
        List<String> lines;
        try (InputStream inputStream = getClass().getResourceAsStream("/report.txt")) {
            if (inputStream == null) {
                throw new IOException("File not found: report.txt");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                lines = reader.lines().toList();
            }
        }
        return lines;
    }

    /**
     * Записывает строки в файл "result.txt".
     *
     * @param lines Список строк для записи в файл.
     * @throws IOException Если произошла ошибка при записи в файл.
     */
    public void writeLines(List<String> lines) throws IOException {
        String outputFilePath = "result.txt";
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputFilePath))) {
            for (String line : lines) {
                bw.write(line);
            }
        }
    }
}
