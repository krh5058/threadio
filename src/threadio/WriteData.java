package threadio;
import java.io.*;

import java.util.ArrayList;

public class WriteData // Skeleton I/O process.  For example, writing data from some buffer (and possibly flush)
extends IOProcess {
	private final static String name = "WriteData"; // A name all processes of this type will be called
	private static int pace = 4000; // Assume you do not need to write data that often
	private ArrayList<Double> buffer = new ArrayList<Double>();
	
	protected String getName() {return WriteData.name;}
	protected int getInterval() {return WriteData.pace;}
	
//	private String outFile;
	
	public void execute() {
//		System.out.println("write"); // Meets interface requirement #2
		this.writeFromBuffer();
		this.clearBuffer();
	}
	public void appendToBuffer(double value){
		this.buffer.add(value);
	}
	public void writeFromBuffer(){
		if (this.buffer.size()>0){
			for (double value : this.buffer){
				System.out.println(value);
			}
		}
	}
	public void clearBuffer(){
		this.buffer.clear();
	}
}
//
//DataOutputStream out = new DataOutputStream(
//		new FileOutputStream(filename));
//for (int i=0; i < doubleData.length; i++)
//{
//	out.writeDouble(doubleData[i]);
//}
//out.close();