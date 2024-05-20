def read_report(file_path):
    # Открываем файл для чтения в режиме чтения текста с кодировкой UTF-8.
    with open(file_path, 'r', encoding='utf-8') as file:
        # Читаем все строки из файла и сохраняем их в список.
        lines = file.readlines()
    # Первая строка содержит недельную норму списаний, конвертируем её в целое число.
    weekly_norm = int(lines[0].strip())
    # Создаём генератор, который будет возвращать список слов для каждой строки, начиная со второй.
    records = (line.strip().split() for line in lines[1:])
    # Возвращаем недельную норму и генератор записей.
    return weekly_norm, records


def calculate_disbalance(weekly_norm, records):
    # Создаём пустой словарь для хранения суммарных часов для каждого сотрудника.
    employee_hours = {}
    # Создаём пустой словарь для хранения информации о сотрудниках.
    employee_info = {}
    for record in records:
        # Извлекаем поля из записи.
        employee_id, surname, name, patronymic, date, hours = record[0], record[1], record[2], record[3], record[
            4], float(record[5])
        # Если employee_id отсутствует в словаре employee_hours, инициализируем его значением 0.0.
        if employee_id not in employee_hours:
            employee_hours[employee_id] = 0.0
            # Также сохраняем информацию о сотруднике в словарь employee_info.
            employee_info[employee_id] = (surname, name, patronymic)
        # Добавляем часы к суммарным часам сотрудника.
        employee_hours[employee_id] += hours

    # Порог для определения дизбаланса (10% от недельной нормы).
    threshold = weekly_norm * 0.1
    # Создаём словарь с дизбалансом, фильтруя сотрудников с разницей больше порога.
    disbalance = {employee_id: total_hours - weekly_norm
                  for employee_id, total_hours in employee_hours.items()
                  if abs(total_hours - weekly_norm) > threshold}
    # Возвращаем словарь с дизбалансом и информацию о сотрудниках.
    return disbalance, employee_info


def write_result(disbalance, employee_info, output_file):
    # Сортируем и форматируем сотрудников с отрицательным дизбалансом.
    negative_disbalance = sorted(
        (
        f"{employee_info[employee_id][0]} {employee_info[employee_id][1][0]}.{employee_info[employee_id][2][0]}. {int(hours)}"
        for employee_id, hours in disbalance.items()
        if hours < 0), key=lambda x: x.split()[0])
    # Сортируем и форматируем сотрудников с положительным дизбалансом.
    positive_disbalance = sorted(
        (
        f"{employee_info[employee_id][0]} {employee_info[employee_id][1][0]}.{employee_info[employee_id][2][0]}. +{int(hours)}"
        for employee_id, hours in disbalance.items()
        if hours > 0), key=lambda x: x.split()[0])

    # Открываем файл для записи в режиме записи текста с кодировкой UTF-8.
    with open(output_file, 'w', encoding='utf-8') as file:
        # Записываем сотрудников с отрицательным дизбалансом.
        for entry in negative_disbalance:
            file.write(f"{entry}\n")
        # Записываем сотрудников с положительным дизбалансом.
        for entry in positive_disbalance:
            file.write(f"{entry}\n")


def main():
    # Задаём имена входного и выходного файлов.
    input_file = 'report.txt'
    output_file = 'result.txt'
    # Читаем недельную норму и записи из входного файла.
    weekly_norm, records = read_report(input_file)
    # Вычисляем дизбаланс и получаем информацию о сотрудниках.
    disbalance, employee_info = calculate_disbalance(weekly_norm, records)
    # Записываем результаты в выходной файл.
    write_result(disbalance, employee_info, output_file)


# Запускаем основную функцию, если файл исполняется как скрипт.
if __name__ == "__main__":
    main()