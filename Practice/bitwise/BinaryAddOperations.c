
// C++ code to add add
// one to a given number
#include <stdio.h>

int incrementbyOne(int x)
{
    int m = 1;

    printf("Value of x , m;  =%d , =%d\n",x,m);
    // Flip all the set bits
    // until we find a 0
    while( x & m )
    {
	printf("+++++++++++++ \n");
        x = x ^ m;
  	printf("DBG 1  x ^ m; =%d \n",x);
        m <<= 1;
        printf("DBG 2  m<<=1 = %d \n",m);
	printf("+++++++++++++ \n");
    }

    // flip the rightmost 0 bit
    x = x ^ m;
    printf("DBG 3 x = x ^ m;  =%d \n",x);


    return x;
}

int add2Numbers(int firstNbr,  int seconNbr) {
  return 0;	
}

/* Driver program to test above functions*/
int main()
{
    printf("%d \n", incrementbyOne(10));
    return 0;
}


// in 11
// m = 01

// x = x^m
// x = 10
// m << =1 ; m = 10
