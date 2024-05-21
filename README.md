# Тестовое задание для отбора на Летнюю ИТ-школу КРОК по разработке

## Условие задания
Будучи сотрудником департамента внутренней автоматизации одной IT компании, вы занимаетесь разработкой и интеграцией различных решений с целью автоматизации различных внутренних бизнес процессов компании. Так, например, в свое время в вашем департаменте была разработана система учета проектных трудозатрат.

Идеологически такая система содержит функциональность по вводу и работе со сведениями о проектах и, самое главное, управление фактическими трудозатратами – списаниями времени. Это значит, что при выполнении той или иной проектной задачи сотрудник фиксирует затраченное на решение задачи время в данной системе. В результате менеджмент проектов может оценивать потраченное время и, как следствие, деньги на реализацию проекта: по задачам, этапам и любому другому уровню детализации.

Что еще важно знать о списаниях времени. У каждого сотрудника компании есть норма списания - количество времени, которое необходимо списывать за рабочий день. В общем случае (и в рамках задачи делаем допущение, что во всех случаях) такая норма составляет 8 часов в день. Однако сотрудники по тем или иным причинам могут списывать как больше 8 часов, так и меньше, в результате чего образуется дизбаланс. 

В первом случае можно говорить о том, что сотрудник перегружен или тратит на задачи больше времени, чем планируется, но, желая успеть в срок, задерживается на работе. Во втором случае причиной может быть как простой (отсутствие задач), так и недоработки со стороны самого сотрудника. 

Дизбаланс списаний стал серьезно волновать руководителей производственных департаментов, поэтому было принято решение на регулярной основе мониторить дизбаланс и принимать соответствующие меры. Однако за неимением такого отчета в системе, руководство департамента обратилось к вам с просьбой реализовать MVP подобного отчета.

На текущий момент система управления содержит функцию выгрузки списаний “TSR-3” (присвоенный отчету код) за указанный период в следующем формате:

"_<id сотрудника в формате UUID> <Фамилия> <Имя> <Отчество> <Дата списания в формате dd.MM.YYYY> <количество часов>_"

При этом стоит учесть, что количество списаний сотрудником не ограничивается. Так, например, файл может выглядеть следующим образом:
```
a30b4d51-11b4-49b2-b356-466e92a66df7 Иванов Иван Иванович 16.05.2024 4
0f0ccd5e-cd46-462a-b92d-69a6ff147465 Петров Алексей Сергеевич 16.05.2024 6
a30b4d51-11b4-49b2-b356-466e92a66df7 Иванов Иван Иванович 16.05.2024 2.5
0f0ccd5e-cd46-462a-b92d-69a6ff147465 Петров Алексей Сергеевич 16.05.2024 2
a30b4d51-11b4-49b2-b356-466e92a66df7 Иванов Иван Иванович 16.05.2024 1.5
```

По данному примеру видно, что оба сотрудника (Петров А.С. и Иванов И.И.) списали за 16 мая каждый по 8 часов, но, скорее всего, в разные задачи.

Вам же необходимо разработать программу для анализа дизбаланса, технические требования к разработке которой содержат следующее:
1. На вход программа для анализа получает файл со списаниями report.txt за конкретную рабочую неделю. О формате файла известно следующее:
    - в первой строке файла указывается недельная норма списания на одного сотрудника (из расчета дневной нормы списания, равной 8 часам; например, 40 для пятидневной рабочей недели);
    - во всех последующих строках файла указывается перечень списаний сотрудников в формате отчета “TSR-3”. 
2. Программа определяет сотрудников, у которых дизбаланс списаний времени за неделю составляет более 10% в обе стороны;
3. Найденные сотрудники с дизбалансом по итогам работы программы записываются в текстовый файл result.txt в следующем виде:
  "_<Фамилия И.О.> <количество часов, составляющих дизбаланс, с указанием знака>_".
  Количество часов дизбаланса сопровождается знаками “+” и “–” в соответствии со следующими условиями:
    - для часов, которые идут сверх нормы, ставится знак “+”;
    - для часов, недостающих до нормы, ставится знак “–”.
4. В выходном файле сначала перечисляются сотрудники с отрицательным дизбалансом в алфавитном порядке, затем - сотрудники с положительным дизбалансом в алфавитном порядке.
5. Ручной ввод пути к файлу через интерактивный режим (посредством клавиатурного ввода) недопустим. Также эксплуатация программы не должна подразумевать какие-либо изменения программного кода с целью указания пути к файлам.

Примеры входного и выходного файлов приложены к настоящему техническому заданию.

# Анализ Дизбаланса Списаний Времени

## Автор решения
**ФИО**: Шилович Платон Юрьевич

## Описание реализации

Программа для анализа дизбаланса списаний времени за рабочую неделю. Программа считывает данные из входного файла `report.txt`, рассчитывает дизбаланс времени для каждого сотрудника и сохраняет результаты в выходной файл `result.txt`.

### Основные этапы реализации:

1. **Чтение данных из файла**:
   - Функция `read_report(file_path)` читает входной файл `report.txt`, извлекает недельную норму списаний и записи о списаниях сотрудников. Входной файл должен содержать информацию в формате:
     ```
     <недельная норма>
     <id сотрудника> <Фамилия> <Имя> <Отчество> <Дата списания> <количество часов>
     ```
   - Первая строка файла указывает недельную норму списаний, а последующие строки содержат записи о списаниях сотрудников.

2. **Вычисление дизбаланса**:
   - Функция `calculate_disbalance(weekly_norm, records)` обрабатывает записи, суммирует часы для каждого сотрудника и вычисляет дизбаланс (разницу между фактическими списаниями и недельной нормой). Дизбаланс считается значительным, если он превышает 10% от недельной нормы.
   - Используются два словаря: `employee_hours` для хранения суммарных часов и `employee_info` для хранения информации о сотрудниках (ФИО).

3. **Запись результатов в файл**:
   - Функция `write_result(disbalance, employee_info, output_file)` сортирует сотрудников по фамилии и записывает их в выходной файл `result.txt` в формате:
     ```
     <Фамилия И.О.> <количество часов>
     ```
   - Сначала записываются сотрудники с отрицательным дизбалансом, затем - с положительным.

### Структура кода:

- `read_report(file_path)`: Чтение входного файла и извлечение данных.
- `calculate_disbalance(weekly_norm, records)`: Вычисление дизбаланса времени для каждого сотрудника.
- `write_result(disbalance, employee_info, output_file)`: Запись результатов в выходной файл.
- `main()`: Основная функция, управляющая процессом выполнения.

## Инструкция по сборке и запуску

### Требования:

- Python 3.x

### Запуск программы:

1. **Клонирование репозитория**:
   ```bash
   git clone https://github.com/PlatonShilovich/school2024-test-task6.git
   cd school2024-test-task6
1. **Запуск программы**:
   ```bash
   python main.py
