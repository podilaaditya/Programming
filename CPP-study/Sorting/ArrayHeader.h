#ifndef ARRAY_HEADER_H_
#define ARRAY_HEADER_H_

#include <iostream>
using std::cout;
using std::endl;

#include <iomanip>
using std::setw;

#include <typeinfo>

// define a clas array of type T
// the type is not know yet and will 
// be defined by instantiation 
// of object of class array<T> from main

/*

 */
template< typename T > class ArrayCls {
	private:
		int mSize;
		T  *mPtrMyArray;
	
	public:
		// constructor with user pre-defined size

		/**
		 * 
		 */
		array (int s) {
			mSize = s;
			mPtrMyArray = new T [size];
		}
		// class array member function to set element of myarray 
		// with type T values
		/**
		 * [setArray description]
		 * @param int aElem : postion of the t
		 * @param T   aVal  : value to be inserted in the array.
		 */
		void addElement ( int aElem, T aVal) {
			myarray[elem] = val;
		}

		//TODO: we have to decide the need for this since it would be a 
		// leak of memory space to the requesting entity. 
		// what if the requesting entity would use the pointer to iterate over the data and change or 

		/**
		 * this would allow the user to get the template array as a pointer 	
		 */
		/*void getArray (void) {


		}*/

		/**
		 * @param
		 * @return
		 */
		bool deleteElement(T aVal) {
			// delete b
			bool 

		}

		
};

#endif