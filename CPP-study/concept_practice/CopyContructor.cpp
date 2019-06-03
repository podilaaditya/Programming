#include <iostream>
#include <cstring>
using namespace std;
class CopyConstructor {

	char *s_copy;
	public:
	CopyConstructor(const char *str)
	{
		s_copy = new char[16]; //Dynamic memory allocation
		strcpy(s_copy, str);
	}
	void concatenate(const char *str)
	{
		strcat(s_copy, str); //Concatenating two strings
	}

	CopyConstructor (const CopyConstructor &str)
	{
	    cout<<"Copy Constructor "<<endl;
		s_copy = new char[16]; //Dynamic memory alocation
		strcpy(s_copy, str.s_copy);
	}

	/*
	CopyConstructor& operator = (const CopyConstructor  &t)
	{
	  cout<<"Assignment operator called "<<endl;
        }*/

	~ CopyConstructor () { 
		if(s_copy != NULL) {
			cout << "destrutor" << endl;
			delete  s_copy;
		}
	}
	void display()
	{      if(s_copy != NULL) {
			cout<< s_copy << &s_copy <<  endl;
		}
	}
};

int main()
{
	CopyConstructor c1("Copy");
	CopyConstructor c2=c1; //Copy constructor
	cout <<  "c1  == " << &c1 << endl;
	cout << "c2  == " << &c2 << endl ;

	c1.display();
	c2.display();
	
	c1.concatenate("Constructor"); //c1 is invoking concatenate()
	c1.display();
	c2.display();
        	
	return 0;
}


