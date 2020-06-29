#include <iostream>
#include "queue.h"
#define MAX_QUEUE 10
using namespace std;


int  main(){
  Queue obj(5);
  Queue *queueObj = new Queue(20);
  cout << queueObj;
  obj.enqueue(10);
  obj.enqueue(20);
  obj.enqueue(40);
  obj.enqueue(30);
  obj.enqueue(50);
  obj.enqueue(60);
  obj.enqueue(70);
  obj.enqueue(80);
  obj.enqueue(90);
  cout<<endl << obj <<endl;

  delete queueObj;

  return 0;
}
