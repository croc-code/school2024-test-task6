#include <algorithm>
#include <cstdlib>
#include <exception>
#include <fstream>
#include <iostream>
#include <map>
#include <ranges>
#include <string>
#include <utility>
#include <vector>


struct PersonInfo
{
    std::string Name;
    std::string MiddleName;
    std::string LastName;
    double Worktime;
};

constexpr std::vector<std::string> SplitString(std::string_view str, std::string_view delimiters)
{
    std::vector<std::string> output;
    size_t first = 0;

    while (first < str.size())
    {
        const auto second = str.find_first_of(delimiters, first);

        if (first != second)
            output.emplace_back(str.substr(first, second - first));

        if (second == std::string_view::npos)
            break;

        first = second + 1;
    }

    return output;
}

std::string GetFirstUTF8Char(const std::string& str)
{
    if (str.empty())
    {
        return "";
    }

    unsigned char firstByte = static_cast<unsigned char>(str[0]);

    size_t charLength = 1;
    if (firstByte >= 0xF0)
    {
        charLength = 4;
    }
    else if (firstByte >= 0xE0)
    {
        charLength = 3;
    }
    else if (firstByte >= 0xC0)
    {
        charLength = 2;
    }

    return str.substr(0, charLength);
}

bool compareByUTF8(const std::pair<std::string, double>& a, const std::pair<std::string, double>& b)
{
    return a.first < b.first;
}

int main()
{
    std::string line;
    double hours;
    std::map<std::string, struct PersonInfo> InfoWeek;

    std::ifstream in("report.txt");
    try
    {
        std::getline(in, line);
        hours = stod(line);
    }
    catch (const std::exception& exp)
    {
        std::cout << "Expected: First Line Hours: " << exp.what() << '\n';
        std::exit(1);
    }

    if (in.is_open())
    {
        while (std::getline(in, line))
        {
            std::vector Person = SplitString(line, " ");
            if (!InfoWeek.contains(Person[0]))
            {
                InfoWeek.emplace(std::pair<std::string, PersonInfo>{
                    Person[0], PersonInfo((Person[1]), Person[2], Person[3], stod(Person[5]))});
            }
            else
            {
                InfoWeek[Person[0]].Worktime += stod(Person[5]);
            }
        }
    }

    in.close();

    std::ofstream fout("result.txt");
    if (fout.is_open())
    {
        std::vector<std::pair<std::string, double>> OverWorkedPersonPlus;
        std::vector<std::pair<std::string, double>> OverWorkedPersonMinus;

        for (auto& [_, info] : InfoWeek)
        {
            if (info.Worktime > hours * 1.1)
            {
                OverWorkedPersonPlus.push_back(std::make_pair(info.Name + ' ' + GetFirstUTF8Char(info.MiddleName) +
                                                                  '.' + GetFirstUTF8Char(info.LastName) + '.',
                                                              info.Worktime - hours));
            }

            if (info.Worktime < hours * 0.9)
            {
                OverWorkedPersonMinus.push_back(std::make_pair(info.Name + ' ' + GetFirstUTF8Char(info.MiddleName) +
                                                                   '.' + GetFirstUTF8Char(info.LastName) + '.',
                                                               info.Worktime - hours));
            }
        }
        std::sort(OverWorkedPersonMinus.begin(), OverWorkedPersonMinus.end(), compareByUTF8);
        std::sort(OverWorkedPersonPlus.begin(), OverWorkedPersonPlus.end(), compareByUTF8);

        for (const auto& [name, time] : OverWorkedPersonMinus)
        {
            fout << name << ' ' << time << '\n';
        }

        fout << OverWorkedPersonPlus[0].first << " +" << OverWorkedPersonPlus[0].second;

        for (const auto& [name, time] : OverWorkedPersonPlus | std::views::drop(1))
        {
            fout << '\n' << name << " +" << time;
        }
    }
    fout.close();
}
