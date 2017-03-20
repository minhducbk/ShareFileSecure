package SendFile;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Crypto.RC2;
import UI.MainWindow;


public class ReceiveMessage  extends Thread{

	private Socket sock;
	private DataOutputStream  output;

	private volatile boolean isRunning = true;
	public final static int FILE_SIZE = 6022384; // file size temporary hard coded
    public File myFile;
    private MainWindow window;
	
	public ReceiveMessage(MainWindow window, Socket sock, boolean Running){
		this.window = window;
		this.sock = sock;
		this.isRunning = Running;
	}
	
	 public void kill() {
	       isRunning = false;
	 }
	
	public void run() {
		while (isRunning) {
			
			try {
				DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
				DataInputStream inFromClient = new DataInputStream(sock.getInputStream());
				String message = inFromClient.readUTF();
				outToClient.writeUTF(message);
				window.set_message(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				this.kill();
			}	
		}
	}
}
	
