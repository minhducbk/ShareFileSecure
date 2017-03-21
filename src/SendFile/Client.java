package SendFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import UI.MainWindow;

public class Client {
	private Integer local_port;
	private Socket sock_message = null;
	private Socket sock_file = null;
	private BufferedReader input;
	private String FileName;
	private Boolean isServer;
	private Server server;
	private Thread thread_server;
	private MainWindow window;
	 
	public Client(MainWindow window, Integer local_port){
		this.window = window;
		this.local_port = local_port;
		this.isServer = true;
		server = new Server(window, local_port);
		thread_server = new Thread(server);
		thread_server.start();
	}
	
	public Socket get_socket_file(){
		if (isServer)
			return server.get_socket_file();
		else
			return sock_file;
	}
	
	public Socket get_socket_message(){
		if (isServer)
			return server.get_socket_message();
		else
			return sock_message;
	}
	
	public void request_connect(Integer destination_port, String destination_ip){
	      try
	      {
	         System.out.println("Connecting to " + destination_ip + " on port " + destination_port);
	         sock_file = new Socket(destination_ip, destination_port);
	         sock_message = new Socket(destination_ip, destination_port);
	         window.set_status("Just connected to " + sock_file.getRemoteSocketAddress());
	         OutputStream outToServer = sock_message.getOutputStream();
	         DataOutputStream out = new DataOutputStream(outToServer);
	         InputStream inFromServer = sock_message.getInputStream();
	         DataInputStream in = new DataInputStream(inFromServer);
	         
	         out.writeUTF("Hello from "+ sock_message.getLocalSocketAddress());
	         String message = in.readUTF();
	         window.set_message(message);
	         System.out.println("Server says " + message);
	         
	         isServer = false;
	         Thread receive_message = new Thread(new ReceiveMessage(window, sock_message, true));
	         receive_message.start();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	}
	
	public void init_connection()
	{
		
	}	
}
