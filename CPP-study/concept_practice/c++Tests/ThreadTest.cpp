#include <iostream>
#include <thread> 
using namespace std;
void threadFunc()
{
	cout << "Welcome to Multithreading" << endl;

}
int main()
{
	//pass a function to thread
	thread funcTest1(threadFunc);

	funcTest1.join();
}
