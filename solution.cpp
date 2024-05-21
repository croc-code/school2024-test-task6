#include <algorithm>
#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <map>
#include <queue>
#include <tuple>
#include <windows.h>

using namespace std;

int main() {
    // Настройка кодировки консоли для правильного ввода и вывода русских символов
    SetConsoleCP(1251);
    SetConsoleOutputCP(1251);
    // Открытие входного и выходного файлов
    ifstream rep("report.txt");
    ofstream res("result.txt");

    // Словарь для хранения общего времени, затраченного сотрудником
    map<string, float> timeSpent;
    // Словарь для хранения фамилии и инициалов сотрудников
    map<string, string> employeeNames;

    // Вектор для хранения результатов (Фамилия И.О., знак, величина дизбаланса)
    vector<tuple<string, string, float>> answer;

    // Чтение недельной нормы из первой строки файла
    float weeklyNorm;
    rep >> weeklyNorm;
    rep.ignore(); // Игнорирование символа новой строки после чтения нормы

    string s;
    // Чтение и обработка каждой строки в файле
    while (getline(rep, s)) {
        // Разделение строки на части, те по пробелам
        int p = 0;
        vector<string> v;
        for (int j = 0; j < s.size(); ++j) {
            if (s[j] == ' ') {
                v.push_back(s.substr(p, j - p));
                p = j + 1;
            }
            else if (j == s.size() - 1) {
                v.push_back(s.substr(p, j - p + 1));
            }
        }
        // Обновление общего времени, затраченного сотрудником
        timeSpent[v[0]] += stof(v[5]);
        // Формирование строки с фамилией и инициалами
        employeeNames[v[0]] = string(v[1] + ' ' + v[2][0] + '.' + v[3][0] + '.');
    }

    // Вычисление дизбаланса для каждого сотрудника
    for (const auto& i : timeSpent) {
        // Если дизбаланс превышает 10% от нормы, добавление записи в результаты
        if (i.second - weeklyNorm > weeklyNorm * 0.1) {
            answer.emplace_back(employeeNames[i.first], " +", i.second - weeklyNorm);
        }
        else if (i.second - weeklyNorm < -weeklyNorm * 0.1) {
            answer.emplace_back(employeeNames[i.first], " ", i.second - weeklyNorm);
        }
    }

    // Сортировка результатов по знаку и фамилии
    sort(answer.begin(), answer.end(), [](const auto& a, const auto& b) {
        if (get<1>(a).length() == get<1>(b).length())
            return get<0>(a) < get<0>(b);
        return get<1>(a).length() < get<1>(b).length();
        });

    // Запись результатов в выходной файл
    for (auto i : answer) {
        string s1 = get<0>(i) + get<1>(i) + to_string(get<2>(i)) + "\n";
        res << s1;
    }
    rep.close();
    res.close();
}
