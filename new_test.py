import os
'''Запишем в переменную путь к директории, в которой запускается программа для того, чтобы считывать предварительно помещенный в нее входной файл и поместить выходной'''
path = os.path.dirname(os.path.realpath(__file__)).replace('\\', '\\\\')
'''Открываем входной файл и записываем информацию построчно в список'''
with open(path + '\\\\report.txt', encoding='UTF-8') as file:
    report = [line.strip('\n') for line in file] 
'''Запишем в переменную величину недельной нормы (первую строчку входного файла) в часах'''
weekly_norm = int(report[0])
split_list = []

'''Разобъем оставшуюся часть файла в виде вложенных списков, каждый из которых будет соответствовать колонке исходной "таблицы" входного файла"'''
for i in report[1:]:
    split_list.append(i.split())
'''Создадим словарь соответсвия уникального номера сотрудника и его имени в формате, требуемом для вывода'''
id_name_dict = {i[0]: f'{i[1]} {i[2][0]}. {i[3][0]}.' for i in split_list}

report_dict = {}
'''Сгруппируем сумму часов работы по уникальному номеру, тем самым получим "таблицу", связывающую уникальный номер сотрудника и сумму его часов работы за неделю'''
for i in split_list:
    if i[0] not in report_dict:
        report_dict[i[0]] = float(i[-1])
    else:
        report_dict[i[0]] += float(i[-1])

'''Создадим словарь, в котором заменим "столбец" времени работы на столбец разницы этого времени и недельной нормы. В него мы будем добавлять только тех сотрудников,
разница которых проходит проверку на отклонение в 10 процентов
'''
result_id_dict = {}

for i in report_dict:
    if report_dict[i]/weekly_norm > 1.1 or report_dict[i]/weekly_norm < 0.9:
        result_id_dict[i] = report_dict[i] - weekly_norm


'''Создадим два результирующих списка для тех, чья разница отрицательная, и для тех, чья положительная'''
result_name_difference_minus = []
result_name_difference_plus = []
'''Заполним их строками требуемого формата, пробежавшись по словарю соответствия уникального номера и разницы'''
for i in result_id_dict:
    if result_id_dict[i] < 0:
        result_name_difference_minus.append(f'{id_name_dict[i]} {result_id_dict[i]}')
    if result_id_dict[i] >= 0:
        result_name_difference_plus.append(f'{id_name_dict[i]} +{result_id_dict[i]}')
'''Осортируем каждый из списков по алфавиту (в данном случае получится по ФИО) и сложим два списка один за другим, чтобы сначала шли сотрудники с отрицательной разницей'''
result_name_difference_plus = sorted(result_name_difference_plus)
result_name_difference_minus = sorted(result_name_difference_minus)

result_name_difference = result_name_difference_minus + result_name_difference_plus


'''Сформируем выходной файл''' 
with open(path + '\\\\result.txt', 'w') as file:
    file.write('\n'.join(result_name_difference))
