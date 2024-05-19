use std::convert::{From, TryFrom};
use std::hash::Hash;
use chrono::NaiveDate;

/// Сущность TSR-3 записи.
#[derive(Debug)]
#[allow(dead_code)]
pub struct Tsr3Record {
    employee_id: String,
    last_name: String,
    first_name: String,
    second_name: String,
    date: NaiveDate,
    hours: f64,
}

impl Tsr3Record {
    pub fn get_hours(&self) -> f64 {
        self.hours
    }
}

impl TryFrom<&str> for Tsr3Record {
    type Error = String;
    /// Получает строку в формате:
    /// * <id сотрудника в формате UUID> <Фамилия> <Имя> <Отчество> <Дата списания в формате dd.MM.YYYY> <количество часов>
    fn try_from(value: &str) -> Result<Self, Self::Error> {
        let mut vals = value.split(" ");

        let employee_id = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected employee_id.".to_string())
        };

        let last_name = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected last_name.".to_string())
        };

        let first_name = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected first_name.".to_string())
        };

        let second_name = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected second_name.".to_string())
        };

        let date = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected date.".to_string())
        };

        let date = match NaiveDate::parse_from_str(&date, "%d.%m.%Y") {
            Ok(date) => date,
            Err(_) => return Err("Parse date error. Expected format <dd.MM.YYYY>".to_string()),
        };

        let hours = match vals.next() {
            Some(val) => val.to_string(),
            None => return Err("Expected hours.".to_string())
        };

        let hours = match hours.parse::<f64>() {
            Ok(hours) => hours,
            Err(_) => return Err("Hours must be a floating point number.".to_string()),

        };
        Ok(Tsr3Record {
            employee_id,
            last_name,
            first_name,
            second_name,
            date,
            hours
        })
    }
}

/// Структура представляющая сотрудника
#[derive(Debug, Clone)]
pub struct Employee {
    employee_id: String,
    last_name: String,
    first_name: String,
    second_name: String
}

impl Employee {
    /// Функция для получения короткой записи сотрудника
    pub fn get_short_name(&self) -> String {
        format!("{} {}.{}.",
            self.last_name, // фамилия
            self.first_name.chars().next().unwrap(), // первая буква имени
            self.second_name.chars().next().unwrap() // первая буква отчества
        )
    }
}

impl From<Tsr3Record> for Employee {
    fn from(value: Tsr3Record) -> Self {
        Employee {
            employee_id: value.employee_id,
            last_name: value.last_name,
            first_name: value.first_name,
            second_name: value.second_name
        }
    }
}

impl PartialEq for Employee {
    fn eq(&self, other: &Self) -> bool {
        self.employee_id == other.employee_id
    }
    fn ne(&self, other: &Self) -> bool {
        self.employee_id != other.employee_id
    }
}

impl Eq for Employee { }

impl Hash for Employee {
    fn hash<H: std::hash::Hasher>(&self, state: &mut H) {
        self.employee_id.hash(state)
    }
}

#[cfg(test)]
mod tests {
    use std::collections::HashMap;

    use super::*;

    #[test]
    fn test_equal_employee() {
        let a = Employee {
            employee_id: "1".to_string(),
            last_name: "A".to_string(),
            first_name: "S".to_string(),
            second_name: "D".to_string(),
        };
        let mut b = a.clone();
        b.last_name = "other".to_string();
        let mut c = a.clone();
        c.last_name = "another".to_string();

        // рефлексивность
        assert_eq!(a, a);
        // симметричность
        assert_eq!(a, b);
        assert_eq!(b, a);
        // транзитивность
        assert_eq!(a, b);
        assert_eq!(b, c);
        assert_eq!(a, c);
    }

    #[test]
    fn test_hash_employee() {
        let a = Employee {
            employee_id: "1".to_string(),
            last_name: "A".to_string(),
            first_name: "B".to_string(),
            second_name: "C".to_string(),
        };

        let mut b = a.clone();
        b.last_name = "other".to_string();
        let mut c = a.clone();
        c.employee_id = "2".to_string();

        let mut hash_map = HashMap::new();
        hash_map.insert(&a, 1);
        hash_map.insert(&b, 2); // b == a т.к. совпадает employee_id
        hash_map.insert(&c, 3);

        assert_eq!(hash_map.len(), 2usize);
        assert_eq!(hash_map.get(&a), Some(&2));
    }
}