#include <iostream>
using namespace std;

// Returns true if n is even, else odd
bool isEven(int n)
{
    // n&1 is 1, then odd, else even
    cout << ((n&1)) << "\n" ;
    return ((n & 1));
}

// Driver code
int main()
{
    int n = 11;
    //1010
    isEven(n)? cout << "Odd \n": cout << "Even \n";
    return 0;
}
