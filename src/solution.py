from exceptions import ArgumentCountError, HoursError, CollisionError


class DataChecker:
    def check_data_general(self, contents: list):
        """Проверка на полноту переданных аргументов."""
        length = 6
        if len(contents) != length:
            raise ArgumentCountError

    def check_hours(self, hours):
        """
        Проверка на корректность введенных часов.
        """
        if not self.is_float(hours) and not hours.isdigit():
            raise HoursError

    def is_float(self, value) -> bool:
        try:
            value = float(value)
            return True
        except ValueError:
            return False

    def check_collision(self, dictionary, id, name):
        """Проверка на наличие нескольких людей с одним ID"""
        if dictionary[id][0] != name:
            raise CollisionError


class Solution:
    def __init__(self) -> None:
        self.path = "report.txt"
        self.res_path = "result.txt"
        self.data_checker = DataChecker()

    def get_line_generator(self, path: str):
        """
        Функция-генератор, выдает по одной строке файла за раз.
        """
        try:
            with open(path, "r", encoding="utf-8") as file:
                plan = file.readline().strip()
                yield plan
                for line in file:
                    yield line.strip()
        except FileNotFoundError:
            print(f"Файл не найден.")

    def process_data_loop(self, path: str) -> list[list]:
        """
        Преобразует содержимое файла в словарь, в котором
        ключом является ID, а занятость -
        [отформатированное имя, суммарная занятость за неделю]
        """
        compressed_data = {}
        line_gen = self.get_line_generator(path)
        try:
            plan = next(line_gen)
            self.data_checker.check_hours(str(plan))
            for line in line_gen:
                person_data = line.split()
                self.data_checker.check_data_general(person_data)
                formatted_data = self.format_name(
                    person_data,
                )
                identificator = formatted_data[0]
                self.data_checker.check_hours(formatted_data[-1])
                person_hours = float(formatted_data[-1])
                person_name = formatted_data[1]
                if identificator in compressed_data:
                    self.data_checker.check_collision(
                        compressed_data, identificator, person_name
                    )
                    compressed_data[identificator][1] += person_hours
                else:
                    compressed_data[identificator] = [person_name, person_hours]
            return compressed_data, float(plan)
        except HoursError as error:
            print(f"{error}")
        except ArgumentCountError as error:
            print(f"{error}")
        except CollisionError as error:
            print(f"{error}")
        return None

    def format_name(self, person_data: list) -> list:
        """
        Меняется формат имени: Surname Name Midname -> Surname N.M.
        """
        count = 3
        name_index = 1
        last_name, first_name, mid_name = (
            person_data[1],
            person_data[2][0],
            person_data[3][0],
        )
        while count > 0:
            person_data.pop(name_index)
            count -= 1
        full_name = f"{last_name} {first_name}.{mid_name}."
        person_data.insert(1, full_name)
        return person_data

    def compressed_data_formatter(self) -> list[list]:
        """
        Функция находит отклоняющиеся более чем на 10 процентов
        значения и записывает их в результат.
        """
        res = []
        if self.process_data_loop(self.path):
            compressed_data, plan = self.process_data_loop(self.path)
            for value in compressed_data.values():
                if abs((value[1] - plan) / plan) > 0.1:
                    value[1] = value[1] - plan
                    if value[1] == int(value[1]):
                        value[1] = int(value[1])
                    res.append(value)
            res = sorted(res, key=lambda x: (x[1] >= 0, x[0]))
        return res

    def main(self) -> None:
        """
        Главная функция, записывает результат в файл.
        """
        result = self.compressed_data_formatter()
        with open(self.res_path, "w", encoding="utf-8") as file:
            for row in result:
                string = " ".join(map(str, row)) + "\n"
                file.write(string)


if __name__ == "__main__":
    ex = Solution()
    ex.main()
