#include "Logger.h"

Logger* Logger::mLoggerInstance = nullptr ;
std::mutex Logger::mMtxLock;

// Implementation of Methods which generate logs 

/**/
/*int Logger::printToLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...) {
	
	if(fileRoute == FILE_ROUTE::CONSOLE) {
		//printLog(())
		setFileDescriptor(fileRoute);
	}
	else if(fileRoute == FILE_ROUTE::ERROR) {
		setFileDescriptor(fileRoute);

	}
	else if(fileRoute == FILE_ROUTE::DISK) {

	}
}*/

/**/
int Logger::printLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...) {

	if(fileRoute == FILE_ROUTE::CONSOLE) {
		//printLog(())
		setFileDescriptor(fileRoute);

	}
	else if(fileRoute == FILE_ROUTE::ERROR) {
		setFileDescriptor(fileRoute);

	}
	else if(fileRoute == FILE_ROUTE::DISK) {
		setFileDescriptor(fileRoute);

	}


}


/* This gets called only once .. when the file descriptor is not assigned*/
void Logger::setFileDescriptor(FILE_ROUTE aRoute, int aIndex=0) {

	if(aRoute == FILE_ROUTE::CONSOLE) {
		// assign the file pointer for STDOUT_FILENO
		//mLoggerConsoleFile is to be assigned
		mLoggerConsoleFile.writeLock.lock();
		mLoggerConsoleFile.fp =  STDOUT_FILENO;
		mLoggerConsoleFile.writeLock.unlock();

	} 
	else if(aRoute == FILE_ROUTE::ERROR) {
		// assign the file pointer for STDERR_FILENO  
		// mLoggerErrorConsoleFile is to be assigned
		//
		mLoggerErrorConsoleFile.writeLock.lock();
		mLoggerErrorConsoleFile.fp =  STDERR_FILENO;
		mLoggerErrorConsoleFile.writeLock.unlock();

	} 
	else if(aRoute == FILE_ROUTE::DISK) {
		// mLoggerDiskFile

		mLoggerDiskFile.writeLock.lock();
		//mLoggerDiskFile.fp =  ;
/*		
		Logic is to to check if the file exists,  Since 
		we are going to cycle through the complete list of 
		logger files we are going to use 
		
		1. incase none are created we will create from one

		2. Once the logger call's with the aroute, we will check if next 
		file in the alloted list is create and then create a file and returns 

		3. Case where the all the allowed files are created we iterate to 
		the first one then on next call we go to next, how we achive this 
		is by maintaining the next file name

		char mActiveFileName[1024]; 
		char mNextFileName[1024];
*/

/*		
		Generate the  mActiveFileName with following logic
		1. if no files are there in the path
			a. check if the path exists, if not create and 
			we can avoid checking of files existance and continue 
			with create the file and returning the pointer to it 
		2. if files are there then check if the Next supposed to be file
		   create the file and return the file pointer, incase it is allready 
		   created then open it and assign the FP.  
		##
		1. 
		  a. We will close the FP for the present index  
		  b. since we get the active index of the file we will need to next index
		  c. The index if it reaches < mLogFileMax from 0 till 4 if the max is 5
		  1 

*/

		if()

		mLoggerDiskFile.writeLock.unlock();		 
	}

}	


/**
 * Check if a file exists
 * @return true if and only if the file exists, false else
 */
bool Logger::fileExists(const char* file) {
    struct stat buf;
    return (stat(file, &buf) == 0);
}

/**
 * Check if a file exists
 * @return true if and only if the file exists, false else
 */
bool Logger::fileExists(const std::string& file) {
    struct stat buf;
    return (stat(file.c_str(), &buf) == 0);
}


bool Logger::isDirExist(const std::string& path)
{
    struct stat info;
    if (stat(path.c_str(), &info) != 0)
    {
        return false;
    }
    return (info.st_mode & S_IFDIR) != 0;
}

bool Logger::CheckAndMkdir(const std::string& path)
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




/**/ 
static Logger *Logger::getInstance() {

  if(mLoggerInstance == nullptr) {
    //  lock_guard<mutex> lock(m_);
    mMtxLock.lock();
      if(mLoggerInstance == nullptr) {
        cout << "create first time" << endl;
        mLoggerInstance = new Logger();
      }
    mMtxLock.unlock();  
  }
  return mLoggerInstance;
}
