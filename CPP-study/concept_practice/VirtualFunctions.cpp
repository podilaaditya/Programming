#include <stdio.h>
#include <string.h>

class BaseClass
{
    private:
        char Buffer[32];
    public:
        void SetBuffer(char *String)
        {
            strcpy(Buffer,String);
        }
        virtual void PrintBuffer(char *p)
        {
            printf("%s\n",p);
            //printf("%s\n",Buffer);
        }
};

class MyClass1:public BaseClass
{

    public:
        using BaseClass::PrintBuffer;
        void PrintBuffer(char *p)
        {
  
            printf("MyClass1: PrintBuffer \n");
            BaseClass::PrintBuffer("Base from C1");
        }
};

class MyClass2:public BaseClass
{
    public:
        using BaseClass::PrintBuffer;
        void PrintBuffer(char *p)
        {
            printf("MyClass2: PrintBuffer\n");
            BaseClass::PrintBuffer("Base from C2");
        }
};

int main()
{
    BaseClass *Object[2];

    Object[0] = new MyClass1;
    Object[1] = new MyClass2; 
    Object[0]->SetBuffer("string1");
    Object[1]->SetBuffer("string2");
    Object[0]->PrintBuffer(NULL);
    Object[1]->PrintBuffer(NULL);

    return 0;
}
