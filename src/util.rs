use std::{cmp::Ordering, collections::HashMap, str::Lines};

use crate::model::{Employee, Tsr3Record};

static EPSILONE: f64 = 1e-9;

/// Получает недельную норму и вектор TSR-3 записей.
pub fn get_weekly_norm_and_tsr3_records_from_contents(contents: String) -> Result<(u16, Vec<Tsr3Record>), String> {
    let mut lines = contents.lines(); // получение итератора по строкам

    let weekly_norm = get_weekly_norm(&mut lines)?;
    let tsr3_records = get_tsr3_records_from_lines(lines)?;

    Ok((weekly_norm, tsr3_records))
}

/// Принимает итератор на строки. Сдвигает итератор на 1
fn get_weekly_norm(lines: &mut Lines) -> Result<u16, String> {
    let weekly_norm = match lines.next() {
        Some(val) => val,
        None => return Err("Weekly norm was expected.".to_string())
    };

    match weekly_norm.parse::<u16>() {
        Ok(num) => Ok(num),
        Err(_) => return Err("The weekly norm must be a positive integer.".to_string()),
    }
}

/// Преобразует строки формата TSR-3 в сущность Tsr3Record 
fn get_tsr3_records_from_lines(lines: Lines) -> Result<Vec<Tsr3Record>, String> {
    lines
        .map(Tsr3Record::try_from)
        .collect()
}

/// Добавляет '+' к положительному значению
pub fn format_hours(num: f64) -> String {
    if num > EPSILONE {
        format!("+{}", num)
    } else {
        format!("{}", num)
    }
}

/// Сортирует так, что сначала идут сотрудники с отрицательным дизбалансом в алфавитном порядке,
/// затем идут сотрудники с положительным дизбалансом в алфавитном порядке.
pub fn sorting_for_output(employee_with_imbalance: &mut Vec<(Employee, f64)>) {

    employee_with_imbalance.sort_by(|a, b| {
        // Если оба меньше нуля или оба больше нуля, тогда сортируем по алфавиту
        if (a.1 < EPSILONE && b.1 < EPSILONE) || (a.1 > EPSILONE && b.1 > EPSILONE) {
            return a.0.get_short_name().cmp(&b.0.get_short_name());
        }
        // Сравниваем числа с плавующей точкой
        if (a.1 - b.1).abs() < EPSILONE {
            return Ordering::Equal;
        } else if a.1 > b.1 + EPSILONE {
            return Ordering::Greater;
        } 
        Ordering::Less
    })
}

/// Функция, которая вернет вектор сотрудников, с дизбалансом больше некого значения.
/// # Аргументы
/// * employee_hours - отображение, где ключ - сотрудник, а значение - кол-во его списаний.
/// * weekly_norm - недельная норма списаний.
/// * percent - процент отклонения. В векторе будут сотрудники с дизбалансом более указанного значения в обе стороны.
pub fn filter_employees_by_imbalance(employee_hours: HashMap<Employee, f64>, weekly_norm: u16, percent: u16) -> Vec<(Employee, f64)> {
    let factor = weekly_norm as f64 * ( percent as f64 / 100.0 );
    employee_hours
        .into_iter()
        .filter_map(|(employee, hours)| {
            // если отклонение больше factor, то оставить значение в векторе
            if ( weekly_norm as f64 - hours ).abs() - EPSILONE > factor {
                Some( ( employee, hours - weekly_norm as f64 ) )
            } else {
                None
            }
        })
        .collect()
}

/// Для каждого сотрудника считает количество списаний.
/// На вход вектор TSR-3 записей.
/// На выход HashMap, где ключ - id сотрудника, значение - его кол-во списаний за неделю.
pub fn get_employee_hours(tsr3_records: Vec<Tsr3Record>) -> HashMap<Employee, f64> {
    let mut employee_hours = HashMap::new();
    for tsr3_record in tsr3_records {
        let cur_hours = tsr3_record.get_hours();
        employee_hours
            .entry(Employee::from(tsr3_record))
            .and_modify(|hours| *hours += cur_hours)
            .or_insert(cur_hours);
    }

    employee_hours
}

#[cfg(test)]
mod tests {
    use super::*;

    
    #[test]
    fn parse_weekly_norm() {
        let mut content = "\
-56
9.5
12".lines();
        if let Ok(_) = get_weekly_norm(&mut content) {
            panic!("Expected error");
        };
        if let Ok(_) = get_weekly_norm(&mut content) {
            panic!("Expected error");
        }
        if let Err(_) = get_weekly_norm(&mut content) {
            panic!("Expected Ok");
        }
    }

}