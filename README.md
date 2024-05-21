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

## Автор решения: Мальцева И.С.

## Описание реализации: 
Открываем файл report.txt для чтения данных, открываем файл result.txt для записи результатов.
Считываем первую строку из файла report.txt, содержащую недельную норму списания, и преобразуем её в число.
Читаем все оставшиеся строки из файла и разбиваем каждую строку на части по пробелам, добавляя их в список.
Создаем два словаря: один для хранения общего времени списаний каждого сотрудника (hours), другой для хранения фамилии и инициалов сотрудников (fio).
Проходим по каждой строке из файла и преобразуем элементы строки в строки.
Если фамилия сотрудника уже есть в словаре fio, добавляем к текущему значению количество списанных часов, если фамилии нет, создаем новую запись в словаре.
Формируем строку с фамилией и инициалами для словаря fio.
Проходим по ключам словаря hours, где сначала проверяем, превышает ли количество списанных часов норму более чем на 10% и добавляем запись с положительным знаком и количеством часов в список answer, затем проверяем, меньше ли количество списанных часов нормы более чем на 10% и добавляем запись с отрицательным знаком и количеством часов в список answer.
Сортируем список answer сначала по длине подстроки со знаком (чтобы сначала шли отрицательные значения), затем по фамилии с инициалами в алфавитном порядке.
Проходим по списку answer и записываем результаты в файл result.txt в требуемом формате.
Закрываем файлы report.txt и result.txt для завершения работы с ними.

## Инструкция по сборке и запуску решения: 
Убедитесь, что у вас установлен Python 3, создайте файл report.txt в той же директории, где будет находиться файл с решением - solution.py. 
Требования к формату данных в файле report.txt:
    в первой строке файла указывается недельная норма списания на одного сотрудника (из расчета дневной нормы списания, равной 8 часам; например, 40 для пятидневной рабочей недели);
    во всех последующих строках файла указывается перечень списаний сотрудников в формате отчета “TSR-3”.
Откройте и запустите файл solution.py в удобной для Вас среде разработки.
