package Solution;

/**
 * Класс, представляющий дизбаланс списаний времени сотрудником.
 */
public class Disbalance {

    private String fullName;
    private double disbalance;

    /**
     * Конструктор класса Solution.Disbalance.
     *
     * @param fullName полное имя сотрудника
     * @param disbalance значение дизбаланса
     */
    public Disbalance(String fullName, double disbalance) {
        this.fullName = fullName;
        this.disbalance = disbalance;
    }

    /**
     * Возвращает полное имя сотрудника.
     *
     * @return полное имя сотрудника
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Возвращает значение дизбаланса.
     *
     * @return значение дизбаланса
     */
    public double getDisbalance() {
        return disbalance;
    }
}