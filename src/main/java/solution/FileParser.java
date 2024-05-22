package solution;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс FileParser выполняет парсинг списка строк для извлечения недельной нормы и записей.
 */
public class FileParser {
    /** Недельная норма, извлеченная из первой строки входных строк. */
    private final int weeklyNorm;

    /**
     * Список записей типа {@link TSREntity}, полученных в результате парсинга входных строк.
     */
    private final List<TSREntity> entities;

    /**
     * Конструктор объекта FileParser, выполняющий парсинг входных строк.
     *
     * @param lines Список строк для парсинга.
     */
    public FileParser(List<String> lines) {
        weeklyNorm = parseWeeklyNorm(lines.get(0));
        entities = parseLinesToEntities(lines);

    }

    /**
     * Парсит недельную норму из первой строки входных данных.
     *
     * @param firstLine Первая строка, содержащая недельную норму.
     * @return Парсенная недельная норма в виде целого числа.
     */
    private static int parseWeeklyNorm(String firstLine) {
        return Integer.parseInt(firstLine.trim());
    }

    /**
     * Парсит записи типа {@link TSREntity} из входных строк.
     *
     * @param lines Список строк, содержащих записи типа TSREntity.
     * @return Список парсенных записей типа {@link TSREntity}.
     */
    private static List<TSREntity> parseLinesToEntities(List<String> lines) {
        return lines
                .stream()
                .filter(line -> !line.isEmpty())
                .skip(1)
                .map(TSREntity::mapToInstance)
                .collect(Collectors.toList());
    }
    /**
     * Возвращает список записей типа {@link TSREntity}.
     *
     * @return Список записей типа {@link TSREntity}.
     */
    public List<TSREntity> getEntities() {
        return entities;
    }

    /**
     * Возвращает недельную норму.
     *
     * @return Недельная норма в виде целого числа.
     */
    public int getWeeklyNorm() {
        return weeklyNorm;
    }
}
