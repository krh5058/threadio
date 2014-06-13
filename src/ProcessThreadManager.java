public class ProcessThreadManager
{
	private static final int maxAllowableProcesses = 2; // Maximum allowable processes
	private IOProcess[] processes = new IOProcess[maxAllowableProcesses];
	private Thread[] threads = new Thread[maxAllowableProcesses]; // 1:1 process-to-thread ratio
	
    public void startProcess(IOProcess process){ // Store and start thread
    	boolean full = true;
    	for (int i=0;i<processes.length;i++) { // Search for allowable slot
    		if (processes[i] == null){  // Allowed!
    	    	(threads[i] = new Thread(processes[i] = process)).start(); // Store process and thread, and start thread
    	        System.out.format("Started process (%d), %s, in thread: %s%n",i,processes[i].name,threads[i].getName()); // Validate process against thread index
    	        full = false;
    	        break; // Leave remaining slots as null
    		}
    	}
		if (full){ // No slot available
			System.out.format("Could not add process, %s. Allowable processes limit (%d) reached.%n",process.name,maxAllowableProcesses);
		}
    }
    private void stopAll() { // Stop all threads
    	System.out.println("Stopping all threads...");
    	for (Thread thread : threads) {
    		if (thread != null){
    			thread.interrupt(); // Terminated process loop
    		}
    	}
    }
	public static void main(String[] args) { // Demonstration purposes
		ProcessThreadManager jt = new ProcessThreadManager();
		boolean run = true;
		long startTime = System.currentTimeMillis();
		jt.startProcess(new ReadData()); // Start another example process
		jt.startProcess(new WriteData()); // Start an example process
		while (run){
			if ((System.currentTimeMillis() - startTime) > 5005){ // Issue process termination after 5 seconds, with some leeway
				jt.stopAll();
				break;
			}
		}
		System.out.println("Done!");
	}
}

interface ProcessInterface extends Runnable { // A loose contract for a process; #1 Intended to be executed by a thread
	void execute(); // #2 Actually does something
}

abstract class IOProcess implements ProcessInterface { // One example of a process: I/O handling
	public String name; // An accessible name
	private volatile boolean running = true; // A flag for run()
	
	public IOProcess() { // Constructor
		System.out.format("Initialized IOProcess: %s.%n",this.name = this.getName());
    }
	// Expected accessor methods
	abstract protected String getName(); // The process must be able to provide a name
	abstract protected int getInterval(); // The process must be able to provide an interval for execution
	
	@Override
	public void run() // Meets interface requirement #1
	{	
		while (this.running){ // Determines the lifetime of the thread
			try // Essential for sleep
			{
				Thread.sleep(this.getInterval()); // Blocking method to free processor
				this.execute(); // Do something
			} catch (InterruptedException e1) { // Catch an interruption
				this.running = false; // Assume interruptions mean to end process.  Possibly clean-up.
			}
		}
	}
}

class WriteData // Skeleton I/O process.  For example, writing data from some buffer (and possibly flush)
extends IOProcess {
	private final static String name = "WriteData"; // A name all processes of this type will be called
	private final static int pace = 1000; // Assume you do not need to write data that often
	
	protected String getName() {return WriteData.name;}
	protected int getInterval() {return WriteData.pace;}
	
	public void execute() {System.out.println("write");} // Meets interface requirement #2

}

class ReadData // Skeleton I/O process.  For example, reading data into a buffer
extends IOProcess {
	private final static String name = "ReadData";
	private final static int pace = 100; // Assume you need to read data fairly often
	
	protected String getName() {return ReadData.name;}
	protected int getInterval() {return ReadData.pace;}
	
	public void execute() {System.out.println("read");}
}