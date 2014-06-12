public class ProcessThreadHandler
{
	private static final int allowableProcesses = 2;
	private static IOProcess[] processes = new IOProcess[allowableProcesses];
	private static Thread[] threads = new Thread[allowableProcesses];
	
    public void startProcess(IOProcess process){
    	boolean full = true;
    	for (int i=0;i<processes.length;i++) {
    		if (processes[i] == null){
    	    	processes[i] = process;
    	    	Thread t = new Thread(process);
    	    	threads[i] = t;
    	    	t.start();
    	        System.out.format("Started process, %s, in thread: %s%n",process.name,t.getName());
    	        full = false;
    	        break;
    		}
    	}
		if (full){
			System.out.format("Could not add process, %s.  " +
					"Allowable processes limit (%d) reached.%n",process.name,allowableProcesses);
		}
    }
    private void stopAll() {
    	for (IOProcess process : processes) {
    		if (process != null){
	    		process.terminate();
    		}
    	}
    }
	public static void main(String[] args) {
		ProcessThreadHandler jt = new ProcessThreadHandler();
		WriteData wObj = new WriteData();
		ReadData rObj = new ReadData();	
		jt.startProcess(wObj);
		jt.startProcess(rObj);
		jt.startProcess(rObj);
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while (run){
			if ((System.currentTimeMillis() - startTime) > 9900){
				System.out.println("Stop all.");
				jt.stopAll();
				break;
			}
		}
	}
}

interface ProcessInterface extends Runnable {
	void terminate();
	void execute();
}

abstract class IOProcess implements ProcessInterface {
	public String name;
	private volatile boolean running = true;
	
	public IOProcess() {
		this.name = this.getName();
		System.out.format("Initialized IOProcess: %s.%n",this.name);
    }
	
	abstract protected String getName();
	
	public void terminate() {this.running = false;}
	
	@Override
	public void run()
	{	
		while (this.running){
			try
			{
				this.execute();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				this.terminate();
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}
}

class WriteData
extends IOProcess {
	public final static String name = "WriteData";
	public String getName() {return WriteData.name;}
	
	public void execute() {System.out.println("write");}
}

class ReadData
extends IOProcess {
	public final static String name = "ReadData";
	public String getName() {return ReadData.name;}
	
	public void execute() {System.out.println("read");}
}