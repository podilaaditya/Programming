
package linkedlist;

public class NodeDB  {
	
	public NodeDB mPrevious;
	public NodeDB mNext;
	int  mData;

	public NodeDB () {

		mNext = null;
		mPrevious = null;
		mData =  0;
	}

	public NodeDB (int aData) {

		mNext = null;
		mPrevious = null;
		mData =  aData;
	}

}