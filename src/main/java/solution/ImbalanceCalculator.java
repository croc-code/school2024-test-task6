package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для вычисления дисбаланса и формирования списка информации о дисбалансе.
 */
public abstract class ImbalanceCalculator {
    /**
     * Пустой приватный конструктор для того, чтобы не создавать экземпляры данного класса.
     */
    private ImbalanceCalculator() {
    }

    /**
     * Метод для вычисления дисбаланса и формирования списка информации о дисбалансе.
     *
     * @param weeklyNorm недельная норма часов
     * @param entities список сущностей TSREntity
     * @return список информации о дисбалансе
     * @throws IllegalArgumentException если недельная норма равна нулю
     */
    public static List<ImbalanceInfo> calculateImbalances(int weeklyNorm, List<TSREntity> entities) {

        if (weeklyNorm == 0) {
            throw new IllegalArgumentException("Weekly norm cannot be zero.");
        }
        // Вычисление общего количества часов для каждого сотрудника
        Map<String, Double> totalHours = entities.stream()
                .collect(Collectors.toMap(TSREntity::getFullName, TSREntity::getHours, Double::sum));

        // Формирование списка с дисбалансами отрудников
        List<ImbalanceInfo> imbalances = new ArrayList<>();
        for (Map.Entry<String, Double> entity : totalHours.entrySet()) {
            double imbalance = entity.getValue() - weeklyNorm;
            if (Math.abs(imbalance) > weeklyNorm * 0.1) {
                imbalances.add(new ImbalanceInfo(entity.getKey(), imbalance));
            }
        }
        // Сортировка списка по наибольшим дисбалансам сотрудников в отрецательную сторону и по алфавиту
        List<ImbalanceInfo> result = new ArrayList<>(imbalances);
        result.sort((d1, d2) -> {
            if (d1.getImbalance() < 0 && d2.getImbalance() < 0) {
                return d1.getFullName().compareTo(d2.getFullName());
            } else if (d1.getImbalance() >= 0 && d2.getImbalance() >= 0) {
                return d1.getFullName().compareTo(d2.getFullName());
            } else {
                return Double.compare(d1.getImbalance(), d2.getImbalance());
            }
        });
        return result;
    }
}
