use std::process;

fn main() {
    let mut args = std::env::args();
    args.next();
    // получить путь до файла, если аргумент не указан, то присваивается текущая директория
    let path = match args.next() {
        Some(path) => path,
        None => ".".to_string(),
    };
    // процент отклонения
    let percent: u16 = 10;

    match imbalance_analyzer::main(percent, path) {
        Ok(_) => (),
        Err(err) => {
            println!("{}", err);
            process::exit(1);
        }
    }
}