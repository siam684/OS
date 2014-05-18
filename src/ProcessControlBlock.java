

public class ProcessControlBlock implements Comparable<ProcessControlBlock>
{
	//int p[];
	int memAddress, jobNum, jobSize, currentTime, priority, maxCpuTime, state;
	boolean isDoingIO, killBit, isBlocked; //kill bit used if job is to be terminated but still doing i/o
	
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
		this.currentTime = 0;
		this.priority = 0;
		this.maxCpuTime = 0;
	}
	
	ProcessControlBlock(int jobNum, int jobSize, int currentTime, int priority, int maxCpuTime)
	{
		//p = null;
		memAddress = -1;
		state = 0;
		isDoingIO = false;
		killBit = false;
		isBlocked = false;
		this.jobNum = jobNum;
		this.jobSize = jobSize;
		this.currentTime = currentTime;
		this.priority = priority;
		this.maxCpuTime = maxCpuTime;
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
	
	void setCurrentTime(int currentTime)
	{
		this.currentTime = currentTime;
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
	
	int getCurrentTime()
	{
		return currentTime;
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
