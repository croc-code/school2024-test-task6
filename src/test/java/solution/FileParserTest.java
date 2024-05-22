package solution;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тестовые случаи для класса {@link FileParser}.
 */
class FileParserTest {

    /**
     * Тестовый случай для проверки конструктора {@link FileParser#FileParser}.
     */
    @Test
    void testFileParserConstructor() {
        // Тестовые данные
        List<String> lines = Arrays.asList("40",
                "sdfasfasfadf231 Doe John A. 06.05.2024 10.0",
                "sdfasfasfadf231 Smith Alice B. 06.05.2024 20.5");
        // Создание объекта FileParser
        FileParser fileParser = new FileParser(lines);
        // Проверка, что недельная норма равна 40
        assertEquals(40, fileParser.getWeeklyNorm());
        //Данные для сравнения
        List<TSREntity> expectedEntries = Arrays.asList(
                new TSREntity("Doe", "John", "A", 10.0),
                new TSREntity("Smith", "Alice", "B", 20.5)
        );
        List<TSREntity> actualEntries = fileParser.getEntities();
        // Сравнение содержимого
        assertEquals(expectedEntries.size(), actualEntries.size());
        for (int i = 0; i < expectedEntries.size(); i++) {
            TSREntity expectedEntity = expectedEntries.get(i);
            TSREntity actualEntity = actualEntries.get(i);
            assertEquals(expectedEntity.getFullName(), actualEntity.getFullName());
            assertEquals(expectedEntity.getHours(), actualEntity.getHours());
        }
    }



}