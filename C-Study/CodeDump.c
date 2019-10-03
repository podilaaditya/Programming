#include <stdio.h>
#include <string.h>
 
void bar(char* str) 
{
    char buf[4];
    strcpy(buf, str);
}
	 
void foo() 
{
    printf("It survived!");
}
	 
int main(void) 
{
    bar("Longer than 4.");
    foo();
    return 0;
}
