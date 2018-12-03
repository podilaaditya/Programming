#ifndef  __GDS_H__
#define  __GDS_H__

#include "ArrayHeader.h"

//This is a General Data structure 
// Which takes a template Array and then perform  the 
// Operations like add, pop, modify and 
// Search  and  Sort classes which will be added as an iterator 
template< class T > 
class GDS {
 	
public:

	typedef enum GDS_TYPE
	{
		GDS_INT = 1,
		GDS_FLOAT,
		GDS_STRING,
		GDS_STRUCTURE
	} GDSTYPE;

	inline GDS(GDSTYPE aType){

	}

	inline GDS(GDSTYPE aType){
		
	}

    /**
     * @param
     * @param
     * @return
     */
	bool pushElement(GDSTYPE aType, T aValue);

    /**
    * @param
    * @param
    * @return
    */
	bool popElement(GDSTYPE aType, T aValue);

	/**
	 * @return
	 */
	bool sortGDS();

	/**
	 * @return
	 */
	int searchGDS();

}

#endif // for #ifndef  __GDS_H__