//import java.io.DataOutputStream;
//import java.io.FileOutputStream;

import java.util.ArrayList;
public class ProcessThreadHandler
{
	private static final int allowableProcesses = 2;
	private static IOProcess[] processes = new IOProcess[allowableProcesses];
//	private static final Thread[] threads = new Thread[allowableProcesses];
    public ProcessThreadHandler()
    {
    	System.out.println("Initialized ProcessThreadHandler.");
    }
    
//    public void startThread(){
//    	 t.start();
//    }
    
//    public void attachProcessAndStartThread(IOProcess process){
//    	this.processes.add(process);
//    	this.startThread(process);
//    }
    
//    private void startThread(IOProcess process) {
//    	Thread t = new Thread(process);
//    	this.threads.add(t);
//        t.start();
//        System.out.format("Started thread: %s%n",
//    			this.threadMessage(t));
//    }
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
//		new MyJavaThread().start();
//		ProcessThreadHandler jt = new ProcessThreadHandler();
		System.out.println("Trying Write 1.");
		new WriteData();
		System.out.println("Trying Read 1.");
		new ReadInput();
		System.out.println("Trying Write 2.");
		new WriteData();
		System.out.println("Trying Read 2.");
		new ReadInput();
//		jt.attachProcessAndStartThread(WriteData.getInstance());
////		jt.appendFilename("test");
////		jt.appendFilename("test2");
////		System.out.println(jt.getIndex("test"));
////		System.out.println(jt.getIndex("test2"));
//		boolean run = true;
//		long startTime = System.currentTimeMillis();
//		while (run){
//			if ((System.currentTimeMillis() - startTime) > 10000){
//				System.out.println("Stop all.");
//				jt.stopAll();
//				break;
//			}
//		}
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
    
//	private static final int allowableFiles = 1;
//	private static String[] filenames = new String[allowableFiles];
	
//    public void specifyFilename(String filename)
//    {
//    	if (this.filenames.contains(filename)) {
//    		System.out.format("Filename, %s, already appened to index %d.%n",
//    				filename,
//    				this.getIndex(filename));
//    	} else {
//    		this.filenames.add(filename);
//    	}
//    }
//    public int getIndex(String filename)
//    {
//    	return this.filenames.indexOf(filename);
//    }

	public void execute() {
		System.out.println("write");
	}


				//    	            DataOutputStream out = new DataOutputStream(
				//    	                                     new FileOutputStream(filename));
				//    	            for (int i=0; i < doubleData.length; i++)
				//    	            {
				//    	                out.writeDouble(doubleData[i]);
				//    	            }
				//    	            out.close();
				
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