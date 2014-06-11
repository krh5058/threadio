//import java.io.DataOutputStream;
//import java.io.FileOutputStream;

import java.util.ArrayList;
public class ProcessThreadHandler
{
	private ArrayList<IOProcess> processes = new ArrayList<IOProcess>();
    public ProcessThreadHandler()
    {
    	System.out.println("Initialized ProcessThreadHandler.");
    }
    
//    public void startThread(){
//    	 t.start();
//    }
    
    public void attachProcess(){
    	WriteData
    	
    }
    
    public void startThread() {
    	process = new Process1(); 
    	t = new Thread(process);
        t.start();
        System.out.format("Started thread: %s%n",
    			this.threadMessage());
    }
    public void stopThread() {
    	if (t != null){
    		process.terminate();
    		System.out.println("terminate");
    	}
    }
    private String threadMessage() {
        String threadName =
            Thread.currentThread().getName();
        return threadName;
    }

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
////		new MyJavaThread().start();
//		MyJavaThread jt = new MyJavaThread();
//		jt.appendFilename("test");
//		jt.appendFilename("test2");
//		System.out.println(jt.getIndex("test"));
//		System.out.println(jt.getIndex("test2"));
//	}
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