package solution;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImbalanceCalculatorTest {
    /**
     * Тест для метода {@link ImbalanceCalculator#calculateImbalances(int, List)}}.
     */
    @Test
    void calculateImbalance() {
        // Тестовые данные
        List<TSREntity> entities = Arrays.asList(
                new TSREntity("Иванов", "Иван", "Иванович", 25.0),
                new TSREntity("Мэнкайнд", "Император", "Императорович", 40040.0),
                new TSREntity("Иванов", "Иван", "Иванович", 25.0),
                new TSREntity("Антонов", "Антон", "Антонович", 35.0),
                new TSREntity("Норманов", "Норман", "Нормавич", 40.0),
                new TSREntity("Мерькьюри", "Фредди", "Рокавич", 100.0)
        );

        List<ImbalanceInfo> result = ImbalanceCalculator.calculateImbalances(40, entities);

        // Проверка, что результат соответствует ожидаемому списку дисбалансов
        assertEquals(4, result.size());
        assertEquals("Антонов А.А. -5,0\r\n", result.get(0).toString());
        assertEquals("Иванов И.И. +10,0\r\n", result.get(1).toString());
        assertEquals("Мерькьюри Ф.Р. +60,0\r\n", result.get(2).toString());
        assertEquals("Мэнкайнд И.И. +40000,0\r\n", result.get(3).toString());


    }
}