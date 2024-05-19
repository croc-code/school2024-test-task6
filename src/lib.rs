use std::fs;

use model::Employee;

mod model;
mod util;

pub fn main(percent: u16, path: String) -> Result<(), String> {
    let contents = read_contents_from_file(&path)?;
    
    // получить недельную норму и TSR-3 записи
    let (weekly_norm, tsr3_records) = util::get_weekly_norm_and_tsr3_records_from_contents(contents)?;

    // подсчет общего кол-ва списаний за неделю для каждого сотрудника
    let employee_hours = util::get_employee_hours(tsr3_records);

    // оставить в векторе сотрудников с дизбалансом в 10%
    let mut employee_with_imbalance = util::filter_employees_by_imbalance(employee_hours, weekly_norm, percent);

    // отсортировать для вывода
    util::sorting_for_output(&mut employee_with_imbalance);

    // вывод результата
    write_result(employee_with_imbalance, &path)?;

    Ok(())
}

/// Читает файл report.txt
fn read_contents_from_file(path: &str) -> Result<String, String> {
    // чтение входного файла
    match fs::read_to_string(format!("{}/report.txt", path)) {
        Ok(content) => Ok(content),
        Err(_) => Err("Can't find/open 'report.txt' file".to_string()),
    }
}

/// Записать в файл result.txt
fn write_result(employee_with_imbalance: Vec<(Employee, f64)>, path: &str) -> Result<(), String> {
    let mut contents = String::new();

    for (employee, hours) in employee_with_imbalance {
        contents.push_str(&format!("{} {}\n", employee.get_short_name(), util::format_hours(hours)));
    }

    match fs::write(format!("{}/result.txt", path), contents) {
        Ok(_) => Ok(()),
        Err(err) => Err(err.to_string()),
    }
}