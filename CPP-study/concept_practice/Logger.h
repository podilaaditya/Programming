#ifndef __CONSOLE_LOGGER_H__
#define __CONSOLE_LOGGER_H__

#include <time.h>
#include <string.h>
#include <cstdlib.h>
#include <cstdio.h>
#include <cstdarg>
#include <sys/stat>

#include <iostream>
#include <atomic>
#include <mutex>
#include <thread>

#include <fstream>

#include <errno.h>

#define PATH_FOR_LOGS "//tmp//logger//"
#define FILE_NAME_PREFIX "Log_"

class Logger
{

	private:
		int mActiveIndex=0; 
		char mNextFileName[255];
		char mPathForLogs[4096];
		const int const mLogFileMax = 5;
		static mutex  mMtxLock;
		static Logger *mLoggerInstance;


		struct LoggerFileCntrl {
		  void 	*udata;
		  // lock the file in multi-thread logging
		  std::mutex writeLock; 
		  FILE 	*fp;
		  char 	filePath[4096];
		  int 	level;
		  int 	quiet;
		};

		LoggerFileCntrl mLoggerConsoleFile;
		LoggerFileCntrl mLoggerErrorConsoleFile;
		LoggerFileCntrl mLoggerDiskFile[4]; 

		enum LOGGING_LEVEL { 
			LOG_TRACE,
			LOG_DEBUG, 
			LOG_INFO, 
			LOG_WARN, 
			LOG_ERROR, 
			LOG_FATAL 
		} mLogLevel;

		enum FILE_ROUTE{
			CONSOLE = 10,
			ERROR = 11,
			DISK =12

		}  mFileRoute;
		

	private:
		Logger() {

		}
		Logger(const Logger& obj);

		//file Checking  methods
		bool fileExists(const char* file);
		bool fileExists(const std::string& file);

		bool isDirExist(const std::string& path);
		bool CheckAndMkdir(const std::string& path);
		
		static inline char *timenow() {

		    static char buffer[64];
		    time_t rawtime;
		    struct tm *timeinfo;
		    
		    time(&rawtime);
		    timeinfo = localtime(&rawtime);
		    
		    strftime(buffer, 64, "%Y-%m-%d %H:%M:%S", timeinfo);    
		    return buffer;
		}

		static inline char *DayOfMonth() {

		    static char buffer;
		    time_t rawtime;
		    struct tm *timeinfo;
		    
		    time(&rawtime);
		    timeinfo = localtime(&rawtime);
		    
		    strftime(buffer, 1, "%d", timeinfo.tm_mday);    
		    return buffer;
		}




		/*
		1. This method would set the File pointer 
		*/
		
		void setFileDescriptor(FILE_ROUTE aRoute);
		//int printLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...);


	public:
		/*
		#Function Parameter description
		1. This method would take the parameters 
		2. Format
		3. Values to be printed to file

		#Function Return value.
		 Retruns the number of charecters returned.

		#Logic 
			#Step 1
				Once the limit of lines in a file reach the max set limit 
				we will move to next file. If the file allready present we just
				open it in write mode, not in appen mode, and write into it.
			#Step 2 ( check )
				Once we reach the max file limit we re open the first file and write into
				it. and the file is opened in Write mode only. 
		*/
		//int printToLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...);				
		//int printToFile(FILE *ptr,LOGGING_LEVEL loglvl, const char* str, ...);

		int printLog(FILE_ROUTE fileRoute,LOGGING_LEVEL loglvl, const char* str, ...);

		// 
		static Logger *getInstance():


};
#endif


// // To enable the File Logging set to 1
// #define FILE_PRINT 0
// /*  this defines number of file we would cycle with 
// */
// #define MAX_FILES  5
// #define MAX_LINES 1000
// #define FILE_NAME_PREFIX LOG_

// // === auxiliar functions
// static inline char *timenow();

// #define _FILE strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__

// #define NO_LOG          0x00
// #define ERROR_LEVEL     0x01
// #define INFO_LEVEL      0x02
// #define DEBUG_LEVEL     0x03

// #ifndef LOG_LEVEL
// #define LOG_LEVEL   DEBUG_LEVEL
// #endif

// #if  FILE_PRINT
// #define PRINTTOFILE(format, ...) printToFile(format, __VA_ARGS__)
// #endif

// #define PRINTFUNCTION(format, ...)      fprintf(stderr, format, __VA_ARGS__)

// #define LOG_FMT             "%s | %-7s | %-15s | %s:%d | "
// #define LOG_ARGS(LOG_TAG)   timenow(), LOG_TAG, _FILE, __FUNCTION__, __LINE__

// #define NEWLINE     "\n"

// #define ERROR_TAG   "ERROR"
// #define INFO_TAG    "INFO"
// #define DEBUG_TAG   "DEBUG"

// #if LOG_LEVEL >= DEBUG_LEVEL
// #define LOG_DEBUG(message, args...)     PRINTFUNCTION(LOG_FMT message NEWLINE, LOG_ARGS(DEBUG_TAG), ## args)
// #else
// #define LOG_DEBUG(message, args...)
// #endif

// #if LOG_LEVEL >= INFO_LEVEL
// #define LOG_INFO(message, args...)      PRINTFUNCTION(LOG_FMT message NEWLINE, LOG_ARGS(INFO_TAG), ## args)
// #else
// #define LOG_INFO(message, args...)
// #endif

// #if LOG_LEVEL >= ERROR_LEVEL
// #define LOG_ERROR(message, args...)     PRINTFUNCTION(LOG_FMT message NEWLINE, LOG_ARGS(ERROR_TAG), ## args)
// #else
// #define LOG_ERROR(message, args...)
// #endif

// #if LOG_LEVEL >= NO_LOGS
// #define LOG_IF_ERROR(condition, message, args...) if (condition) PRINTFUNCTION(LOG_FMT message NEWLINE, LOG_ARGS(ERROR_TAG), ## args)
// #else
// #define LOG_IF_ERROR(condition, message, args...)
// #endif


