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

## Автор решения
Полянин Никита Игоревич

## Описание реализации
### Входные данные
Программа принимает на вход файл report.txt, содержащий информацию о списаниях сотрудников за конкретную рабочую неделю. Файл состоит из строк, где:
В первой строке указывается недельная норма списания на одного сотрудника (например, 24 часа).
В последующих строках указывается информация о списаниях сотрудников в формате:
<UUID сотрудника> <Фамилия> <Имя> <Отчество> <Дата списания> <количество часов>

### Выходные данные
Программа генерирует файл result.txt, в котором перечисляются сотрудники с дизбалансом списаний более 10% от недельной нормы, в формате:
<Фамилия И.О.> <количество часов, составляющих дизбаланс, с указанием знака>
Сначала перечисляются сотрудники с отрицательным дизбалансом в алфавитном порядке, затем - сотрудники с положительным дизбалансом в алфавитном порядке.

### Порядок работы программы
1. Чтение данных из файла:
Программа считывает файл report.txt и сохраняет данные в список строк.
Первая строка файла содержит недельную норму списаний, которая сохраняется в переменную weeklyNorm.

2. Анализ данных:
Создаются два словаря: employeeHours для хранения суммарного времени списаний каждого сотрудника и employeeNames для хранения имен сотрудников.
Программа проходит по строкам файла, начиная со второй строки, разбивает каждую строку на части и обновляет информацию в словарях.
Каждая строка содержит идентификатор сотрудника, его фамилию, имя, отчество, дату списания и количество часов.
3. Вычисление дизбаланса:
Программа проходит по словарю employeeHours, вычисляя суммарное время списаний для каждого сотрудника и сравнивая его с недельной нормой.
Вычисляется дизбаланс (разница между фактически списанными часами и нормой) и его процентное значение относительно нормы.
Если процентное значение дизбаланса превышает 10%, сотрудник добавляется в список для выходного файла: либо underBalanced (для отрицательного дизбаланса), либо overBalanced (для положительного дизбаланса).

4. Формирование выходного файла:
Списки underBalanced и overBalanced сортируются в алфавитном порядке.
Содержимое этих списков записывается в файл result.txt.

## Инструкция по сборке и запуску решения
### Требования
Java Development Kit (JDK): Убедитесь, что у вас установлен JDK версии 8 или выше. Вы можете скачать его с официального сайта Oracle.
Текстовый редактор или среда разработки (IDE): Вы можете использовать любой текстовый редактор (например, Notepad++) или среду разработки (например, IntelliJ IDEA, Eclipse).

### Шаги по сборке и запуску

1. Скачайте репозиторий.

2. В папке "resources" зайдите в файл report.txt и введите туда данные формата:
   <UUID сотрудника> <Фамилия> <Имя> <Отчество> <Дата списания> <количество часов>

3. Затем запустите файл Main и дождитесь окончания работы программы. После этого,
   в файле result.txt появится результат выполненной работы формата:
   <Фамилия И.О.> <количество часов, составляющих дизбаланс, с указанием знака>.

