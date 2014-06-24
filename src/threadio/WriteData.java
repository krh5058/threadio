package threadio;
import java.io.*;
import java.nio.charset.*;

import java.util.ArrayList;

public class WriteData
extends IOProcess {
	private final static String name = "WriteData";
	private int pace = 60000;
	private ArrayList<Double> buffer = new ArrayList<Double>();
	
	protected String getName() {return WriteData.name;}
	protected int getInterval() {return this.pace;}

	private final Charset ASCII = Charset.forName("US-ASCII");
//	private final Charset UTF8 = Charset.forName("UTF-8");
	private BufferedWriter writer;
	
	public WriteData(int... pace){
		super();
		if (pace.length!=0){
			this.pace = pace[0];
		}
	}
	
	public void openBufferedOutputStream(String filename) throws IOException{
		FileOutputStream file = new FileOutputStream(filename);
		OutputStreamWriter osw = new OutputStreamWriter(file, ASCII);
		this.writer = new BufferedWriter(osw);		
	}

	public void appendToBuffer(double value){
		this.buffer.add(value);
	}
	public void execute() {
//		System.out.println("write"); // Meets interface requirement #2
		if (this.writer != null) {
			this.writeFromBuffer();
			this.clearBuffer();
		} else {
			System.out.println("WriteData: No output stream defined.");
		}
	}
	public void writeFromBuffer(){
		if (this.buffer.size()>0){
			for (double value : this.buffer){
				System.out.println("Writing (" + Thread.currentThread().getName() + "):" + value);
				try {
					this.writer.write(String.valueOf(value));
					this.writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void clearBuffer(){
		System.out.println("Clearing buffer (" + Thread.currentThread().getName() + ").");
		this.buffer.clear();
	}
	public void cleanup(){
		this.execute();
		if (this.writer != null) {
			try {
				this.writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}