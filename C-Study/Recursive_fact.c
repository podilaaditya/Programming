#include <stdio.h>


//int fact(int);

int fact(int n)
{
    // wrong base case (it may cause
    // stack overflow).
   printf("%d*",n);    
   if (n <= 1) // base case
        return 1;
    else
    	//printf("%d\t",n);    
        return n*fact(n-1);    
}


int main(void) {
  
	printf("\n Value =%d \n",fact(10));

	return 0;
}