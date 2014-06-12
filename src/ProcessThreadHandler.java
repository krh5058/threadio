public class ProcessThreadHandler
{
	private static final int allowableProcesses = 2;
	private static IOProcess[] processes = new IOProcess[allowableProcesses];
	private static Thread[] threads = new Thread[allowableProcesses];
	
    public void startProcess(IOProcess process){
    	for (int i=0;i<processes.length;i++) {
    		if (processes[i] == null){
    	    	processes[i] = process;
    	    	Thread t = new Thread(process);
    	    	threads[i] = t;
    	    	t.start();
    	        System.out.format("Started process, %s, in thread: %s%n",process.name,t.getName());
    	        break;
    		}
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
		WriteData w2Obj = new WriteData();
		ReadData r2Obj = new ReadData();		
		jt.startProcess(wObj);
		jt.startProcess(r2Obj);		
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while (run){
			if ((System.currentTimeMillis() - startTime) > 9900){
				System.out.println("Stop all.");
				jt.stopAll();
				break;
			}
		}
		System.out.println("test");
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
		String name = this.getName();
		if (this.getInstance() == null) {
			this.setInstance(this);
			this.name = name;
	    	System.out.format("Initialized IOProcess: %s.",this.name);
		} else {
			System.out.format("Cannot initialize IOProcess: %s.", name);
		}
    }
	
	abstract protected String getName();
	abstract protected void setInstance(IOProcess process);
	abstract protected IOProcess getInstance();
	
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
	private static WriteData INSTANCE = null;
	
	protected void setInstance(IOProcess process){WriteData.INSTANCE = (WriteData) process;}
	protected WriteData getInstance() {return (WriteData) INSTANCE;}
	public String getName() {return WriteData.name;}
	
	public void execute() {System.out.println("write");}
}

class ReadData
extends IOProcess {
	public final static String name = "ReadData";
	private static ReadData INSTANCE = null;
	
	protected void setInstance(IOProcess process){ReadData.INSTANCE = (ReadData) process;}
	protected ReadData getInstance() {return (ReadData) INSTANCE;}
	public String getName() {return ReadData.name;}
	
	public void execute() {System.out.println("read");}
}