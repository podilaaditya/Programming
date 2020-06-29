#include<stdio.h>
#include<string.h>
#include<stdlib.h>
//to remove the extra occurences in a string
void reverse(char *p)
{
	char temp;
	int i,j,temp1=0,temp2=0;
	int l=strlen(p)-1;
	printf("DBG 1 : l =%d",l);
	for(i=0,j=l;i<j;i++,j--) {
		temp=p[i];
		p[i]=p[j];
		p[j]=temp;
	}

	for(i=0;p[i];i++) {
		temp1=i;
		while(p[i]!=32) {
			if(p[i]=='\0')
				break;

			temp2=i;
			i++;
		}
		for(;temp1<temp2;temp1++,temp2--) {
			temp=p[temp1];
			p[temp1]=p[temp2];
			p[temp2]=temp;
		}
	}

	p[i]=0;
	printf("The reversed string is :\n");
	puts(p);
}
void getString(char **string) {

    int size=0;
    char ch;
    char *str=NULL;
    str=(char*)malloc((size+1)*sizeof(char));
    while((ch=getchar())!='\n') {
        str=realloc(str,(size+1)*sizeof(char));
        str[size]=ch;
        size++;
    }
    str[size]='\0';
    *string=str;
    return;

}
int main()
{
	char *str;
	printf("enter the  string the to be reversed  \n");
	//fgets(str,30,stdin);
	getString(&str);
	//str[strlen(str)-1]='\0';
	printf("the given string is : \n");
	puts(str);
	reverse(str);
	return 0;
}

