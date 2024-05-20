use std::collections::HashMap;
use chrono::NaiveDate;

// Функция для парсинга даты из строки в формат NaiveDate
fn parse_date(date_s: String) -> Result<NaiveDate, String> {
    NaiveDate::parse_from_str(&date_s, "%d.%m.%Y")
        .map_err(|_| "Error while parsing date".to_string())
}

// Структура для представления записи TSR-3
#[allow(dead_code)]
pub struct Tsr3Entry {
    employee_id: String,
    surname: String,
    name: String,
    patronymic: String,
    date: NaiveDate,
    time: f32,
}

// Функция для создания объекта Tsr3Entry из строки
pub fn get_entry(s: &String) -> Tsr3Entry {
    let mut elems = s.split_whitespace();
    let employee_id = elems.next().unwrap().to_string();
    let surname = elems.next().unwrap().to_string();
    let name = elems.next().unwrap().to_string();
    let patronymic = elems.next().unwrap().to_string();
    let date = parse_date(elems.next().unwrap().to_string()).unwrap();
    let time = elems.next().unwrap().parse().unwrap();

    Tsr3Entry {
        employee_id,
        surname,
        name,
        patronymic,
        date,
        time,
    }
}

// Функция для создания карты времени, ассоциированного с каждым сотрудником
fn make_time_map(tsr3_vec: &Vec<Tsr3Entry>) -> HashMap<String, f32> {
    let mut time_map: HashMap<String, f32> = HashMap::new();

    for e in tsr3_vec {
        let full_name = format!("{} {} {}", e.surname, e.name, e.patronymic);
        time_map.entry(full_name).and_modify(|time| *time += e.time).or_insert(e.time);
    }

    time_map
}

// Функция для создания карты дисбаланса времени с порогом threshold
pub fn make_disbalanced_time_map(tsr3_vec: &Vec<Tsr3Entry>, threshold: f32) -> Vec<(String, String)> {
    // Создаем карту времени
    let time_map = make_time_map(tsr3_vec);
    let mut res: HashMap<String, String> = HashMap::new();

    for (k, v) in time_map {
        let disbalance = v - threshold;

        // Проверяем, превышает ли дисбаланс 10% порог в какую либо из сторон
        if disbalance > threshold * 0.1 || disbalance < threshold * -0.1 {
            let mut split_name = k.split_whitespace();
            let short_name = format!(
                "{} {}.{}.",
                split_name.next().unwrap(),
                split_name.next().unwrap().chars().next().unwrap(),
                split_name.next().unwrap().chars().next().unwrap()
            );

            res.insert(short_name, if disbalance < 0.0 { disbalance.to_string() } else { format!("+{}", disbalance) });
        }
    }

    // Преобразуем хеш-таблицу в вектор для сортировки
    let mut res_vec: Vec<(_, _)> = res.into_iter().collect();

    // Сортируем сначала по знаку дисбаланса, затем по имени
    res_vec.sort_by(|a, b| {
        let sign_a = a.1.starts_with('-');
        let sign_b = b.1.starts_with('-');
        sign_b.cmp(&sign_a).then_with(|| a.0.cmp(&b.0))
    });

    res_vec
}

