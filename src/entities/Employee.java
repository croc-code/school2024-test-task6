package entities;


import java.util.Objects;

import static java.lang.String.format;

public class Employee implements Comparable<Employee> {
    private final String UUID;
    private String name;
    private String patronymic;
    private String lastname;

    public Employee(String UUID, String lastname, String name, String patronymic) {
        this.UUID = UUID;
        this.lastname = lastname;
        this.name = name;
        this.patronymic = patronymic;
    }

    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(UUID, employee.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID);
    }

    @Override
    public String toString() {
        return format("%s %s.%s.",
                lastname,
                name.charAt(0),
                patronymic.charAt(0)
        );
    }

    @Override
    public int compareTo(Employee o) {

        int lastnameComparison = lastname.compareTo(o.lastname);
        if (lastnameComparison != 0)
            return lastnameComparison;

        int nameComparison = name.compareTo(o.name);
        if (nameComparison != 0)
            return nameComparison;

        return patronymic.compareTo(o.patronymic);
    }
}
