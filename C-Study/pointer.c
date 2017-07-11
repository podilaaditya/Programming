#include <stdio.h>
#include <stdlib.h>

struct Test
{
	int a;
	char b;
	/* data */
};


struct flexarray
{
    char val;
    int array[];  /* Flexible array member; must be last element of struct */
};

int main(void)
{

 //Structure Pointers	
 struct Test *s ;	
 
 double *p;
 
 int const a = 10;
 int b =20;
 
 //constant usage in pointer declaration
 const int *ptrA = &a;
 const int * const ptrB = &b;
 //End for Const 
 char *ptr = "Test";
 //ptr[2] = "Z";

 printf("Value Ptr = %s\n", ptr);

  printf("Size of pointer s %d\n",sizeof (s));
  printf("Size of sizeof structure s points to (*s) %d\n",sizeof (*s));
  printf("Size of Structure \"struct Test\"  %d\n",sizeof (struct Test));
  printf("Size of int  %d\n",sizeof (int));
  printf("Size of pointer %d\n",sizeof (p));
  printf("Size of type to which P points to  %d\n",sizeof (*p));
  printf("sizeof (struct flexarray) = %zu\n", sizeof (struct flexarray));

 printf("Value at address pointer by ptrA %d\n", *ptrA);
 //*ptrA = 30;
 //printf("after change Value at address pointer by ptrA %d\n", *ptrA);

 printf("Value at address pointer by ptrB %d\n", *ptrB);
 //ptrB = &a;
 //printf("Value at address pointer by ptrB %d\n", *ptrB);

 return 0;

}