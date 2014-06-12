//import java.io.DataOutputStream;
//import java.io.FileOutputStream;

//import java.util.ArrayList;
public class ProcessThreadHandler
{
	private static final int allowableProcesses = 2;
	private static IOProcess[] processes = new IOProcess[allowableProcesses];
	private static Thread[] threads = new Thread[allowableProcesses];
    public ProcessThreadHandler()
    {
    	System.out.println("Initialized ProcessThreadHandler.");
    }
    
    public void startProcess(IOProcess process){
    	ProcessThreadHandler.processes[1] = process;
    	Thread t = new Thread(process);
    	ProcessThreadHandler.threads[1] = t;
    	t.start();
        System.out.format("Started thread: %s%n",
    			this.threadMessage(t));
    }    
    
    private void stopAll() {
    	for (IOProcess process : processes) {
//    		if (t != null){
    		process.terminate();
    		System.out.println("terminate");
//    		}
    	}
    }
    private String threadMessage(Thread t) {
        String threadName =
            t.getName();
        return threadName;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProcessThreadHandler jt = new ProcessThreadHandler();
		System.out.println("Trying Write 1.");
		WriteData w = new WriteData();
		System.out.println("Trying Read 1.");
		ReadInput r = new ReadInput();
		System.out.println("Trying Write 2.");
		WriteData w = new WriteData();
		System.out.println("Trying Read 2.");
		ReadInput r = new ReadInput();
		jt.startProcess(r);
	}
}

interface ProcessInterface extends Runnable {
	void terminate();
	void execute();
}

abstract class IOProcess implements ProcessInterface {
	protected volatile boolean running = true;
	
	public IOProcess() {
		if (this.getInstance() == null) {
			this.setInstance(this);
	    	System.out.println("Initialized IOProcess.");
		} else {
			System.out.println("Nope");
		}
    }
	
	abstract protected void setInstance(IOProcess process);
	abstract protected IOProcess getInstance();
	
	public void terminate() {
		running = false;
	}
	
	@Override
	public void run()
	{
		while (this.running){
			try
			{
				this.execute();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				this.terminate();
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
		System.out.println("finished");
	}
}

class WriteData
extends IOProcess {
	private static WriteData INSTANCE = null;
    
	protected void setInstance(IOProcess process){
		WriteData.INSTANCE = (WriteData) process;
	}
	
	protected WriteData getInstance() {
    	return (WriteData) INSTANCE;
    }
	public void execute() {
		System.out.println("write");
	}
}

class ReadInput
extends IOProcess {
	private static ReadInput INSTANCE = null;
    
	protected void setInstance(IOProcess process){
		ReadInput.INSTANCE = (ReadInput) process;
	}
	
	protected ReadInput getInstance() {
    	return INSTANCE;
    }

	public void execute() {
		System.out.println("read");
	}
}