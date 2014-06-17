package threadio;

import java.io.IOException;

public class ProcessThreadManager
{
	private static final int maxAllowableProcesses = 2; // Maximum allowable processes
	private IOProcess[] processes = new IOProcess[maxAllowableProcesses];
	private Thread[] threads = new Thread[maxAllowableProcesses]; // 1:1 process-to-thread ratio
	
	public void storeProcess(IOProcess process){ // Store and start thread
    	boolean full = true;
    	for (int i=0;i<processes.length;i++) { // Search for allowable slot
		if (processes[i] == null){  // Allowed!
			threads[i] = new Thread(processes[i] = process); // Store process and thread
			System.out.format("Stored process (%d), %s, in thread: %s%n",i,processes[i].name,threads[i].getName()); // Validate process against thread index
				full = false;
    	        break; // Leave remaining slots as null
    		}
    	}
		if (full){ // No slot available
			System.out.format("Could not add process, %s. Allowable processes limit (%d) reached.%n",process.name,maxAllowableProcesses);
		}
    }
	public void removeAll(){ // Store and start thread
		for (int i=0;i<processes.length;i++) { // Search for allowable slot
			if (processes[i] != null){
				processes[i] = null;
				threads[i] = null;
			}
		}
    }
	public void startAll(){
    	System.out.println("Starting all threads...");
    	for (Thread thread : threads) {
    		if (thread != null){
    			thread.start(); // Terminated process loop
    		}
    	}
	}
	public void stopAll() { // Stop all threads
    	System.out.println("Stopping all threads...");
    	for (Thread thread : threads) {
    		if (thread != null){
    			thread.interrupt(); // Terminated process loop
    		}
    	}
    }
	public static void main(String[] args) throws IOException { // Demonstration purposes
		ProcessThreadManager jt = new ProcessThreadManager();
		WriteData wd = new WriteData();
		wd.openBufferedOutputStream("./test.txt");
		jt.storeProcess(wd);
		wd.appendToBuffer(4.0);
		wd.appendToBuffer(1.141243);
		wd.appendToBuffer(5.05454);
		wd.appendToBuffer(54545351.2);
		jt.startAll();
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while (run){
			if ((System.currentTimeMillis() - startTime) > 2000){ // Issue process termination after 5 seconds, with some leeway
				jt.stopAll();
				break;
			}
		}
		jt.removeAll();
//		System.out.println("Done!");
	}
}

interface ProcessInterface extends Runnable { // A loose contract for a process
	void execute(); // Do something
	void cleanup();
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
				this.cleanup();
				this.running = false; // Assume interruptions mean to end process.  Possibly clean-up.
			}
		}
	}
}
