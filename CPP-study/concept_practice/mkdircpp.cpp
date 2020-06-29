#include <iostream>
#include <string>
#include <sys/stat.h> // stat
#include <errno.h>    // errno, ENOENT, EEXIST

#define FOLDER_PATH "/tmp/Logs/"

bool isDirExist(const std::string& path)
{
    struct stat info;
    if (stat(path.c_str(), &info) != 0)
    {
        return false;
    }
    return (info.st_mode & S_IFDIR) != 0;
}

bool makePath(const std::string& path)
{
    mode_t mode = 0755;
    int ret = mkdir(path.c_str(), mode);
    if (ret == 0)
        return true;

    switch (errno)
    {
    case ENOENT:
        // parent didn't exist, try to create it
        {
            int pos = path.find_last_of('/');
            if (pos == std::string::npos)
                return false;
            if (!makePath( path.substr(0, pos) ))
                return false;
        }
        // now, try to create again
        return 0 == mkdir(path.c_str(), mode);
    case EEXIST:
        // done!
        return isDirExist(path);

    default:
        return false;
    }
}

int main(int argc, char* ARGV[])
{
    // for (int i=1; i<argc; i++)
    // {
    //     std::cout << "creating " << ARGV[i] << " ... " << (makePath(ARGV[i]) ? "OK" : "failed") << std::endl;
    // }
    makePath(FOLDER_PATH);
    return 0;
}