#include "tools.h"

int main() {
    //таким образом любые исключения будут пойманы! кроме segfault
    try {
        ImbalanceAnalysis();
    } catch (const std::exception& err) {
        std::cerr << "Error: " << err.what() << std::endl;
    } catch (...) {
        std::cerr << "Unknown Error " << std::endl;
    }
    return 0;
}