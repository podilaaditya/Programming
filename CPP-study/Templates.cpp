#include <iostream>
using namespace std;

/* Template Methods (functions )*/

// One function works for all data types.  This would work
// // even for user defined types if operator '>' is overloaded
template <typename T>
T myMax(T x, T y)
{
   return (x > y)? x: y;
}




// Template classes 
/* This class would be able to take any kind primary data type array and provide an operation 
 * encapsulation over it */

template <typename Array>
class ArrayClass {
  private:
    Array *mArrayPtr;
    int mSize; 
    
    // so we are limited by size of the underlying implemenatation of the processor 32 / 64 bit processor
  public:
    ArrayClass(Array aArray[], int aSize);
    int PrintArray();
    
};

template <typename Array>
ArrayClass<Array>::ArrayClass(Array aArray[], int aSize) {
    if(aSize > 0 ) 
    {
      mSize =  aSize;
      mArrayPtr  = new Array[mSize];
      for (int i =0 ; i < mSize ;  i++) {
        
         mArrayPtr[i] = aArray[i];  
      }
    }
    else {
      std::cout << " Cannot create an empty array " ;
    }

}

template <typename Array>
int ArrayClass<Array>::PrintArray(){
 
  if(mSize>0 && mArrayPtr != NULL) {
  
    for(int i =0 ;  i< mSize; i++) {
      std::cout << "\t "  << *(mArrayPtr + i);
       
    }
    std::cout << std::endl;
   } 
   else {
   
     std::cout << " nothing to print man \n" << std::endl; 
     return -1;
   }
  return 0;
}

int main()
{
    cout << myMax<int>(3, 7) << endl;  // Call myMax for int
    cout << myMax<double>(3.0, 7.0) << endl; // call myMax for double
    cout << myMax<char>('g', 'e') << endl;   // call myMax for char
    
    cout << " #############################  \n ";
    
    int arr[5] = {1, 2, 3, 4, 5};
    ArrayClass<int> a(arr, 5);
    a.PrintArray();
    return 0;
}
