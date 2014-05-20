

public class ProcessControlBlock implements Comparable<ProcessControlBlock>
{
	//int p[];
	int memAddress, jobNum, jobSize, timeIn, priority, maxCpuTime, state, timeOnCpu;	
	boolean isDoingIO, killBit, isBlocked; //kill bit used if job is to be terminated but still doing i/o
	
	public int getTimeOnCpu() 
	{
		return timeOnCpu;
	}

	public void setTimeOnCpu(int timeOnCpu) 
	{
		this.timeOnCpu = timeOnCpu;
	}

	
	
	ProcessControlBlock()
	{
		//p = null;
		memAddress = -1;
		state = 0; //states 0.unassigned 1.new 2.ready 3.running 4.waiting on i/o 5.terminated
		isDoingIO = false;
		killBit = false;
		isBlocked = false;
		this.jobNum = 0;
		this.jobSize = 0;
		this.timeIn = 0;
		this.priority = 0;
		this.maxCpuTime = 0;
	}
	
	ProcessControlBlock(int jobNum, int priority, int jobSize, int maxCpuTime, int timeIn, int state,int timeOnCpu)
	{
		//p = null;
		memAddress = -1;
		this.state = state;
		isDoingIO = false;
		killBit = false;
		isBlocked = false;
		this.jobNum = jobNum;
		this.jobSize = jobSize;
		this.timeIn = timeIn;
		this.priority = priority;
		this.maxCpuTime = maxCpuTime;
		this.timeOnCpu = timeOnCpu;
	}
	
	void setState(int state)
	{
		this.state = state;
	}
	
	int getState()
	{
		return state;
	}
	
	boolean isBlocked()
	{
		return this.isBlocked;
	}
	
	void setBlocked(boolean isBlocked)
	{
		this.isBlocked = isBlocked;
	}
	
	boolean isDoingIO()
	{
		return isDoingIO;
	}
	
	void setDoingIO(boolean isDoingIO)
	{
		this.isDoingIO = isDoingIO;
	}
	
	int getMemLoc()
	{
		return memAddress;
	}
	
	void setMemLoc(int mAddress)
	{
		memAddress = mAddress;
	}
	
//	int[] getP()
//	{
//		return p;
//	}
//	void setP(int incP[])
//	{
//		p = incP.clone();
//	}
//	
	void setJobNum(int jobNum)
	{
		this.jobNum = jobNum;
	}
	
	void setJobSize(int jobSize)
	{
		this.jobSize = jobSize;
	}
	
	void setTimeIn(int timeIn)
	{
		this.timeIn = timeIn;
	}
	
	void setPriority(int priority)
	{
		this.priority = priority;
	}
	
	void setMaxCpuTime(int maxCpuTime)
	{
		this.maxCpuTime = maxCpuTime;
	}
	
	int getJobNum()
	{
		return jobNum;
	}
	
	int getJobSize()
	{
		return jobSize;
	}
	
	int getTimeIn()
	{
		return timeIn;
	}
	
	int getPriority()
	{
		return priority;
	}
	
	int getMaxCpuTime()
	{
		return maxCpuTime;
	}
	
	public int compareTo(ProcessControlBlock node) 
	{
		if (this.priority > node.priority) 
		{
			return 1;
		} 
		else if (this.priority == node.priority) 
		{
			return 0;
		} 
		else 
		{
			return -1;
		}
	}
	
}
