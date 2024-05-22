package solution;

/**
 * Класс, представляющий информацию о дисбалансе для сотрудника.
 */
public class ImbalanceInfo {
    private final String fullName;
    private final double imbalance;

    /**
     * Конструктор класса ImbalanceInfo.
     *
     * @param fullName полное имя сотрудника
     * @param imbalance дисбаланс для сотрудника
     */
    public ImbalanceInfo(String fullName, double imbalance) {
        this.fullName = fullName;
        this.imbalance = imbalance;
    }
    /**
     * Метод для получения полного имени сотрудника.
     *
     * @return полное имя сотрудника
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Метод для получения значения дисбаланса для сотрудника.
     *
     * @return значение дисбаланса
     */
    public double getImbalance() {
        return imbalance;
    }

    /**
     * Переопределение метода toString для форматированного вывода информации о дисбалансе.
     *
     * @return строковое представление информации о дисбалансе
     */
    @Override
    public String toString() {
        if(imbalance > 0) {
            return String.format("%s +%.1f%n", fullName, imbalance);
        }
        return String.format("%s %.1f%n", fullName, imbalance);
    }


}
