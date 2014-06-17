package threadio;
import java.io.*;
import java.nio.charset.*;

import java.util.ArrayList;

public class WriteData
extends IOProcess {
	private final static String name = "WriteData";
	private static int pace = 60000;
	private ArrayList<Double> buffer = new ArrayList<Double>();
	
	protected String getName() {return WriteData.name;}
	protected int getInterval() {return WriteData.pace;}

	private final Charset UTF8 = Charset.forName("UTF-8");
	private BufferedWriter writer;
	
	public void openBufferedOutputStream(String filename) throws IOException{
		FileOutputStream file = new FileOutputStream(filename);
		OutputStreamWriter osw = new OutputStreamWriter(file, UTF8);
		this.writer = new BufferedWriter(osw);		
	}
	
	public void execute() {
//		System.out.println("write"); // Meets interface requirement #2
		if (this.writer != null) {
			this.writeFromBuffer();
			this.clearBuffer();
		}
	}
	public void appendToBuffer(double value){
		this.buffer.add(value);
	}
	public void writeFromBuffer(){
		if (this.buffer.size()>0){
			for (double value : this.buffer){
				System.out.println(value);
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
		this.buffer.clear();
	}
	@Override
	public void cleanup(){
		this.execute();
		try {
			this.writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}