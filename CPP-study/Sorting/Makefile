# this would be first make file for the project called sorting 
# What  this file would  contain 


#Common cpp flags and dependencies
# 1.Compiler 
CC=g++
$(info $(CC))
# 2. CPP FLags  // Add further stuff as needed.  
CFLAGS=-c -Wall -std=c++11
$(info $(CFLAGS))
# Linker FLags
LDFLAGS= 
$(info $(LDFLAGS))


#3. Source Folder 
SOURCEDIR=./src
$(info $(SOURCEDIR))
#4. Header folder 
HEADERDIR=./inc
$(info $(HEADERDIR))
#object dir
OBJDIR=./obj
$(info $(OBJDIR))
#generated Binary Dir
BINDIR=./bin
$(info $(BINDIR))



#Prove the  Headers
INCLUDES := $(wildcard $(HEADERDIR)/*.h)
$(info $(INCLUDES))

#Provide the Source Files 
SOURCE = $(wildcard $(SOURCEDIR)/*.cpp)
$(info $(SOURCE))


