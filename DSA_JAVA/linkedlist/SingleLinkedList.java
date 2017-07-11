package linkedlist;

public class SingleLinkedList {

	//specfies the place of the node placement
	public static final int AT_DEFAULT = 1;
	public static final int AT_BEGINNING = 1;
	public static final int AT_END = 2;
	public static final int AT_POS = 3;


	public Node mHead;

	public void insertNode( int aPosType, int aPosition, int aData){
		/* 1 & 2: Allocate the Node &
		Put in the data*/
		Node lNewNode = new Node(aData);
		// Check if the head is empty
		// Check the node head if its is not pointing to any thing then or insert in Beginning 
		if(mHead ==  null || aPosType == AT_BEGINNING || aPosType ==  AT_DEFAULT){
			
			/* 3. Make next of new Node as head */
			lNewNode.mNext =  mHead;			
			/* 4. Move the head to point to new Node */
			mHead = lNewNode;

			Node.updateCount();

			System.out.println(" --  "+ Node.getNodeCount() + " Node in the List -- ");				

			return;
		} 
		else {
			// End
			if ( aPosType == AT_END )	{							
			    // This would be the last node so we need to set the next to null.	
			    lNewNode.mNext = null;
			    // we need to iterate 
			    Node lLast = mHead;
			    /*   traverse till the last node */
    			while (lLast.mNext != null)
        			lLast = lLast.mNext;
        		/*  Change the next of last node */
        		lLast.mNext = lNewNode;
			}

			else if (aPosition == AT_POS) {

			}	
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
		System.out.println("Head == "+mHead.mData);    	 

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
