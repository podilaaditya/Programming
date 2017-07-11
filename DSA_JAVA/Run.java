import linkedlist.*;


class Run {

	public static final int AT_DEFAULT = 1;
	public static final int AT_BEGINNING = 1;
	public static final int AT_END = 2;
	public static final int AT_POS = 3;

	public static void main(String [] args)
		{
			CircularLinkedList llist = new CircularLinkedList();
			for (int i=5; i>0; --i)
			{
				llist.insertNode(AT_DEFAULT,0,i);
				System.out.println(" -- List As Of Now -- ");
				
				//llist.printMiddle();
			}

			llist.printLinkedList();
		}
}