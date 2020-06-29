// CPP program to Decimal to binary conversion
// using bitwise operator
// Size of an integer is assumed to be 32 bits
#include <iostream>
using namespace std;

// Function that convert Decimal to binary
int decToBinary(int n)
{

  int i = 0;
  // Size of an integer is assumed to be 32 bits
  i =   sizeof(int) * 8 - 1 ;
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


// Function that reverse the binary data
int reversed(int num)
{

  unsigned int  NO_OF_BITS = sizeof(num) * 8 ;
  unsigned int reverse = 0, i, temp;

  for (i = 0; i < NO_OF_BITS; i++) {
      temp = (num & (1 << i));
      if(temp) {
          //reverse |= (1 << ((NO_OF_BITS - 1) - i));
          reverse |= (1 << ((NO_OF_BITS - 1) - i));
        }
  }
  decToBinary(reverse);
  cout << reverse << endl;
  cout<<endl;
  return 0;
}


// Circular shift
int CircularShift(int &num, int shiftCount, int rightOrLeft) {
  int bitCount =  sizeof(int) * 8;
  int cyclicReverse = 0;
  if(rightOrLeft) {
    // right shift
    // 0xFF  shiftcount = 8
    // 0xFF >> shiftcount
    decToBinary(num);
    decToBinary((num >> shiftCount));
    decToBinary(num << (bitCount - shiftCount));
    cyclicReverse = (num >> shiftCount) | (num << (bitCount - shiftCount));
    decToBinary(cyclicReverse);

  }
  else { //lefy shift
    decToBinary(num);
    decToBinary(num << shiftCount);
    decToBinary(num >> (bitCount  - shiftCount));
    cyclicReverse = (num << shiftCount) | (num >> (bitCount  - shiftCount));
    decToBinary(cyclicReverse);
  }
 return cyclicReverse;
}

// A utility function to count set bits
// in a number x
// A utility function to count set bits
// in a number x
unsigned int countSetBitsUtil(unsigned int x)
{
    if (x <= 0)
        return 0;
    return (x % 2 == 0 ? 0 : 1) + countSetBitsUtil(x / 2);
}
// Returns count of set bits present in all
// numbers from 1 to n
unsigned int countSetBits(unsigned int n)
{
    int bitCount = 0; // initialize the result

    for (int i = 1; i <= n; i++)
        bitCount += countSetBitsUtil(i);

    return bitCount;
}

// driver code
int main()
{
    int n = 0x7F;  //00000000 00000000 00000000 00001111
    decToBinary(n);
    reversed(n);
    signed int b = 0xFFFFFFFF;

    //
    cout << b << endl;
    decToBinary(b);
    reversed(b);

    //
    CircularShift(n,4,1);
    cout << "============" << endl;
    CircularShift(n,4,0);

    return 0;
}
