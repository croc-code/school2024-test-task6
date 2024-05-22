package solution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс FileHandler отвечает за чтение и запись строк из файлов.
 */
public class FileHandler {

    /**
     * Читает строки из файла "report.txt" и возвращает их в виде списка строк.
     *
     * @return Список строк, содержащий прочитанные строки из файла.
     * @throws IOException Если файл "report.txt" не найден или возникла ошибка при чтении файла.
     */
    public List<String> readLines() throws IOException {
        return Files.readAllLines(Paths.get("report.txt"));
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
