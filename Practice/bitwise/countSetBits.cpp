#include <iostream>

using namespace std;


// Function that convert Decimal to binary
// Function that convert Decimal to binary
template <typename T>
int decToBinary(T n)
{

  int i = 0;
  // Size of an integer is assumed to be 32 bits
  i =   sizeof(T) * 8 - 1 ;
  cout <<i<<endl;
  for (; i >= 0; i--) {
    int k = n >> i;
    if (k & 1)
        cout << "1";
    else
        cout << "0";
  }
  cout<<endl;
  return 0;
}


template <typename T>
int countBitsSet(T var) {
  int setCount = 0;
  int len = 0;
  len = sizeof(T) * 8 - 1;
  cout << len << "\n" ;
  for(int cntr = 0;  cntr <= len ;cntr++) {
    if((var&1) ==  1){
      setCount++;
    }
    var = var >> 1;
  }
  return setCount;
}

int main (void) {

  int a = 0xFFF;
  cout << decToBinary(a);
  int count =  countBitsSet(a);
  cout << "number of set bits ="  << count<< "\n";

  return 0;
}
