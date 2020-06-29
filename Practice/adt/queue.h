#include <iostream>

#define MAX_QUEUE 10
using namespace std;

class Queue {

  public :
    int rear, front;
    int *data =  NULL;
    int capacity;
    int count;  	// current size of the queue

  public :
    Queue(int size = MAX_QUEUE); // set the default value to
    ~Queue();				   // destructor

  public :
      // operations on the Queue
      void dequeue();
    	void enqueue(int x);
    	int peek();
    	int size();
    	bool isEmpty();
    	bool isFull();

      friend std::ostream& operator << (std::ostream &out, const Queue &obj);
};

Queue::Queue(int size) {
  data = new(nothrow) int[size];
  if(data != NULL) {
    capacity = size;
    front = 0;
    rear = -1;
    count = 0;
    for(int i =0 ; i < capacity ; i++) {
      data[i] = 0;
    }

  }
  else {
    cout << "failed to allocate memory";
  }
}

// Destructor to free memory allocated to the queue
Queue::~Queue()
{
	delete[] data;
}

std::ostream& operator << ( ostream &output, const Queue &obj ) {
   //output << "F : " << D.feet << " I : " << D.inches;
  output<< endl << "Printing the content of queue " <<endl;
  for(int i= 0 ;  i < obj.capacity ; i++){
    output << obj.data[i] << "\t";
  }
  output << endl;
  return output;
}



int Queue::size()
{
	return this->count;
}

// Utility function to check if the queue is empty or not
bool Queue::isEmpty()
{
  if(size() == 0)
    return (true);
  else
    return (false);
}

// Utility function to check if the queue is full or not
bool Queue::isFull()
{
  if(size() == capacity  )
    return (true);
  else
    return (false);

}

void Queue::enqueue(int a) {
  // check for queue overflow
	if (isFull())
	{
		cout << "OverFlow\nProgram Terminated\n";
		return;
	}
	cout << "Inserting " << a << '\n';
	rear = (rear + 1) % capacity;
	data[rear] = a;
	count++;

}


void Queue::dequeue(){
  // check for queue underflow
	if (isEmpty())
	{
		cout << "UnderFlow\nProgram Terminated\n";
		exit(EXIT_FAILURE);
	}

	cout << "Removing " << data[front] << '\n';
	front = (front + 1) % capacity;
	count--;
}
