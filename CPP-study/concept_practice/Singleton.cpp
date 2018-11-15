#include<iostream>
#include<atomic>
#include<mutex>
#include <thread> 

using namespace std;

class Singleton {
  private:
        static atomic<Singleton*> mPtrInstance;
        static mutex m_;
  public:
        // Make the constructor as private 
        static Singleton* getInstance();
        
  private:
        // concreate singleton class  
        Singleton() {
           cout << " Create an instance of the Single ton "  << endl ;
        }
};

atomic<Singleton*> Singleton::mPtrInstance  { nullptr };
std::mutex Singleton::m_;

Singleton* Singleton::getInstance() {

  if(mPtrInstance == nullptr) {
    //  lock_guard<mutex> lock(m_);
    m_.lock();
      if(mPtrInstance == nullptr) {
        cout << "create first time" << endl;
        mPtrInstance = new Singleton();
      }
    m_.unlock();  
  }

  return mPtrInstance;

}

void ThfunctionOne(){

   int ch = 0;
   while ( ch < 100 ) {
     cout << " in ThfunctionOne " ;  
     Singleton* foo1 = Singleton::getInstance(); 
     cout << " foo1 =  " << foo1 << endl;
     ch++; 
   }
}
   
 void Thfunctiontwo() {

   int ch = 0;
   while ( ch < 100 ) {
     cout << " in Thfunctiontwo " ;  
     Singleton* foo2 = Singleton::getInstance(); 
     cout << " foo2 =  " << foo2 << endl; 
     ch++; 
   }
}
    
  
  
int main (){
 
  std::thread first (ThfunctionOne);
  std::thread second (Thfunctiontwo);

  std::cout << "main, ThfunctionOne and Thfunctiontwo now execute concurrently...\n";
  
  first.join(); 
  second.join();
  return 0;
}

