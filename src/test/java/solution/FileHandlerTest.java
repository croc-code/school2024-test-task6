package solution;





import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Тесты для класса {@link FileHandler}.
 */
class FileHandlerTest {
    private  FileHandler fileHandler;

    @BeforeEach
    void setUp(){
        fileHandler = new FileHandler();
    }

    /**
     * Тест для метода {@link FileHandler#readLines()}.
     */
   @Test
    void readLines() {
       List<String> lines;
       try {
           lines = fileHandler.readLines();
           // Проверка, что список не является пустым и содержит хотя бы один элемент
           assertTrue(lines != null && !lines.isEmpty());
       } catch (IOException e) {
           // Если возникла ошибка, тест считается не пройденным
           e.printStackTrace();
           assertTrue(false);
       }
    }

    /**
     * Тест для метода {@link FileHandler#writeLines(List)}.
     */
    @Test
    void writeLines() {
        List<String> lines = List.of("Test line 1", "Test line 2", "Test line 3");
        try {
            fileHandler.writeLines(lines);
            // Если при записи не возникло ошибок, тест считается пройденным
            assertTrue(true);
        } catch (IOException e) {
            // Если возникла ошибка, тест считается не пройденным
            e.printStackTrace();
            assertTrue(false);
        }
    }
}