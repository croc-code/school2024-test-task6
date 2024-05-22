package solution;

import java.io.IOException;
import java.util.List;

/**
 * Точка входа в приложение.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //Создание объекта для чтения и записифайлов
        FileHandler fileHandler = new FileHandler();
        //Создание объект парсера с передачей в конструктор данных из файла
        FileParser fileParser = new FileParser(fileHandler.readLines());
        //Формирование результата
        List<ImbalanceInfo> imbalanceInfos = ImbalanceCalculator.calculateImbalances(fileParser.getWeeklyNorm(), fileParser.getEntities());
        //Запись результата в файл
        fileHandler.writeLines(imbalanceInfos.stream().map(ImbalanceInfo::toString).toList());

    }
}
