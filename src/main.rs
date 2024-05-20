use std::fs::File;
use std::io::{self, Write, BufRead};

mod tsr3;

fn main() -> io::Result<()> {
    // Открываем файл report.txt для чтения
    let file = File::open("report.txt")?;
    let reader = io::BufReader::new(file);
    
    // Создаём вектор для хранения записей TSR-3
    let mut tsr3_vec: Vec<tsr3::Tsr3Entry> = Vec::new();

    // Читаем первую строку, содержащую недельную норму списания
    let mut lines = reader.lines();
    let disbalance_threshold: f32 = lines.next().unwrap().unwrap().parse().unwrap();
    
    // Читаем остальные строки, содержащие списания сотрудников
    for line in lines {
        let l: String = line.unwrap();
        tsr3_vec.push(tsr3::get_entry(&l));
    }

    // Определяем сотрудников с дизбалансом списаний
    let filtered_map = tsr3::make_disbalanced_time_map(&tsr3_vec, disbalance_threshold);

    // Создаем и открываем файл result.txt для записи результата
    let mut result = File::create("result.txt")?;
    
    // Записываем сотрудников с дизбалансом в файл
    for (k, v) in &filtered_map {
        writeln!(result, "{} {}", k, v)?;
    }

    Ok(())
}

