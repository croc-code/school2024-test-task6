package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для вычисления дисбаланса и формирования списка информации о дисбалансе.
 */
public abstract class DisbalanceCalculator {
    /**
     * Пустой приватный конструктор для того, чтобы не создавать экземпляры данного класса.
     */
    private DisbalanceCalculator() {
    }

    /**
     * Метод для вычисления дисбаланса и формирования списка информации о дисбалансе.
     *
     * @param weeklyNorm недельная норма часов
     * @param entities список сущностей TSREntity
     * @return список информации о дисбалансе
     * @throws IllegalArgumentException если недельная норма равна нулю
     */
    public static List<DisbalanceInfo> calculateDisbalances(int weeklyNorm, List<TSREntity> entities) {

        if (weeklyNorm == 0) {
            throw new IllegalArgumentException("Weekly norm cannot be zero.");
        }
        // Вычисление общего количества часов для каждого сотрудника
        Map<String, Double> totalHours = entities.stream()
                .collect(Collectors.toMap(TSREntity::getFullName, TSREntity::getHours, Double::sum));

        // Формирование списка с дисбалансами отрудников
        List<DisbalanceInfo> disbalances = new ArrayList<>();
        for (Map.Entry<String, Double> entitiy : totalHours.entrySet()) {
            double disbalance = entitiy.getValue() - weeklyNorm;
            if (Math.abs(disbalance) > weeklyNorm * 0.1) {
                disbalances.add(new DisbalanceInfo(entitiy.getKey(), disbalance));
            }
        }
        // Сортировка списка по наибольшим дисбалансам сотрудников в отрецательную сторону и по алфавиту
        List<DisbalanceInfo> result = new ArrayList<>(disbalances);
        result.sort((d1, d2) -> {
            if (d1.getDisbalance() < 0 && d2.getDisbalance() < 0) {
                return d1.getFullName().compareTo(d2.getFullName());
            } else if (d1.getDisbalance() >= 0 && d2.getDisbalance() >= 0) {
                return d1.getFullName().compareTo(d2.getFullName());
            } else {
                return Double.compare(d1.getDisbalance(), d2.getDisbalance());
            }
        });
        return result;
    }
}
