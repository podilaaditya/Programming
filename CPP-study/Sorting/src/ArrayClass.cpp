#include "ArrayHeader.h"

template< typename T > 
ArrayCls<T>::array (int aSize) {
	
	if (ISNEG(aSize) && aSize !=0 ) {
		mSize = aSize;				
		mPtrMyArray = new T [mSize]; 
	} else {
		mSize =  DEFAULT_ARRAY_SIZE; 
		mPtrMyArray = new T [mSize]; 
	}
}


template< typename T > 
int ArrayCls<T>::getSizeOfArray(void) {
	return this.mSize;
}

/**
 * [getElementPos :: 
 * This mehod would return the first ocurance of the value 
 * provided in the array/ Array is not an auto incrementing array, since 
 * this would be defined by the size we pass when we are creating the array .]
 * 
 * @param  Val [description]
 * @return     [description]
 */
template< typename T > 
int ArrayCls<T>::getElementPos(T Val) {

}


/**
 * [resizeArray description]
 * @param  aNewSize [description]
 * @return          [description]
 */
template< typename T > 
bool ArrayCls<T>::resizeArray(int aNewSize) {

	// we need to check if the older array is allocated memory
	// only then we will resize // create a new array copy all 
	// the contents to it an remove the previous content or array 
	// by calling delete on the array
}

// Operations on the elements.
/**
 * [addElement description]
 * @param int aElem : postion of the t
 * @param T   aVal  : value to be inserted in the array.
 */
template< typename T > 
bool ArrayCls<T>::addElement ( int aElem, T aVal) {
	myarray[elem] = val;
}

/**
 * @param
 * @return
 */

template< typename T > 
bool ArrayCls<T>::deleteElement(T aVal) {
	// delete b
	bool isDeleted = false;
}

/**
 * [swapElements description]
 * @param  aLeft  [description]
 * @param  aRight [description]
 * @return        [description]
 */
template< typename T > 
bool ArrayCls<T>::swapElements(int aLeft,  int aRight) {
	// this method would get the element from position  provided in left 
	// and swap with the position provided in right 
}

/**
 * [mergeArray description]
 * @param  aPtrLeft  [description]
 * @param  aPtrRight [description]
 * @return           [description]
 */

template< typename T > 
T* ArrayCls<T>::mergeArray(T *aPtrLeft, T *aPtrRight) {
	// this methods would take 2 arrays and merge them into one 
	// and return the  pointer to the newly created array ( memory 
	// allocated on heap)
	//1. get the sum of sizes of the left, right array
	int lSizeOfMergedArray = aPtrLeft->getSizeOfArray() + aPtrRight->getSizeOfArray();
	

}

