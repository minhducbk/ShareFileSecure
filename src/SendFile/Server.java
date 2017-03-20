package SendFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import UI.MainWindow;

public class Server extends Thread{
	private ServerSocket serverSocket;
	private Socket sock_file = null;
	private Socket sock_message = null;
	private Integer local_port;
	private MainWindow window;
	public Server(MainWindow window, Integer local_port){
		this.local_port = local_port;
		this.window = window;
		try {
			serverSocket = new ServerSocket(local_port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		listen();
	}
	
	public Socket get_socket_file(){
		return sock_file;
	}
	
	public Socket get_socket_message(){
		return sock_message;
	}
	
	private void listen(){
	     try
	     {
	        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			window.set_status("Inital successful");
	        sock_file = serverSocket.accept();
	        sock_message = serverSocket.accept();
			window.set_status("Just connected from " + sock_file.getRemoteSocketAddress());
			DataOutputStream outToClient = new DataOutputStream(sock_message.getOutputStream());			
			DataInputStream inFromClient = new DataInputStream(sock_message.getInputStream());
            outToClient.writeUTF("Thank you for connecting to " + sock_message.getLocalSocketAddress());
            String message = inFromClient.readUTF();
            System.out.println(message);
            window.set_message(message);
			
			Thread receive_message = new Thread(new ReceiveMessage(window, sock_message, true));
	        receive_message.start();
	     }catch(SocketTimeoutException s)
		 {
			 System.out.println("Socket timed out!");
	     }catch(IOException e)
	     {
	        e.printStackTrace();
	     }
	}
}
