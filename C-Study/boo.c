#include <stdio.h>

int main() {

	int i, j, k, l;
	char *a = (char *) 0;
	int *b = (int *) 0;
	float *c = (float *) 0;
	double *d = 0;
	
	i = (int) (a + 1);
	j = (int) (b + 1);
	k = (int) (c + 1);
	l = (int) (d + 1);
	
	printf("%d, %d, %d, %d\n", (a + 1), (b + 1), (c + 1), (d + 1));
	
	printf("%d, %d, %d, %d\n",a ,b, c, d);
	printf("%d, %d, %d, %d\n",i ,j, k, l);
	return 0;
}
