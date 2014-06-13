//import java.io.DataOutputStream;
//import java.io.FileOutputStream;

import java.util.ArrayList;
public class ProcessThreadManager
{
	private ArrayList<IOProcess> processes = new ArrayList<IOProcess>();
	private ArrayList<Thread> threads = new ArrayList<Thread>();
    public ProcessThreadManager()
    {
    	System.out.println("Initialized ProcessThreadManager.");
    }
    
//    public void startThread(){
//    	 t.start();
//    }
    
    public void attachProcessAndStartThread(IOProcess process){
    	this.processes.add(process);
    	this.startThread(process);
    }
    
    private void startThread(IOProcess process) {
    	Thread t = new Thread(process);
    	this.threads.add(t);
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
//		new MyJavaThread().start();
		ProcessThreadManager jt = new ProcessThreadManager();
		jt.attachProcessAndStartThread(new WriteData());
//		jt.appendFilename("test");
//		jt.appendFilename("test2");
//		System.out.println(jt.getIndex("test"));
//		System.out.println(jt.getIndex("test2"));
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while (run){
			if ((System.currentTimeMillis() - startTime) > 10000){
				System.out.println("Stop all.");
				jt.stopAll();
				break;
			}
		}
	}
}


interface IOProcess extends Runnable {
	void terminate();
}

class WriteData
implements IOProcess {
    private ArrayList<String> filenames = new ArrayList<String>();
	private volatile boolean running = true;
	
    public WriteData()
    {
    	System.out.println("Initialized WriteData.");
    }
    
	public void terminate() {
		running = false;
	}
	
    public void appendFilename(String filename)
    {
    	if (this.filenames.contains(filename)) {
    		System.out.format("Filename, %s, already appened to index %d.%n",
    				filename,
    				this.getIndex(filename));
    	} else {
    		this.filenames.add(filename);
    	}
    }
    public int getIndex(String filename)
    {
    	return this.filenames.indexOf(filename);
    }

	@Override
	public void run()
	{
		while (running){
			try
			{
				System.out.println("test");
				Thread.sleep(2000);
				//    	            DataOutputStream out = new DataOutputStream(
				//    	                                     new FileOutputStream(filename));
				//    	            for (int i=0; i < doubleData.length; i++)
				//    	            {
				//    	                out.writeDouble(doubleData[i]);
				//    	            }
				//    	            out.close();
			} catch (InterruptedException e) {
                running = false;
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
		System.out.println("finished");
	}
}