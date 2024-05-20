#include <iostream>
#include <fstream>
#include <vector>
#include <map>

//хеш-функция
int hash_func(std::string str) { return ((int)*str.begin() + (int)*(str.end() - 1)) % 30; }

int main()
{
    //создание векторов для помещения данных из файла report.txt
    std::vector<std::vector<std::string>> ID(30);
    std::vector<std::vector<std::string>> FIO(30);
    std::vector<std::vector<double>> taken_hours(30);

    std::ifstream in;
    
    //проверка открытия файла
    try {
        in.open("report.txt");
        if (!in.is_open())
            throw std::exception("Error!");
    }
    catch (...) {
        return -1;
    }

    //перенос из первой строчки файла требуемого кол-ва часов в переменную
    double MAIN_hours;
    in >> MAIN_hours;

    //считывание данных из файла, получится устройство данных в виде хеш-таблицы, но она не будет расширяться при коллизиях
    while (!in.eof()) {
        std::string tmp;
        std::string tmp1;

        in >> tmp;
        //если ячейка вектора не пуста, то проверяем, делается ли списание повторно, или списание осуществляет другой сотрудник
        //(проверка по ID!)
        if (tmp.size() > 0 && ID[hash_func(tmp)].size() > 0) {
            bool is_there = false;
            for (int i{ 0 }; i < ID[hash_func(tmp)].size(); i++) {
                //если списание повторное, то добавляем часы к уже существующему в векторе сотруднику
                if (ID[hash_func(tmp)][i] == tmp) {
                    is_there = true;
                    //таким образом пропускаем "лишнюю" информацию из файла
                    in >> tmp1;
                    in >> tmp1;
                    in >> tmp1;
                    in >> tmp1;
                    double working;
                    in >> working;
                    taken_hours[hash_func(tmp)][i] += working;
                    break;
                }
            }
            //если сотрудник новый, то добавляем его ID, списанные часы и ФИО
            if (!is_there) {
                ID[hash_func(tmp)].push_back(tmp);
                in >> tmp1;
                FIO[hash_func(tmp)].push_back(tmp1);
                in >> tmp1;
                FIO[hash_func(tmp)].push_back(tmp1);
                in >> tmp1;
                FIO[hash_func(tmp)].push_back(tmp1);
                in >> tmp1;
                double working;
                in >> working;
                taken_hours[hash_func(tmp)].push_back(working);
            }
        }
        //если ячейка, номер которой получен с помощью хеш-функции, пуста, то записываем во все векторы информацию о сотруднике
        //tmp.size() > 0 существует для обхода ошибки, возникающей, если файл заканчивается '\n'
        else if (tmp.size() > 0) {
            ID[hash_func(tmp)].push_back(tmp);
            in >> tmp1;
            FIO[hash_func(tmp)].push_back(tmp1);
            in >> tmp1;
            FIO[hash_func(tmp)].push_back(tmp1);
            in >> tmp1;
            FIO[hash_func(tmp)].push_back(tmp1);
            in >> tmp1;
            double working;
            in >> working;
            taken_hours[hash_func(tmp)].push_back(working);
        }
    }

    in.close();

    std::ofstream out;
    out.open("result.txt");

    //два контейнера мультимэп для помещения сотрудников, переработавших или недоработавших > 10% (с сортировкой по алфавиту)
    std::multimap<std::string, double> plus_balance;
    std::multimap<std::string, double> minus_balance;

    for (int i{ 0 }; i < taken_hours.size(); i++) {
        for (int j{ 0 }; j < taken_hours[i].size(); j++) {
            if (taken_hours[i][j] / MAIN_hours < 0.9) {
                //помещаем ключ в требуемом виде
                minus_balance.emplace(FIO[i][j * 3] + " " + FIO[i][j * 3 + 1][0] + "." + FIO[i][j * 3 + 2][0] + "." + " -", MAIN_hours - taken_hours[i][j]);
                continue;
            }
            if (taken_hours[i][j] / MAIN_hours > 1.1) 
                plus_balance.emplace(FIO[i][j * 3] + " " + FIO[i][j * 3 + 1][0] + "." + FIO[i][j * 3 + 2][0] + "." + " +", taken_hours[i][j] - MAIN_hours);
        }
    }

    //запись в результирующий файл
    for (const auto& item : minus_balance) {
        out << item.first << item.second << std::endl;
    }
    for (const auto &item : plus_balance) {
        out << item.first << item.second << std::endl;
    }
    
    out.close();
    return 0;
}
