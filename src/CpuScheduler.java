

import java.util.Collections;
import java.util.LinkedList;
//import java.util.ListIterator;

public class CpuScheduler
{
	//private static ListIterator<ProcessControlBlock> queueIterator;
	
	public static int nextJob(LinkedList<ProcessControlBlock> readyQueue)
	{
		int index;
		Collections.sort(readyQueue);	
		index =  readyQueue.indexOf(readyQueue.peekFirst());
//		for(ProcessControlBlock e: readyQueue)
//		{
//			e.setPriority(e.getPriority()+1);
//		}
		return index;
	}
	
	public void addtoQueue(ProcessControlBlock inProcess, LinkedList<ProcessControlBlock> readyQueue)
	{
		readyQueue.add(inProcess);		
		Collections.sort(readyQueue);
	}
	
	public static void removeFromQueue(ProcessControlBlock outProcess, LinkedList<ProcessControlBlock> readyQueue)
	{
		readyQueue.remove(readyQueue.indexOf(outProcess));
	}
	
	public static void removeFromQueue(LinkedList<ProcessControlBlock> readyQueue)
	{
		readyQueue.remove();
	}
}
