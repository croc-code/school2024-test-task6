

class Employee:
    def __init__(self, id, s_name, f_name, m_name):
        self.id = id
        self.f_name = f_name
        self.m_name = m_name
        self.s_name = s_name
        self.hours = {}
    """Инициализирует сотрудника:
        id - айди сотрудника в компании
        s_name - фамилия сотрудника
        m_name - отчество сотрудника
        f_name - имя сотрудника
        hours - количество отработанных часов в неделю"""




    def add_hours(self, date, hours):
        """Добавялет количество часов в определенную дату"""
        if date in self.hours:
            self.hours[date] += hours
        else:
            self.hours[date] = hours

    def sum_hours(self):
        """Возвращает количество отработанных часов в неделю"""
        return sum(self.hours.values())

    def get_full_name(self):
        """Возвращает ФИО сотрудника в формате Фамилия И.О."""
        return f"{self.s_name} {self.f_name[0]}.{self.m_name[0]}."
