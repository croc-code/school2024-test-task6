package solution;

/**
 * Класс, представляющий информацию о дисбалансе для сотрудника.
 */
public class DisbalanceInfo {
    private String fullName;
    private double disbalance;

    /**
     * Конструктор класса DisbalanceInfo.
     *
     * @param fullName полное имя сотрудника
     * @param disbalance дисбаланс для сотрудника
     */
    public DisbalanceInfo(String fullName, double disbalance) {
        this.fullName = fullName;
        this.disbalance = disbalance;
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
    public double getDisbalance() {
        return disbalance;
    }

    /**
     * Переопределение метода toString для форматированного вывода информации о дисбалансе.
     *
     * @return строковое представление информации о дисбалансе
     */
    @Override
    public String toString() {
        if(disbalance > 0) {
            return String.format("%s +%.1f%n", fullName, disbalance);
        }
        return String.format("%s %.1f%n", fullName, disbalance);
    }


}
