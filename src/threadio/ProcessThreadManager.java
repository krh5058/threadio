package threadio;

import java.io.IOException;

public class ProcessThreadManager
{
	private static final int maxAllowableProcesses = 10; // Maximum allowable processes
	private IOProcess[] processes = new IOProcess[maxAllowableProcesses];
	private Thread[] threads = new Thread[maxAllowableProcesses]; // 1:1 process-to-thread ratio
	private Thread startThread;
	
//	public void storeProcess(IOProcess ... inProcesses){ // Store and start thread
	public void storeProcess(IOProcess process){ // Store and start thread
//		for (IOProcess process : inProcesses){
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
//				break;
			}
//		}
    }
	public void removeAll(){ // Store and start thread
		for (int i=0;i<processes.length;i++) { // Search for allowable slot
			if (processes[i] != null){
				processes[i] = null;
				threads[i] = null;
			}
		}
    }
	public void startAll(int ... stagger){
		if (stagger.length!=0){ 
			System.out.println("Starting (staggered) all threads...");
			StartThreads ST = new StartThreads(threads,stagger[0]);
			this.startThread = new Thread(ST);
			this.startThread.start();
//			for (Thread thread : threads) {
//				if (thread != null){
//					thread.start(); // Start thread
//					try {
//					    Thread.sleep(stagger[0]);
//					} catch(InterruptedException ex) {
//					    Thread.currentThread().interrupt();
//					}
//				}
//			}
		} else {
			System.out.println("Starting all threads...");
			for (Thread thread : threads) {
				if (thread != null){
					thread.start(); // Terminated process loop
				}
			}
		}
	}
	public void stopAll() { // Stop all threads
    	System.out.println("Stopping all threads...");
    	if (this.startThread != null) {
    		this.startThread.interrupt();
    	}
    	for (Thread thread : threads) {
    		if (thread != null){
    			thread.interrupt(); // Terminated process loop
    		}
    	}
    }
	public static void main(String[] args) throws IOException { // Demonstration purposes
		ProcessThreadManager jt = new ProcessThreadManager();
//		WriteData wd = new WriteData();
		WriteData wd1 = new WriteData(500);
		WriteData wd2 = new WriteData(500);
		jt.storeProcess(wd1);
		jt.storeProcess(wd2);
		wd1.openBufferedOutputStream("./test1.txt");
		wd2.openBufferedOutputStream("./test2.txt");
		wd1.appendToBuffer(1);
		wd1.appendToBuffer(2);
		wd1.appendToBuffer(3.3213);
		wd2.appendToBuffer(2);
		wd2.appendToBuffer(4);
		wd2.appendToBuffer(5.3213);
		jt.startAll(6000);
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while (run){
			if ((System.currentTimeMillis() - startTime) > 7000){ // Issue process termination after 7 seconds, with some leeway
				jt.stopAll();
				break;
			}
		}
		jt.removeAll();
		System.out.println("Done!");
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

class StartThreads implements ProcessInterface{
	private Thread[] threads;
	private int stagger;
	public StartThreads(Thread[] threads,int stagger) { // Constructor
		System.out.println("Initialized StartThreads.");
		this.threads = threads;
		this.stagger = stagger;
    }

	@Override
	public void run() {
		for (Thread thread : this.threads) {
			if (thread != null){
				try {
					thread.start();
					Thread.sleep(this.stagger);
				} catch(InterruptedException ex) {
					this.cleanup();
					break;
				}
			}
		}
	}

	@Override
	public void execute() {}

	@Override
	public void cleanup() {
    	for (Thread thread : this.threads) {
    		if (thread != null){
    			thread.interrupt(); // Terminated process loop
    		}
    	}
		this.threads = null;
		this.stagger = 0;
	}
}
