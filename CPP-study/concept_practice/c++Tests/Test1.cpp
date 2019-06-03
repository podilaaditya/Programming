#include<iostream>

using namespace std;
class abc { 

   public: 
      int i; 

      abc(int i) { 
      	cout << " 1 :"<< i << endl ;
         this->i = i;
         cout << " 2 :" << i << endl;
      }
};

int main() { 
   abc m(5); 
   
   cout<< " 3 :" << m.i << endl;

   return 0;
}