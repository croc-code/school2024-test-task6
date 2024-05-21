#include <iostream>
#include <fstream>
#include <string>
#include <cctype>
#include <cstdio>
#include <cstring>
#include <vector>
#include <sstream>
#include <map>
#include <algorithm>
#include <iconv.h>
#include <windows.h>

using namespace std;

#define ERROR -1
#define OK 0

struct Employee 
{
    wstring ID;
    wstring fullname;
    float totalHours = 0.0;
};

bool compare(const pair<wstring, float>& a, const pair<wstring, float>& b) 
{
    return a.first < b.first;
}

int main(void)
{
    setlocale(LC_ALL, "RU");
    //открытие файла report.txt с проверкой
    wifstream file("report.txt");

    if (!file.is_open())
    {
        cout << "ERROR OPEN FILE!" << endl;
        return ERROR;
    }
    //считываем недельную норму часов и проверяем  что в первой строке файла действительно число
    wstring buffer;
    getline(file, buffer);
    try
    {
        int limit_time = stoi(buffer);
    }
    catch (invalid_argument)
    {
        cout << "CHECK FILE'S DATA!";
        file.close();
        return ERROR;
    }
    int limit_time = stoi(buffer);
    //создание вектора для помещени еданных из файла report.txt
    vector<Employee> employees;
    //считываем данные сотрудников из файла и заполняе структуру
    while (getline(file, buffer))
    {
        wstringstream ss(buffer);
        wstring ID, surname, fname, lname, date;
        float hours;
        ss >> ID >> surname >> fname >> lname >> date >> hours;
        wstring fullname = surname + L" " + fname.substr(0,1) + L". " + lname.substr(0,1) + L".";
        
        //проверка на повтор по ID
        bool found = false;
        for (auto& employee : employees)
        {
            if (employee.ID == ID)
            {
                employee.totalHours += hours;
                found = true;
                break;
            }
        }
        //если данные о сотруднике еще не были вписаны
        if (!found)
        {
            Employee employee;
            employee.ID = ID;
            employee.totalHours = hours;
            employee.fullname = fullname;
            employees.push_back(employee);
        }

    }
    file.close();
    //открытие файла result.txt с проверкой
    wofstream resultFile("result.txt");
    if (!resultFile.is_open())
    {
        cout << "ERROR OPEN FILE!" << endl;
        return ERROR;
    }
    //создание двух векторов для недоработаших или переработавших сотрудников
    vector<pair<wstring, float> > negativeDisbalance;
    vector<pair<wstring, float> > positiveDisbalance;
    // определям кто из сотрудников недоработал или переработал
    for (const auto& employee : employees)
    {

        float disbalance = employee.totalHours - limit_time;
        if (disbalance < -0.1 * limit_time)
        {
            negativeDisbalance.emplace_back(employee.fullname, disbalance);
        }
        else if (disbalance > 0.1 * limit_time)
        {
            positiveDisbalance.emplace_back(employee.fullname, disbalance);
        }
    }
    //сортируем данные векторов в алфавитном порядке
    sort(negativeDisbalance.begin(), negativeDisbalance.end(), compare);
    sort(positiveDisbalance.begin(), positiveDisbalance.end(), compare);
    //вывод результата в файл result.txt
    for (const auto& entry : negativeDisbalance)
    {
        resultFile << entry.first << " -" << abs(entry.second) << endl;
    }

    for (const auto& entry : positiveDisbalance)
    {
        resultFile << entry.first << " +" << entry.second << endl;
    }
    resultFile.close();

    return OK;
}