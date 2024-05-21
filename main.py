from organization import Employee

def import_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print(f"Файл {filename} не найден.")
    try:
        week_norm = int(lines[0])
    except ValueError:
        raise ValueError("Первая строка файла должна содержать числовое значение недельной нормы списания.")

    record = lines[1:]
    employees = {}

    for str in record:
        emp = str.strip().split()
        if len(emp) != 6:
            continue
        id, s_name, f_name, m_name, date, hours_str = emp
        hours = float(hours_str)

        if id not in employees:
            employees[id] = Employee(id, s_name, f_name, m_name)

        employees[id].add_hours(date, hours)
    return week_norm, employees


def calc(week_norm, employees):

    disbalance = week_norm * 0.1
    wrong = {}

    for emp in employees.values():
        total = emp.sum_hours()
        if abs(total - week_norm) > disbalance:
            wrong[emp.get_full_name()] = total - week_norm
    return wrong


def output(wrong, file_path):
    negative = {}
    positive = {}

    for name, hours in wrong.items():
        if hours < 0:
            negative[name] = hours
        elif hours > 0:
            positive[name] = hours

    with open(file_path, 'w', encoding='utf-8') as file:
        for name in sorted(negative):
            file.write(f"{name} {negative[name]}\n")
        for name in sorted(positive):
            file.write(f"{name} {positive[name]}\n")


def main():
    report = 'report.txt'
    result = 'result.txt'

    week_norm, employees = import_file(report)
    disbalance = calc(week_norm, employees)
    output(disbalance, result)


if __name__ == "__main__":
    main()