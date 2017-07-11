package linkedlist;



class DoubblyLinkedList {

		//specfies the place of the node placement
	public static final int AT_DEFAULT = 1;
	public static final int AT_BEGINNING = 1;
	public static final int AT_END = 2;
	public static final int AT_POS = 3;

	public NodeDB mHead;

	public DoubblyLinkedList() {

	}

	public void insertNode( int aPosType, int aPosition, int aData){
		/* 1 & 2: Allocate the Node &
		Put in the data*/
		NodeDB lNewNode = new NodeDB(aData);
		// Check if the head is empty
		// Check the node head if its is not pointing to any thing then or insert in Beginning 
		if(mHead.mNext ==  null || aPosType == AT_BEGINNING || aPosType ==  AT_DEFAULT){
			System.out.println(" -- First Node in the List -- ");				
			/* 3. Make next of new Node as head */
			lNewNode.mNext =  mHead;			
			/* 4. Move the head to point to new Node */
			mHead = lNewNode;

			return;
		} 
		else {
			// End
			if ( aPosType == AT_END )	{							
			    // This would be the last node so we need to set the next to null.	
			    lNewNode.mNext = null;
			    // we need to iterate 
			    NodeDB lLast = mHead;
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
		NodeDB tnode = mHead;
		while (tnode != null)
		{
			System.out.print(tnode.mData+"->");
			tnode = tnode.mNext;
		}
		System.out.println("NULL");    	 

    } 


    public void deleteNode(int aPosType,int aPosition ) {


    }

    public Node getNode() {

    	return null;
    }

    public Node peek() {

    	return null;
    }


}