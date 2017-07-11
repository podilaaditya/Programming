#include "Common.h"

void print_trace (void);
void handler(int sig);
int main (void) 
{
	signal(SIGSEGV, print_trace);   // install our handler

	char u8buffer[10]; //the right end of the array is 9
	u8buffer[10]="d";
	//fprintf(stderr, "Error: signal %d:\n", sig);
	printf("u8buffer = %s\n", u8buffer);

	return 0;	
}



/* Obtain a backtrace and print it to stdout. */
void print_trace (void)
{
	void *array[10];
	size_t size;
	char **strings;
	size_t i;

	size = backtrace (array, 10);
	strings = backtrace_symbols (array, size);

	printf ("Obtained %zd stack frames.\n", size);

	for (i = 0; i < size; i++)
	  printf ("%s\n", strings[i]);

	free (strings);
}


void handler(int sig) {
  void *array[10];
  size_t size;

  // get void*'s for all entries on the stack
  size = backtrace(array, 10);

  // print out all the frames to stderr
  fprintf(stderr, "Error: signal %d:\n", sig);
  backtrace_symbols_fd(array, size, STDERR_FILENO);
  exit(1);
}
