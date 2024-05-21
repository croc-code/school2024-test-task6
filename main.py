def analyze_report(report_file='report.txt'):
    with open(report_file, 'r', encoding='utf-8') as report:
        lines = report.readlines()
    weekly_norm = float(lines[0])

    employee_work = {}
    for line in lines[1:]:
        record = line.split()
        employee_id = record[0]
        employee_full_name = ' '.join(record[1:4])
        date_work = record[-2]
        working_hours = float(record[-1])
        print(employee_id, employee_full_name, date_work, working_hours)

        if employee_id not in employee_work:
            employee_work[employee_id] = {'full_name': employee_full_name,
                                          'total_working_hours': 0}
        employee_work[employee_id]['total_working_hours'] += working_hours

    imbalanced_workers = []
    for employee_id, record in employee_work.items():
        total_hours = record['total_working_hours']
        imbalance = total_hours - weekly_norm
        if abs(imbalance) > weekly_norm * 0.1:
            imbalanced_workers.append((record['full_name'], imbalance))

    # print(imbalanced_workers)
    negative_imbalance = sorted([employee for employee in imbalanced_workers if employee[1] < 0],
                                key=lambda x: x[0])
    positive_imbalance = sorted([employee for employee in imbalanced_workers if employee[1] >= 0],
                                key=lambda x: x[0])

    # print(negative_imbalance)
    # print(positive_imbalance)
    write_result(negative_imbalance, positive_imbalance)


def write_result(result_negative, result_positive, result_file="result.txt"):
    with open(result_file, 'w', encoding='utf-8') as file:
        for full_name, imbalance in result_negative+result_positive:
            file.write(write_string(full_name, imbalance))


def write_string(full_name, imbalance):
    full_name_split = full_name.split()
    short_name = f"{full_name_split[0]} {full_name_split[1][0]}.{full_name_split[2][0]}."
    imbalance = f"{imbalance:.0f}" if imbalance < 0 else f"+{imbalance:.0f}"
    return f"{short_name} {imbalance}\n"


if __name__ == "__main__":
    analyze_report()
