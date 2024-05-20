from config import MAX_PERCENT_DEVIATION, INPUT_FILE, OUTPUT_FILE


def is_correct_line(line: str) -> bool:
    line = line.strip().split()
    if len(line) != 6:
        return False
    hours = line[5].replace(".", "")
    correct_hours = all(digit.isdigit() for digit in hours)
    correct_last_name = all(char.isalpha() for char in line[1])
    correct_first_name = all(char.isalpha() for char in line[2])
    correct_patronymic = all(char.isalpha() for char in line[3])
    return all((correct_last_name, correct_first_name, correct_patronymic, correct_hours))


def read_report(report_file: str) -> tuple:
    with open(report_file, "r") as report:
        working_time_standard = report.readline()
        try:
            working_time_standard = float(working_time_standard)
        except ValueError as e:
            return "Первая строка файла report.txt (норма рабочего времени) должна быть числом!!!", e

        line = report.readline()
        staff_working_hours = {}
        while line:
            if is_correct_line(line):
                uuid, last_name, first_name, patronymic, date, working_hours = line.strip().split()
                full_name = last_name + " " + first_name[0] + "." + patronymic[0] + "."
                if full_name not in staff_working_hours:
                    staff_working_hours[full_name] = 0
                staff_working_hours[full_name] += float(working_hours)
            line = report.readline()
        return working_time_standard, staff_working_hours


def check_imbalance(normal_hours: float, staff_working_hours: dict) -> list[tuple]:
    abnormal_working_hours_less_null = []
    abnormal_working_hours_more_null = []
    for name, working_hours in staff_working_hours.items():
        difference = round(working_hours - normal_hours, 3)
        normal_variation = normal_hours * MAX_PERCENT_DEVIATION
        if abs(difference) > normal_variation:
            if difference > 0:
                abnormal_working_hours_more_null.append((name, difference))
            else:
                abnormal_working_hours_less_null.append((name, difference))
    staff_less_normal = sorted(abnormal_working_hours_less_null)
    staff_more_normal = sorted(abnormal_working_hours_more_null)
    all_imbalance_staff = []
    all_imbalance_staff.extend(staff_less_normal)
    all_imbalance_staff.extend(staff_more_normal)
    return all_imbalance_staff


def write_imbalance_staff(all_staff: list[tuple],  result_file):
    with open(result_file, "w") as result:
        for employee_data in all_staff:
            username, imbalance_hours = employee_data
            if imbalance_hours % 1 == 0:
                imbalance_hours = int(imbalance_hours)

            if imbalance_hours > 0:
                result.write(" +".join((username, str(imbalance_hours))))
            else:
                result.write(" ".join((username, str(imbalance_hours))))
            result.write("\n")


if __name__ == "__main__":
    normal_time, data_of_staff = read_report(INPUT_FILE)
    if isinstance(normal_time, float) and isinstance(data_of_staff, dict):
        imbalance_staff = check_imbalance(normal_time, data_of_staff)
        write_imbalance_staff(imbalance_staff, OUTPUT_FILE)
    else:
        print(normal_time, data_of_staff, sep="\n")
