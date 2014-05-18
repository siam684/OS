



public class FreeSpaceNode implements Comparable<FreeSpaceNode>
{
	private int address;
	private int size;
	
	FreeSpaceNode(int address, int size)
	{
		 this.address = address;
		 this.size = size;
	}
	
	int getSize()
	{
		return size;
	}
	
	int getAddress()
	{
		return address;
	}
	
	void setAddress(int address)
	{
		this.address = address;
	}
	
	void setSize(int size)
	{
		this.size = size;
	}
	
	@Override
	public int compareTo(FreeSpaceNode node) 
	{
		if (this.address > node.address) 
		{
			return 1;
		} 
		else if (this.address == node.address) 
		{
			return 0;
		} 
		else 
		{
			return -1;
		}
	}
}
