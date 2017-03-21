package SendFile;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Crypto.RC2;
import UI.MainWindow;


public class ReceiveFile implements Runnable{

	private Socket sock;
	private Socket sock_m;
	private BufferedReader input;
	private DataOutputStream  output;
	private MainWindow window;

	private volatile boolean isRunning = true;
	public final static int FILE_SIZE = 6022384; // file size temporary hard coded
      
	public ReceiveFile(Socket sock, Socket sock_m, boolean Running){
		this.sock = sock;
		this.sock_m = sock_m;
		this.isRunning = Running;
	}
	
	public ReceiveFile(MainWindow window, Socket sock, Socket sock_m, boolean Running){
		this.window = window;
		this.sock = sock;
		this.sock_m = sock_m;
		this.isRunning = Running;
	}
	
	@Override
	public void run() {
		while (isRunning) {
			System.out.println("I'm in running\n");
	 		try {
				check();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	 public void kill() {
	       isRunning = false;
	 }

	
	public void check() throws IOException {
		while(isRunning) { 
			receiveFile();
		}
		
	}

	public void receiveFile() throws IOException {
		
	    int bytesRead;
	    int current = 0;
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
	    try {
	    	JFileChooser fs = new JFileChooser(); 
	    	JFrame parentFrame = new JFrame();
	    	int userSelection = fs.showSaveDialog(parentFrame);
	    	 
	    	if (userSelection == JFileChooser.APPROVE_OPTION) {
	    	    File fileToSave = fs.getSelectedFile();
	    	    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
	    	}
	    	fs.setDialogTitle("Save a File");
			
	        // receive file
	        fos = new FileOutputStream(fs.getSelectedFile().getAbsolutePath());
	        bos = new BufferedOutputStream(fos);
	        InputStream is = sock.getInputStream();
	        InputStream is_m = sock_m.getInputStream();
	        DataInputStream inFromClient = new DataInputStream(sock_m.getInputStream());
			String message = inFromClient.readUTF();
			System.out.println("message:" + message);
			Integer size_file = Integer.parseInt(message.split("\\(")[1].split(" ")[0]);
			System.out.println("file size" + size_file);
			
			
	        byte [] mybytearray  = new byte [size_file];

	        while ((bytesRead = is.read(mybytearray, current, mybytearray.length - current)) > 0) {
	        	   if(bytesRead > 0) 
	        		   current += bytesRead;
	        }
	        
		    /////////////// Decrypt
        	try {
				mybytearray = RC2.decrypt(mybytearray, "this is key");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	////////////////

	        bos.write(mybytearray, 0 , mybytearray.length);
	        bos.flush();
	        System.out.println("Saved.");

	      }
	      finally {
//	        if (fos != null) fos.close();
//	        if (bos != null) bos.close();
//		    sock.close();
	        this.kill();
	      }
	}
}
	