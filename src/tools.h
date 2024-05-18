#pragma once 

#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
#include <codecvt>
#include <locale>
#include <math.h>
#include <set>
#include <map>

#define REPORT_PATH "../report.txt"
#define RESULT_PATH "../result.txt"
#define DEVIATION 10.0
#define EPSILON 1e-8 // необходимо для срачнения вещественных чисел.


// Простарнство функций как-либо форматирующих строку.
namespace Formation {

    // Формирует фамилию с инициалами
    std::string FormationOfFullNameInitials(std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>>& converter,
        const std::string& lastName, const std::string& firstName, const std::string& fatherName);

    // Для корректной обработки числа с вещественной частью.
    std::string FormationOfRoundedNumber(const float& num);

    // Заменяет символов. Нужно для корректной работы std::stof.
    void Replacer(std::string& str, const char replacedWhat, const char replaceTo);
}

// Обёртка над классом ifstream для более удобного пользования и безопасности.
class SmartFileReader : public std::ifstream {
public:
    SmartFileReader(const std::string& fileName);

    ~SmartFileReader();
};

// Обёртка над классом ofstream для более удобного пользования и безопасности. Пародия на smart pointer
class SmartFileWriter : public std::ofstream {
public:
    SmartFileWriter(const std::string& fileName);

    ~SmartFileWriter();
};

// Для гибкости кода. Если вдрг понадобится учитывать дату, то структура уже реализованна. Пародия на smart pointer
struct Data{

    Data() = default;
    Data(const std::string& str);

    int day;
    int month;
    int year;
};

// Структура, хранит uuid рабочего и его ФИО.
struct Employee {
    
    Employee(const std::string& uuid, const std::string& lastName, const std::string& firstName, const std::string& fatherName);

    // Оператор сравнения нужен для того, чтобы map знал как сравнивать эти структуры между собой.
    bool operator<(const Employee& other) const;

    std::string uuid_;
    std::string lastName_;
    std::string firstName_;
    std::string fatherName_;
};

// Главная функция. Обрабатывает report.txt и заносит сведения в result.txt.
void ImbalanceAnalysis();