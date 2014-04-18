package server;

import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cusServices.CusService;


public class CustomerServer extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int defaultPort = 9732;

	private JScrollPane scroll;
	private JTextArea textConsole;
	
	public CustomerServer(){
		this.setTitle("Customer server");
		
		textConsole = new JTextArea();
		textConsole.setEditable(false);
		textConsole.setFont(new Font("Serif", Font.PLAIN, 16));
		scroll = new JScrollPane(textConsole);
		
		this.add(scroll);
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void appendContent(String text){
		this.textConsole.append(text+'\n');
	}
	
	
	public static void main(String args[]) {
		Socket socket = null;
		ServerSocket serverSocket = null;
		CusQueryThread clientThread;

		CusService cusService = new CusService();
		CustomerServer cServer = new CustomerServer();

		try {
			//System.out.println("Server is starting ...");
			cServer.appendContent("Server is starting ...");
			serverSocket = new ServerSocket(defaultPort);

		} catch (Exception ex) {
			//System.out.println("Unable to start server");
			cServer.appendContent("Unable to start server");
			System.exit(1);
		}
		try {
			
			cServer.appendContent("Server started - Waiting for clients on port:"
					+ defaultPort +"\n");
			while (true) {
				socket = serverSocket.accept();
				clientThread = new CusQueryThread(socket, cusService, cServer);
				clientThread.start();
			}
		} catch (IOException ex) {
			cServer.appendContent("Unable to open I/O stream to client");
			System.exit(1);
		}

	}

}
