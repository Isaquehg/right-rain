#include <iostream>
#include <iomanip>
#include <sstream>
#include <ctime>

std::string format_iso8601_time(std::time_t time)
{
    std::stringstream ss;
    ss << std::put_time(std::gmtime(&time), "%FT%T.000Z");
    return ss.str();
}

int main()
{
    std::time_t now = std::time(nullptr);
    std::string iso8601_time = format_iso8601_time(now);
    std::cout << iso8601_time << std::endl;
    return 0;
}
