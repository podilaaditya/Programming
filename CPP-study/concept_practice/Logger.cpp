#include "Logger.h"

Logger* Logger::mLoggerInstance = nullptr ;
std::mutex Logger::mMtxLock;

// Implementation of Methods which generate logs 

/**/
int Logger::printToLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...) {
	//
}

/**/
int Logger::printToConsole(FILE *ptr,LOGGING_LEVEL loglvl, const char* str, ...) {

}

/**/
int Logger::printToFile(FILE *ptr,LOGGING_LEVEL loglvl, const char* str, ...) {

}

/**/
FILE *Logger::returnFileDescriptor(FILE_ROUTE aRoute) {

	if(aRoute == FILE_ROUTE::CONSOLE) {
		// return the file pointer for STDOUT_FILENO
		ofstream myfile;


	} 
	else if(aRoute == FILE_ROUTE::ERROR) {
		// return the file pointer for STDERR_FILENO  
	} 
	else if(aRoute == FILE_ROUTE::DISK) {

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
