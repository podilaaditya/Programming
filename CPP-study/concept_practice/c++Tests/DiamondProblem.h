/*
This class has the set of inheritence stucture for diamond problem.
*/

#include <iostream>


class ParentALevel1 {

public:
    ParentALevel1() {
        std::cout<<" This is ParentALevel1" << std::endl;
    }

};


class ParentBLevel2 :  virtual  ParentALevel1 {

public:
    ParentBLevel2() {
        std::cout<<" This is ParentBLevel2" << std::endl;
    }

};


class ParentCLevel2 : virtual  ParentALevel1{

public:
    ParentCLevel2() {
        std::cout<<" This is ParentBLevel2" << std::endl; 
    }

};



class ChildALevel3 :  public ParentCLevel2 , public ParentBLevel2 {

public:
    ChildALevel3() {
        std::cout<<" This is ChildCLevel3" << std::endl; 
    }

};
