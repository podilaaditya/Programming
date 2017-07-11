/*
TODO: Would need to include the GNU LGPL and any requirequired licence here 
*/


// This Header will give commoon encapsulation for the POSIX standards
// Including this header i would be able to get all methods i would require to use
#ifndef COMMON_OS
#define COMMON_OS

//This would go into the 
#define USE_SYSTEM_CALL 1

//Basic functions and APi which are part of the C Language
//Decission for not using C++ would be changed later by using a proper constucts for using C++ compiler

//Standard and Libc Headers
#include <stdio.h>
#include <unistd.h>
//Include the custom headers here
#include "Error.h"

#ifdef USE_SYSTEM_CALL
#include <stdlib.h>
#endif 


//Print the POSIX Version
int printPOSIXVersion();
int printPOSIXApi();


//Returns the Process Id of the Parent 
int returnParentProcId();

//Returns the Process ID of the current running or rather Process ID of Process which excutes this Method
int returnProcId();

//Updates the Array Integer Array with list of process which are the child of this process
int returnChildProcIds(unsigned int *ptrProcIdList);

//this function intends to print all the Content in the Curent Process
int printProcStructure();

//Print all the Files opened in the current Process
int printOpenFiles();




//This

#endif