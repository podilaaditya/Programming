#ifndef ARRAY_HEADER_H_
#define ARRAY_HEADER_H_

#include <iostream>
using std::cout;
using std::endl;

#include <iomanip>
using std::setw;

#include <typeinfo>

#include "Constants.h"
// 1. define a clas array of type T
// 2. T type is not know yet and will defined 
// during creation of object of object of class array<T> from main

template< typename T > class ArrayCls {
	private:
		int mSize;
		T  *mPtrMyArray;
	
	public:
		// constructor with user pre-defined size

		inline array (int aSize); 

		/**
		 * [resizeArray description]
		 * @param  aNewSize [description]
		 * @return          [description]
		 */		
		bool resizeArray(int aNewSize);

			// we need to check if the older array is allocated memory
			// only then we will resize // create a new array copy all 
			// the contents to it an remove the previous content or array 
			// by calling delete on the array
		

// Operations on the elements.
		/**
		 * [addElement description]
		 * @param int aElem : postion of the t
		 * @param T   aVal  : value to be inserted in the array.
		 */
		inline void addElement ( int aElem, T aVal);

		/**
		 * [deleteElement description]
		 * @param  aVal [description]
		 * @return      [description]
		 */
		inline bool deleteElement(T aVal);

		/**
		 * [swapElements description]
		 * @param  aLeft  [description]
		 * @param  aRight [description]
		 * @return        [description]
		 */
		inline bool swapElements(int aLeft,  int aRight);

		/**
		 * [mergeArray description]
		 * @param  aPtrLeft  [description]
		 * @param  aPtrRight [description]
		 * @return           [description]
		 */
		inline T* mergeArray(T *aPtrLeft, T *aPtrRight); 

//#######################################
//Standard getter and setter methods bellow. 
		/**
		 * [getSizeOfArray description]
		 * @return  [description]
		 */
		 int getSizeOfArray(void);

		/**
		 * [getElementPos :: 
		 * This mehod would return the first ocurance of the value 
		 * provided in the array/ Array is not an auto incrementing array, since 
		 * this would be defined by the size we pass when we are creating the array .]
		 * 
		 * @param  Val [description]
		 * @return     [description]
		 */
		int getElementPos(T Val);



};

#endif // for #ifndef ARRAY_HEADER_H_