
/*
CircularLinkedList is variation of the Single List. where in the last node would point back to the 
Head.
*/


package linkedlist;




public class CircularLinkedList {

	//specfies the place of the node placement
	public static final int AT_DEFAULT = 1;
	public static final int AT_BEGINNING = 1;
	public static final int AT_END = 2;
	public static final int AT_POS = 3;


	public Node mStartNode;
	public Node mEndNode;
	public Node mHead;

	public CircularLinkedList () {}

	public void insertNode( int aPosType, int aPosition, int aData){
		/* 1 & 2: Allocate the Node &
		Put in the data*/

		System.out.println("--> aData = "+aData);
		Node lNewNode = new Node(aData);

		// Check if the head is empty
		// Check the node head if its is not pointing to any thing then or insert in Beginning 
		if(mHead ==  null && ( aPosType == AT_BEGINNING || aPosType ==  AT_DEFAULT)){			
			System.out.println("--> Createing First Node = ");
			// 1. Create a Node  -- done 
			// 2. Point the Start to the new node --
			// 3. Point the new node to the start.					
			//mHead.mNext =lNewNode; 	
			mHead = lNewNode;
			lNewNode.mNext = mHead;
			mStartNode = mHead;
			return;
		}  
		else if (mHead !=  null &&  ( aPosType == AT_BEGINNING || aPosType ==  AT_DEFAULT)) {
			System.out.println("--> Createing Next Nodes = ");
			// 1. create a new node 
			// 2. set the data and next link (data is passed by the user , next link is head)
			// 3. since we have allready first node which points to the head.
			//while()
		}
		// End
		else if ( aPosType == AT_END )	{							
		 

		}
		else if (aPosition == AT_POS) {


		}	
		

		return;		
	}
    
    public void printLinkedList() {

    	//  iterate through the List and print each nodes data
		Node tnode = mHead;
		while (tnode != null)
		{
			System.out.print(tnode.mData+"->");
			tnode = tnode.mNext;
		}
		//System.out.println("\t Head == "+mHead.mData);    	 
		System.out.println("\t Lastnode == "+tnode);
    } 


    public void deleteNode(int aPosType,int aPosition ) {


    }

    public Node getNode() {

    	return null;
    }

    public Node getPeek() {

    	return null;
    }
	

}