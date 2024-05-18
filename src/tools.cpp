#include "tools.h"

//----------------------------------SmartFileReader----------------------------------

SmartFileReader::SmartFileReader(const std::string& fileName) : std::ifstream(fileName) {}

SmartFileReader::~SmartFileReader() {
    if (this->is_open()) this->close();
}

//----------------------------------SmartFileWriter----------------------------------

SmartFileWriter::SmartFileWriter(const std::string& fileName) : std::ofstream(fileName) {}

SmartFileWriter::~SmartFileWriter() {
    if (this->is_open()) this->close();
}

//----------------------------------Data----------------------------------

Data::Data(const std::string& str) {
    std::string day;
    std::string month;
    std::string year;

    std::istringstream iss(str);

    std::getline(iss, day, '.');
    std::getline(iss, month, '.');
    std::getline(iss, year, '.');

    this->day = std::stoi(day);
    this->month = std::stoi(month);
    this->year = std::stoi(year);
}

//----------------------------------Employee----------------------------------

Employee::Employee(const std::string& uuid, const std::string& lastName, const std::string& firstName, const std::string& fatherName) {
    this->uuid_ = uuid;
    this->lastName_ = lastName;
    this->firstName_ = firstName;
    this->fatherName_ = fatherName;
}

bool Employee::operator<(const Employee& other) const {
    return (lastName_ + firstName_ + fatherName_) < (other.lastName_ + other.firstName_ + other.fatherName_);
}



std::string Formation::FormationOfFullNameInitials(std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>>& converter,
        const std::string& lastName, const std::string& firstName, const std::string& fatherName) {
    std::string returnObject;

    std::wstring wFirstName = converter.from_bytes(firstName);
    std::wstring wFatherName = converter.from_bytes(fatherName);

    std::string firstNameInitial = converter.to_bytes(wFirstName[0]);
    std::string fatherNameInitial = converter.to_bytes(wFatherName[0]);

    returnObject += lastName + " " + firstNameInitial + "." + fatherNameInitial + ".";

    return returnObject;
}

std::string Formation::FormationOfRoundedNumber(const float& num) {
    float difference = round(num) - num;
    if (fabs(difference) < EPSILON) { // Если верное, то число можно считать целым, без дробной части.
        return std::to_string(static_cast<int>(round(num)));
    }
    std::string returnObject = std::to_string(num);
    Formation::Replacer(returnObject, ',', '.');

    // В этой части убираем ненужные нули после запятой
    size_t nonZeroPos = returnObject.find_first_of("0", returnObject.find('.'));

    if (nonZeroPos != std::string::npos) {
        returnObject = returnObject.substr(0, nonZeroPos);
    }
    return returnObject;
}

void Formation::Replacer(std::string& str, const char replacedWhat, const char replaceTo) {
    for (auto& ch : str) {
        if (ch == replacedWhat) {
            ch = replaceTo;
            break;
        }
    }
    return;
}

void ImbalanceAnalysis() {

    // Необходимо для правильной записи русских символов
    std::locale loc("ru_RU.UTF-8");
    std::locale::global(loc);
    std::wcout.imbue(loc);

    // Так же необходимый инструмент для преобразования из в "Русскую" строку.
    std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;

    SmartFileReader ReportFile(REPORT_PATH);
    SmartFileWriter ResultFile(RESULT_PATH);

    if (!ReportFile.is_open() || !ResultFile.is_open()) throw std::runtime_error("Files didn't open. Check REPORT_PATH and RESULT_PATH, or give the necessary permissions to the files");

    std::map<Employee, float> employeeWorkingHours;

    // вычисляем максимально допустимое отклонение от нормы.
    std::string strLine;
    std::getline(ReportFile, strLine);
    const float weeklyNeededHours = std::stof(strLine);
    const float delta = weeklyNeededHours * (DEVIATION/100.0);


    while (std::getline(ReportFile, strLine)) {
        std::string uuid;
        std::string lastName;
        std::string firstName;
        std::string fatherName;
        std::string data;
        std::string workingHours;

        std::istringstream iss(strLine);
        iss >> uuid >> lastName >> firstName >> fatherName >> data >> workingHours;
        
        Employee employee(uuid, lastName, firstName, fatherName);
        Formation::Replacer(workingHours, '.', ',');
        employeeWorkingHours[employee] += std::stof(workingHours);
    }

    // Сначала выводим всеех с отрицательным дисбалансом
    for (const auto& [employee, workHours] : employeeWorkingHours) {
        float difference = workHours - (weeklyNeededHours - delta);
        if (difference < 0 && std::fabs(difference) >= EPSILON) {
            strLine = Formation::FormationOfFullNameInitials(converter, employee.lastName_, employee.firstName_, employee.fatherName_) + 
                    " -" + Formation::FormationOfRoundedNumber(weeklyNeededHours - workHours);
            ResultFile << strLine << '\n';
        }
    }
    ResultFile.flush(); // принудительно выгружаем все из буфера в файл.

    for (const auto& [employee, workHours] : employeeWorkingHours) {
        float difference = workHours - (weeklyNeededHours + delta);
        if (difference > 0 && std::fabs(difference) >= EPSILON) {
            strLine = move(Formation::FormationOfFullNameInitials(converter, employee.lastName_, employee.firstName_, employee.fatherName_)) + 
                    " +" + move(Formation::FormationOfRoundedNumber(workHours - weeklyNeededHours));
            ResultFile << strLine << '\n';
        }
    }
    ResultFile.flush();
}