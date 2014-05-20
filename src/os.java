
import java.util.Collections;
import java.util.LinkedList;
//import java.util.Iterator;
//import java.util.List;

public class os 
{

	static LinkedList<FreeSpaceNode> freeSpaceTable;
	static LinkedList<ProcessControlBlock> jobTable;
	static LinkedList<ProcessControlBlock> readyQueue;
	static LinkedList<ProcessControlBlock> iOqueue;
	static boolean diskBusy;
	static boolean drumWorking;
	static ProcessControlBlock beingSwapped;
	static boolean found;
	static boolean printAll= false;
	static ProcessControlBlock toDrum;
	static int currentTime;
	
	/*SOS calls this first
	 * initialize variables
	 * */
	public static void startup()
	{
		freeSpaceTable = new LinkedList<FreeSpaceNode>();
		freeSpaceTable.add(new FreeSpaceNode(0,99));
		jobTable = new LinkedList<ProcessControlBlock>(); //contains all info of jobs in system
		readyQueue = new LinkedList<ProcessControlBlock>();
		iOqueue = new LinkedList<ProcessControlBlock>();
		diskBusy = false;
		sos.ontrace();
		printAll=true;
	}
	
	
	/*
	 * idicates the arrival of new job on drum
	 * sos spooler sends new job to system
	 * p[1] = job number
	 * p[2] = priority 1.highest 5.lowest
	 * p[3] = job size
	 * p[4] = max cpu time allowed
	 * p[5] = current time
	 * */
	public static  void Crint(int []a,int p[])
	{
		
		/* if there is a process that has cpu bookkeep
		 *  - if a[] = 2
		 *  	- save process that has cpu
		 *  		- save state to pcb
		 * else 
		 * call swapper to put jobs from job table to in memory
		 * 	- create new pcb with p[] data
		 *  - put new pcb in jobtable 
		 *  - call swapper to look through job table to find a job to put into memory
		 *  
		 * add job to ready queue
		 * call dispatcher
		 * 
		 * */
		if(printAll)
		{
			System.out.println("crint int called start-------------------------");
			printAll(a,p);
		}

		
		
		bookKeep(a,p);
		jobTable.add(new ProcessControlBlock(p[1],p[2],p[3],p[4],p[5],1,0));
		//printAll(a,p);
		swap(0,p);
		//printAll(a,p);
		dispatch(a,p);
		//return;
		
		
		//call swapper to put job in memory
		//put job in ready queue, maybe do that in swapper?
		
		//before leaving any interrupt a[] p[] must be set
		//a* is state of cpu, 1= no jobs to run, cpu will wait until interrupt, ignore p values
		//a[] = 2, set cpu to run mode, must set p[] values
		/*
		 * p[0],p[1],p[5] are ignored
		 * p[2] = base addres of job to be run(mem address)
		 * p[3] = size
		 * p[4] = time quantom
		 * probably a call to the dispatcher
		 * */
		//leave each interrupt with return statement?

		if(printAll)
		{
			printAll(a,p);
			System.out.println("crint int called end-----------------------");
		}
		return;
		
	}
	
	/*
	* disk interrupt	* 
	 * i/o transfer between disk and memory is complete
	 * p[5] = current time
	 * */
	public static void Dskint(int a[],int p[])
	{
		//before leaving any interrupt a[] p[] must be set
		//a* is state of cpu, 1= no jobs to run, cpu will wait until interrupt, ignore p values
		//a[] = 2, set cpu to run mode, must set p[] values
		/*
		 * p[0],p[1],p[5] are ignored
		 * p[2] = base addres of job to be run(mem address)
		 * p[3] = size
		 * p[4] = time quantom
		 * probably a call to the dispatcher
		 * */
		//leave each interrupt with return statement?

		if(printAll)
		{
			System.out.println("disk int called start-------------------------");
			printAll(a,p);
		}

		
		
		bookKeep(a,p);
		for(ProcessControlBlock e:readyQueue)
		{
			if(e.getJobNum()==p[1])
			{
				e.setBlocked(false);
				e.setDoingIO(false);
				diskBusy = false;
				break;
			}
		}
		swap(0,p);
		dispatch(a,p);
		
		if(printAll)
		{
			printAll(a,p);
			System.out.println("disk int called end---------------------------");
		}

		return;
	}
	
	/*
	 * drum interrupt
	 * program transfer between drum and memory has completed swap
	 * p[5] = current time
	 * */
	public static void Drmint(int a[],int p[])
	{
		//before leaving any interrupt a[] p[] must be set
		//a* is state of cpu, 1= no jobs to run, cpu will wait until interrupt, ignore p values
		//a[] = 2, set cpu to run mode, must set p[] values
		/*
		 * p[0],p[1],p[5] are ignored
		 * p[2] = base addres of job to be run(mem address)
		 * p[3] = size
		 * p[4] = time quantom
		 * probably a call to the dispatcher
		 * */
		//leave each interrupt with return statement?
		if(printAll)
		{
			System.out.println("drum int called start-------------------------");
			printAll(a,p);
		}		

		
		bookKeep(a,p);
		if(toDrum==null)
		{
			for(ProcessControlBlock e:jobTable)
			{
				System.out.println("job table job number "+e.jobNum+"p job number  "+p[1]);
				if(e.jobNum==beingSwapped.getJobNum())
				{
					e.setState(2);				
					for(ProcessControlBlock f:readyQueue)
					{
						if(f.getJobNum()==e.getJobNum())
						{
							f.setState(2);
						}
					}
					System.out.println("added job to ready queue"+e.jobNum+" "+p[1]);
					readyQueue.add(e);				
				}
			}
		}
		else
		{
			toDrum.setMemLoc(-1);
			toDrum.setState(1);
			readyQueue.remove(toDrum);
		}
		
		drumWorking = false;
		swap(0,p);
		dispatch(a,p);

		
		if(printAll)
		{
			printAll(a,p);
			System.out.println("drum int called end---------------------");
		}

		return;
	}
	
	/*called by sos when intervalTimer reaches 0
	 * time run out
	 * timer interrupt
	 * p[5] = current time
	 * */
	public static void Tro(int a[],int p[])
	{
		//System.out.println("tro int called start ----------------------------");
		//before leaving any interrupt a[] p[] must be set
		//a* is state of cpu, 1= no jobs to run, cpu will wait until interrupt, ignore p values
		//a[] = 2, set cpu to run mode, must set p[] values
		/*
		 * p[0],p[1],p[5] are ignored
		 * p[2] = base addres of job to be run(mem address)
		 * p[3] = size
		 * p[4] = time quantom
		 * probably a call to the dispatcher
		 * */
		//leave each interrupt with return statement?

		
		bookKeep(a,p);
		swap(0,p);
		dispatch(a,p);
		//System.out.println("tro int called end ----------------------------");
		return;
	}
	
	
	/*
	 * supervisor call for user program
	 * invoked when the executing job needs service and has just executed a software interrupt instruction
	 * p[5] = current time
	 * a[] = 5 = job has terminated
	 * a[] = 6 = job request disk i/o, sos wants job p[1] to do disk operation (do i/o), add to i/o queue
	 * a[] = 7 = job wants to be blocked until  i/o requests completed (prevented from running on cpu)
	 * 
	 * if job requests to be terminated it while doing i/o it must first finish i/o
	 * instead set kill bit
	 * 
	 * when job terminates it must be removed from jobtable and memory
	 * 
	 * when i/o is being done for a job it must be in core
	 * */
	public static void Svc(int a[],int p[])
	{
		//before leaving any interrupt a[] p[] must be set
		//a* is state of cpu, 1= no jobs to run, cpu will wait until interrupt, ignore p values
		//a[] = 2, set cpu to run mode, must set p[] values
		/*
		 * p[0],p[1],p[5] are ignored
		 * p[2] = base addres of job to be run(mem address)
		 * p[3] = size
		 * p[4] = time quantom
		 * probably a call to the dispatcher
		 * */
		//leave each interrupt with return statement?
		if(printAll)
		{
			System.out.println("svc int called start----------------------------");
			printAll(a,p);
		}

		
		bookKeep(a,p);
		
		if(a[0]==5)
		{
			for(ProcessControlBlock e:jobTable)
			{
				if(e.getJobNum()==p[1])
				{
					
					MemoryManager.free(e.getMemLoc(), e.getJobSize(), freeSpaceTable);
					jobTable.remove(e);
					for(ProcessControlBlock f:readyQueue)
					{
						if(f.getJobNum()==e.getJobNum())
						{
							readyQueue.remove(f);
						}
					}
				}
			}
		}
		else if(a[0]==6)
		{
			for(ProcessControlBlock e:jobTable)
			{
				if(e.getJobNum()==p[1])
				{
					if(!diskBusy)
					{
						sos.siodisk(e.getJobNum());
						e.setDoingIO(true);
					}
					else
					{
						iOqueue.add(e);
						e.setState(4);
						break;
					}
					
				}
			}	
		}
		else if(a[0]==7)
		{
			for(ProcessControlBlock e:readyQueue)
			{
				if(e.getJobNum()==p[1])
				{
					if(e.isDoingIO)
					{
						e.setBlocked(true);
						break;
					}
				}				
			}
		}
		
		swap(0,p);
		dispatch(a,p);
		if(printAll)
		{
			printAll(a,p);
			System.out.println("svc int called end---------------------------------");
		}

		return;
	}

	/*
	 * 
	 * sets cpu registers before context switch
	 * must be called before leaving each interrupt
	 * should set a[] and p[] arguments
	 * a* =1 cpu idle
	 * a* =2 cpu in user ode
	 * p[0],p[1],p[5] are ignored
	 * p[2] = base addres of job to be run(mem address)
	 * p[3] = size
	 * p[4] = time quantom
	 * */
	public static void dispatch(int []a,int p[])
	{
		//if there are jobs
		//call scheduler to decided which job to pick
		//set p to the job values and a to 2 
		//only base address register(p[2]) and size register(p[3]) need to be set
		//else
		//halt cpu -- a[]=2;
		//leave each interrupt with return statement?
		for(ProcessControlBlock e:readyQueue)
		{
			if(e.getState()==p[1]&&!e.isDoingIO())
			{
				e.setTimeOnCpu(e.getTimeOnCpu()+1);
			}	
			if(e.getTimeOnCpu()==e.getMaxCpuTime())
			{
				System.out.println("Deleteing: "+e.getJobNum());
				MemoryManager.free(e.getMemLoc(), e.getJobSize(), freeSpaceTable);					
				readyQueue.remove(e);
				jobTable.remove(e);
			}
		}
		
		
		if(readyQueue.size()>0)
		{
			//a[0] = 2;
			Collections.sort(readyQueue);
			//printAll(a,p);
			for(ProcessControlBlock e:readyQueue)
			{
				if(!e.isBlocked()&&!e.isBlocked())//&&readyQueue.get(index).getJobNum()==p[1]
				{
					p[2] = e.getMemLoc();
					p[3] = e.getJobSize();
					p[4] = 1;
					a[0] = 2;
					e.setState(3);
					//if(!e.isDoingIO())
					//	e.setTimeOnCpu(e.getTimeOnCpu()+1);
					break;
					//jobTable.get(jobTable.indexOf(readyQueue.get(index))).setState(3);
				}
				else
				{
					a[0] = 1;
				}
			}//printAll(a,p);				
		}
		else
		{
			a[0] = 1;
		}
		
		if(printAll)
		{
			printAll(a,p);
		}

		return;
	}
	
	
	/*
	 * swaps jobs between drum and memory
	 * job currently doing i/o cant be swapped out of memory, use latch bit
	 * determine which job that is not in memory should be placed in memory
	 * long term scheduler
	 * calls routine to find space in memory
	 * handles drum interrupts?
	 * call siodrum to swap the job into memory
	 * may need to call swap jobs out to make room for the that you want to swap in
	 * 
	 * siodrum: start a drum transfer (swap)
	 * 		void siodrum(int jobnum,int jobsize, int startcoreaddress(mem),int transfer direction)
	 * 				direction:0= drum to mem, 1= mem to drum
	 * */
	public static void swap(int direction,int p[])
	{
		/*
		 * look through job table for job that is not in memory
		 * 	- check for state is new
		 * use memory manager to allocate memory
		 * 	- send job size
		 * 	-if space found	(addres returned >=0)		
		 * 		- put job in memory
		 * 		- save address in pcb
		 * 		- call siodrum with 0
		 *  -else (addres returned is -2(table not found) or -1(not enough sapce))
		 *  	- use job table to find job with 
		 *  			least priority less than job coming in and 
		 *  			size > than	size of job coming in and
						not latched and 
						not blocked
						if found send it back the drum and free its memory
						then allocate the new job
		 * */
		if(direction==1)
		{
			sos.siodrum(toDrum.getJobNum(),toDrum.getJobSize(),toDrum.getMemLoc(),1);
			drumWorking = true;
			beingSwapped = toDrum;
			toDrum = null;
			found = true;
			
			
		}
		else
		{
			for(ProcessControlBlock e:jobTable)
			{
				if(e.getState()==1&&!drumWorking)
				{
					int memAddress;
					memAddress = MemoryManager.allocate(e.getJobSize(), freeSpaceTable);
					if(memAddress>=0)
					{
						e.setMemLoc(memAddress);
						sos.siodrum(e.getJobNum(),e.getJobSize(),e.getMemLoc(),0);
						drumWorking = true;
						beingSwapped = e;
						found = true;
					}
					else
					{
						found = false;
						System.out.println("need to swap out a job to make space for: " + p[1]);				
					}
				}
			}
		}
		
		if(!found)
		{
			for(ProcessControlBlock e:readyQueue)
			{
				//printCompareTwoJobs(e,jobTable.getLast());
				if( e.getJobSize()>jobTable.getLast().getJobSize()&&
					e.getPriority()<jobTable.getLast().getPriority()&&
					!e.isDoingIO()&&
					!e.isBlocked()&&					
					e.getState()!=3
					)
				{
					toDrum = e;
				}
			}
			
			if(toDrum!=null)
			{
				swap(1,p);
			}
		}
		return;
	}
	
	public static void bookKeep(int []a,int []p)
	{
		
		if(a[0]==2)
		{
			int baseAddress = p[2];
			int jobSize = p[3];
			for (ProcessControlBlock e: jobTable)
			{
				if(e.getMemLoc()==baseAddress&&e.getJobSize()==jobSize)
				{
					e.setState(2);
					//e.setTimeOnCpu(e.getTimeOnCpu()+p[5]-currentTime);
					currentTime = p[5];
					break;
				}
			}
		}
	}
	
	public static void printAll(int a[],int p[])
	{
		
		System.out.println("\n\n---------------------------------------------------------------------------------\n");		
		
		System.out.println("Free Space Tavble:");
		MemoryManager.print(freeSpaceTable);
		
		System.out.println("\n\nReady Queue:");
		for (ProcessControlBlock e:readyQueue)
		{
			printProcessControlBlock(e);			
		}
		
		System.out.println("\n\nJob Table:");
		
		for (ProcessControlBlock e:jobTable)
		{
			printProcessControlBlock(e);			
		}		
		
		System.out.println("\n\nI/O queue:");
		
		for (ProcessControlBlock e:iOqueue)
		{			
			printProcessControlBlock(e);
		}
		
		System.out.println("\na[]: "+ a[0] + "\n");
		
		int x= 0;
		for (int e:p)
		{
			
			System.out.println("p[ " + x +"]: "+ e);
			x++;
		}
		
		System.out.println("\n");
		
		//printCoreMap();
		
		System.out.println("\n---------------------------------------------------------------------------------\n\n");
		return;
	}
	
	public static void printProcessControlBlock(ProcessControlBlock e)
	{
		System.out.println("Job Num: " + e.getJobNum());
		System.out.println("Size: " + e.getJobSize());
		System.out.println("max cpu time: " + e.getMaxCpuTime());
		System.out.println("mem location: " + e.getMemLoc());
		System.out.println("priority: " + e.getPriority());
		System.out.println("state : " + e.getState());
		System.out.println("time in: " + e.getTimeIn());
		System.out.println("time on cpu: " + e.getTimeOnCpu());
		System.out.println("is doing io: " + e.isDoingIO());
		System.out.println("is blocked: " + e.isBlocked());
		System.out.println("");
		return;
	}
	
	static void printCompareTwoJobs(ProcessControlBlock e,ProcessControlBlock f)
	{
		System.out.println("\t\tJob #"+e.getJobNum()+"\tJob #"+f.getJobNum());
		System.out.println("Job size:\t"+e.getJobSize()+"\t"+f.getJobSize());
		System.out.println("Job prior:\t"+e.getPriority()+"\t"+f.getPriority());
		System.out.println("I/O\t\t"+e.isDoingIO());
		System.out.println("blocked\t\t"+e.isBlocked());
		System.out.println("state\t\t"+e.getState());
	}
	
}

