import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

public class SCTPClient {
	private static SCTPClient sync = new SCTPClient();
	
	private static int process_node;
	private static String host;
	
	public static void nodeProcess()
	{
		try {
			host = InetAddress.getLocalHost().getHostName();
			int itr = 0;
			for (String h : Parser.hostname) 
			{
				itr++;				
				if (h.equalsIgnoreCase(host)) 
				{
					process_node = itr;
					System.out.println("Process node: " + process_node +
							", listening on port: " + Parser.getPort(process_node));
				}			
			}
		} catch (UnknownHostException e) {
			System.out.println("Failed : Host name");
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		
		if (process_node == new Main().getInitiator())
		{
			System.out.println("I am the initiator and starting broadcast");
			Parser.generateSpanningTree(process_node, process_node);	
		}
	}


	public static void doWait()
	{
		synchronized (sync) {
			try {
				sync.wait();
			} catch (InterruptedException e) {
				e.getMessage();
			}
		}
	}

	public static void doNotify()
	{
		synchronized (sync) {
			sync.notify();
		}
	}
	
	
	// Send broadcast message to the selected node.
	public static void Sctpsend_broadcastMsg(int node, int port) 
	{
		Socket srv_socket;
		try {
			srv_socket = new Socket(Parser.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(srv_socket.getOutputStream(), true);
			msg_send.println("BROADCAST_MSG from node: " + process_node + " to node: " + node);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Send broadcast acknowledge to the selected node.
	public static void Sctpsend_broadcastAck(int node, int port) 
	{
		Socket srv_socket;
		try {
			srv_socket = new Socket(Parser.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(srv_socket.getOutputStream(), true);
			msg_send.println("BROADCAST_ACK_MSG from node: " + process_node + " to node: " + node);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// Send application message to the selected node.
	public static void Sctpsend_applicationMsg(int node, int port, int root) 
	{		
		Socket srv_socket;
		try {
			srv_socket = new Socket(Parser.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(srv_socket.getOutputStream(), true);
			msg_send.println("APP_MSG from node, root-node: " + process_node +
							 ", " + root + " to node: " + node);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	// Send application acknowledge to the selected node.
	public static void Sctpsend_applicationAck(int node, int port) 
	{
		Socket srv_socket;
		try {
			srv_socket = new Socket(Parser.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(srv_socket.getOutputStream(), true);			
			msg_send.println("APP_ACK from node: " + process_node + " to node: " + node);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int getnodenumber()
	{	
		return process_node;
	}
	public static String getHostName()
	{
		return host;
	}
}