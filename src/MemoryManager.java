
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
public class MemoryManager
{
	
	private static FreeSpaceNode tempNode;
	private static ListIterator<FreeSpaceNode> tableIterator;
	
//	MemoryManager()
//	{
//		freeSpaceTable = new LinkedList<FreeSpaceNode>();
//		freeSpaceTable.add(new FreeSpaceNode(0,99));
//	}
//	
	public static int allocate(int sizeOfJob,LinkedList<FreeSpaceNode> freeSpaceTable)
	{
		/*
		 * loop through each element to check for first free space that job can be placed
		 * until end of table. 
		 * */
		int address= -2;
		tableIterator = freeSpaceTable.listIterator();
		while(tableIterator.hasNext())
		{
			tempNode = tableIterator.next();
			
			/*
			 * if found, send size of job and index of node to memSplit
			 * to allocate memory and adjust free space table
			 * */
			if(tempNode.getSize()>=sizeOfJob)
			{
				Collections.sort(freeSpaceTable);
				address =  memSplit(sizeOfJob,freeSpaceTable.indexOf(tempNode),freeSpaceTable);
			}
			else
			{
				System.out.println("no space avaliable");
				address = -1;
			}
		}
		System.out.println("Address of job: "+ address);
		print(freeSpaceTable);
		return address;
		
	}
	
	public static void free(int address, int size,LinkedList<FreeSpaceNode> freeSpaceTable)
	{	
		freeSpaceTable.add(new FreeSpaceNode(address,size));
		Collections.sort(freeSpaceTable);
		memCoalesce(freeSpaceTable);
	}
	
	private static void memCoalesce(LinkedList<FreeSpaceNode> freeSpaceTable)
	{
		int size = freeSpaceTable.size()-1;
		while((size>0))
		{
			if(freeSpaceTable.get(size-1).getAddress()+freeSpaceTable.get(size-1).getSize()==freeSpaceTable.get(size).getAddress())
			{
				int sizeOfadjacent = freeSpaceTable.get(size).getSize();
				freeSpaceTable.remove(size);
				freeSpaceTable.get(size-1).setSize(sizeOfadjacent+freeSpaceTable.get(size-1).getSize());
			}
			size--;
		}
	}
	
	private static int memSplit(int sizeOfJob, int index,LinkedList<FreeSpaceNode> freeSpaceTable)
	{
		tempNode = freeSpaceTable.remove(index);		
		freeSpaceTable.add(new FreeSpaceNode((tempNode.getAddress())+sizeOfJob,tempNode.getSize()-sizeOfJob));
		return tempNode.getAddress();
	}
	
	public static void print(LinkedList<FreeSpaceNode> freeSpaceTable)
	{
		tableIterator = freeSpaceTable.listIterator();
		while(tableIterator.hasNext())
		{
			System.out.println("Address: "+ tableIterator.next().getAddress() + " Size: " + tableIterator.previous().getSize());
			tableIterator.next();
		}
	}
}
