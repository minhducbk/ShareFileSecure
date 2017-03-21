package SendFile;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Crypto.RC2;
import UI.MainWindow;


public class SendFile  extends Thread{

	private Socket sock;
	private Socket sock_m;
	private DataOutputStream  output;

	private volatile boolean isRunning = true;
	public final static int FILE_SIZE = 6022384; // file size temporary hard coded
    public File myFile;
    private MainWindow window;
    
	public SendFile(File file, Socket sock, Socket sock_m, boolean Running){
		this.sock = sock;
		this.sock_m = sock_m;
		this.isRunning = Running;
		this.myFile = file;
	}
	
	public SendFile(MainWindow window, File file, Socket sock, Socket sock_m, boolean Running){
		this.window = window;
		this.sock = sock;
		this.sock_m = sock_m;
		this.isRunning = Running;
		this.myFile = file;
	}
	
	 public void kill() {
	       isRunning = false;
	 }
	
	public void run() {
		while (isRunning) {
			System.out.println("I'm in running\n");
	 		try {
				sendFile(myFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendFile(File myFile) throws IOException{
		BufferedInputStream bis = null;
		OutputStream os = null;
		try { 
			 System.out.println("I'm in SendFile in SendReceiveFile\n");
			 
			 byte [] mybytearray  = new byte [(int)myFile.length()];
			 FileInputStream fis = new FileInputStream(myFile);
	         bis = new BufferedInputStream(fis);
	         os = sock.getOutputStream();
	         bis.read(mybytearray,0,mybytearray.length);
	         
	         
	         ////////////////////////////// Encrypt
    		 mybytearray = RC2.encrypt(mybytearray, "this is key");
			 ///////////////////////////////////////
    		 
    		 
    		 DataOutputStream outToClient = new DataOutputStream(sock_m.getOutputStream());
 			 String message = sock.getLocalSocketAddress() + " sent file " + myFile.getName() +  " (" + mybytearray.length + " bytes)";
    		 outToClient.writeUTF(message.substring(1, message.length()));
    		 
	         
 			 System.out.println(sock+":Sending " + myFile.getPath() + "(" + mybytearray.length + " bytes)");
	         os.write(mybytearray,0,mybytearray.length);
	         os.flush();
	         System.out.println("Done.");
         } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 finally{
//			 if (os != null)
//				 os.close();
//			 if (bis != null)
//				 bis.close();
//			 sock.close();
			 this.kill();
		 }
	}
}
	
