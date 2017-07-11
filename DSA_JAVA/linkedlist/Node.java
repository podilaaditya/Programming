package linkedlist;

public class Node {
    
	static int mCount = 0;
	int mData;		
	Node mNext;

	public Node(int d)
	{
		mData = d;
		mNext = null;
	}

	public Node() {
		mData = 0; 
		mNext = null; 
	}

	public static void updateCount() {
		mCount++;
	}

	public static int getNodeCount() {

		return mCount;
	}



}
