import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class SCTPServer extends Thread
{
	private static int pNode = -1;
	private static final boolean bool = true;
	public static int listen_port;
	String line;
	int ack_counter=0;
	private static SCTPServer sync = new SCTPServer();
	private static int parentNode = -1;
	
	public static void doWait() {
		synchronized (sync) {
			try {
				sync.wait();
			} catch (InterruptedException e) {
				e.getMessage();
			}
		}
	}

	public static void doNotify() {
		synchronized (sync) {
			sync.notify();
		}
	}
	
	
	@Override
	public void run() 
	{
		// Create a listener port and receive incoming
		// application messages from other clients.
		try (ServerSocket srvsocket = new ServerSocket(listen_port);) 
		{			
			SCTPServer.doNotify();
			while (bool) 
			{
				try 
				{
					Socket recv_socket = srvsocket.accept();					
					BufferedReader reader = new BufferedReader(new InputStreamReader(recv_socket.getInputStream()));	
					String message = reader.readLine();
					
					if(message.contains("BROADCAST_MSG"))
					{						
						System.out.println("Received "+message);
						String[] part = message.split(":");
						int sender = Integer.parseInt(part[1].replaceAll("\\s",""));
						//System.out.println(message);
						System.out.println("Forwarding broadcast message to my children");
						Parser.sendBroadcast();
						pNode = sender;
						if(Parser.children.size() == 0)
						{
							System.out.println("I am leaf node, sending broadcast ack. back to my parent node: " + sender);
							SCTPClient.Sctpsend_broadcastAck(sender, Parser.getPort(sender));
						}
					}
					else if (message.contains("BROADCAST_ACK_MSG"))
					{
						System.out.println("Received "+message);
						ack_counter++;
						System.out.println("No. of broadcast acks received: " + ack_counter);
						if (ack_counter == Parser.children.size()) {	
							System.out.println("All broadcast ack. received from children");
							ack_counter = 0;
							//System.out.println("Parent node value: " + pNode);
							if (pNode < 0) {
								System.out.println("\n######Broadcast operation completed######\n\n");								
								//pNode = -1;
								if (Parser.noOfBroadcast == 1)
									System.exit(0);
								
								if (Parser.noOfBroadcast > 1)
								{
									Parser.noOfBroadcast--;
									System.out.println(Parser.noOfBroadcast+" time(s) remaining\n");
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {		
										e.printStackTrace();
									}
									new Main().doBroadcast();									
								}
							} else {
								System.out.println("Sending broadcast ack back to my parent");
								SCTPClient.Sctpsend_broadcastAck(pNode, Parser.getPort(pNode));
							}
						}
					}
					else if (message.contains("APP_MSG"))
					{
						//System.out.println("application: "+message);
						String[] part = message.split(":");
						String[] part2 = part[1].replaceAll("\\s","").split(",");
						int senderNode = Integer.parseInt(part2[0]);
						int rootNode = Integer.parseInt(part2[1]);
						//System.out.println("parent node value: "+parentNode);
						//System.out.println(rootNode==TCPSend.getnodenumber());
						//System.out.println("overall value: "+(parentNode<0 && !(rootNode==TCPSend.getnodenumber())));
						if(parentNode < 0 && !(rootNode == SCTPClient.getnodenumber()))
						{
							System.out.println("Received "+message);
							parentNode = senderNode;
							Parser.neighbours.add(parentNode+"*");
							SCTPClient.Sctpsend_applicationAck(senderNode, Parser.getPort(senderNode));
							Parser.generateSpanningTree(SCTPClient.getnodenumber(), rootNode);
						}
					}
					else if (message.contains("APP_ACK"))
					{
						System.out.println("Received "+message);
						//System.out.println("acknowledgement: "+message);
						String[] part = message.split(":");
						int senderNode = Integer.parseInt(part[1].replaceAll("\\s",""));
						Parser.children.add(senderNode);
						Parser.neighbours.add(Integer.toString(senderNode));
					}
					reader.close();	
				}
				catch (IOException e) 
				{
					System.out.println("Exception while performing accept");
					System.out.println(e.getMessage());
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Exception while performing port ops");
			System.out.println(e.getMessage());
		}
	}
}