#include <iostream>
#include <thread>
#include <atomic>
#include <mutex>
#include <set>
using namespace std;

//class MyObserver;

class MyObserver {

  public:
    // This is a method which would be called by MyObservable when he iterates 
    // through list of Observers registered with it.
    // And the class implementing rather extending Oberver class
    virtual void Notify() = 0;
};


class MyObservable {
     static MyObservable* instance;
     set<MyObserver*> observers;
     MyObservable() { };
  public:
     static MyObservable* GetInstance();
     void AddObserver(MyObserver& o);
     void RemoveObserver(MyObserver& o);
     void NotifyObservers();
     void Trigger();
     friend ostream & operator << (ostream &out, const MyObservable &obj); 
};


MyObservable* MyObservable::instance = NULL;

MyObservable* MyObservable::GetInstance()
{
  if ( instance == NULL ) {
    instance = new MyObservable();
  }
  return instance;
}



void MyObservable::AddObserver(MyObserver& o)
{
      observers.insert(&o);
}

void MyObservable::RemoveObserver(MyObserver& o)
{
      observers.erase(&o);
}

void MyObservable::NotifyObservers()
{
      set<MyObserver*>::iterator itr;
          for ( itr = observers.begin();
                        itr != observers.end(); itr++ )
                (*itr)->Notify();
}


ostream & operator << (ostream &out, const MyObservable &c) 
{ 
    out << "c.observers Would need to be print "<< endl; 
    //out << "+i" << c.imag << endl; 
    return out; 
} 

// TEST METHOD TO TRIGGER
// // IN THE REAL SCENARIO THIS IS NOT REQUIRED

void MyObservable::Trigger()
{
      NotifyObservers();
}

class MyClass : public MyObserver {

    MyObservable* observable;

    public:
        MyClass() {
          observable = MyObservable::GetInstance();
          observable->AddObserver(*this);
        }

        ~MyClass() {
           observable->RemoveObserver(*this);
        }

        void Notify() {
            cout << "Received a change event in [MyClass] --> [Observer]" << endl;
        }
};

int main()
{
      MyObservable* observable = MyObservable::GetInstance();
      MyClass* obj = new MyClass();
      cout << observable <<  endl;
      observable->Trigger();

  return 0;
}


