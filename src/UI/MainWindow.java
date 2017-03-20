package UI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import SendFile.Client;
import SendFile.ReceiveFile;
import SendFile.SendFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextField txtDestPort;
	private JTextField txtDestIP;
	private JTextField txtLocalPort;
	private JTextField txtAttachFile;
	private JLabel txtStatus;
	private JTextArea txtMessage;
	private Client MyConnection = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void set_status(String status){
		txtStatus.setText(status);
	}
	
	public void set_message(String status){
		txtMessage.setText(status);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		MainWindow window = this;
		frame = new JFrame();
		frame.setBounds(100, 100, 465, 343);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Destination IP");
		lblNewLabel.setBounds(30, 73, 120, 14);
		frame.getContentPane().add(lblNewLabel);
		
		txtDestPort = new JTextField();
		txtDestPort.setBounds(146, 103, 135, 20);
		frame.getContentPane().add(txtDestPort);
		txtDestPort.setColumns(10);
		
		JButton btnInitConnect = new JButton("Initiate connection");
		
		btnInitConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyConnection = new Client(window, Integer.parseInt(txtLocalPort.getText()));
			}
		});
		btnInitConnect.setBounds(291, 11, 148, 23);
		frame.getContentPane().add(btnInitConnect);
		
		JLabel lblDestinationPort = new JLabel("Destination Port");
		lblDestinationPort.setBounds(30, 106, 120, 14);
		frame.getContentPane().add(lblDestinationPort);
		
		txtDestIP = new JTextField();
		txtDestIP.setBounds(146, 70, 135, 20);
		frame.getContentPane().add(txtDestIP);
		txtDestIP.setColumns(10);
		
		txtMessage = new JTextArea();
		txtMessage.setBounds(73, 173, 310, 44);
		frame.getContentPane().add(txtMessage);
		
		JLabel lblLocalPort = new JLabel("Local Port");
		lblLocalPort.setBounds(30, 15, 80, 14);
		frame.getContentPane().add(lblLocalPort);
		
		txtLocalPort = new JTextField();
		txtLocalPort.setBounds(146, 12, 135, 20);
		frame.getContentPane().add(txtLocalPort);
		txtLocalPort.setColumns(10);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(30, 43, 46, 14);
		frame.getContentPane().add(lblStatus);
		
		txtStatus = new JLabel("Ready!");
		txtStatus.setBounds(183, 45, 256, 14);
		frame.getContentPane().add(txtStatus);
		
		JButton btnSend = new JButton("Send File");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File myFile = new File(txtAttachFile.getText());
				Thread t = new Thread(new SendFile(window, myFile, MyConnection.get_socket_file(), MyConnection.get_socket_message(), true));
				t.start();
			}
		});
		btnSend.setBounds(335, 227, 104, 23);
		frame.getContentPane().add(btnSend);
		
		txtAttachFile = new JTextField();
		txtAttachFile.setBounds(10, 228, 191, 20);
		frame.getContentPane().add(txtAttachFile);
		txtAttachFile.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				File f;
				chooser.setDialogTitle("Choose a file");
				chooser.setVisible(true);
				try {
					f = new File(new File("").getCanonicalPath());
					chooser.setSelectedFile(f);
					chooser.showOpenDialog(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
			    File curFile = chooser.getSelectedFile();
			    System.out.println("Cur File: " + curFile.getName() + "\n");
			    if (curFile.isFile() && curFile.exists() && !curFile.isDirectory())
			    	txtAttachFile.setText(curFile.getAbsolutePath());
			}
		});
		btnBrowse.setBounds(229, 227, 89, 23);
		frame.getContentPane().add(btnBrowse);
		
		JButton btnSendRequest = new JButton("Send Request");
		btnSendRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer destination_port = Integer.parseInt(txtDestPort.getText());
				String destination_ip = txtDestIP.getText();
				MyConnection.request_connect(destination_port, destination_ip);
			}
		});
		btnSendRequest.setBounds(135, 134, 158, 23);
		frame.getContentPane().add(btnSendRequest);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new ReceiveFile(window, MyConnection.get_socket_file(), MyConnection.get_socket_message(), true));
				t.start();
			}
		});
		btnDownload.setBounds(335, 261, 104, 23);
		frame.getContentPane().add(btnDownload);
	}
}
