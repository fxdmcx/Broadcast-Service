import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class Parser
{
	static BufferedReader br = null;
	static ArrayList<String> hosts = new ArrayList<String>();
	static ArrayList<String> hostname = new ArrayList<String>();
	static ArrayList<Integer> ports = new ArrayList<Integer>();
	static ArrayList<String> topology = new ArrayList<String>();
	static ArrayList<Integer> children = new ArrayList<Integer>();
	static ArrayList<String> neighbours = new ArrayList<String>();
	static int noOfBroadcast = 0;
	
	/* Parse the configure file, collect topology and ports info */
	public static void checkNodes()
	{
		try {		 
			String sCurrentLine; 				
			br = new BufferedReader(new FileReader("conf.txt")); 
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if(sCurrentLine.startsWith("#"))
					continue;
				if(sCurrentLine.startsWith("Broadcast"))
				{
					String[] part = sCurrentLine.split(":");
					noOfBroadcast = Integer.parseInt(part[1].replaceAll("\\s",""));
					continue;
				}
				String[] parts = sCurrentLine.split("\t");
				String oneNode = parts[0];	
				int port = Integer.parseInt(parts[1]);
				String topo = parts[2];
				//System.out.println(oneNode);
				//System.out.println(ports);
				hostname.add(oneNode+".utdallas.edu");	
				topology.add(topo);
				ports.add(port);
			}			
			for(int i=0;i<hostname.size();i++)				
			{
				//System.out.println(hostname.get(i)+" : "+ports.get(i));			
				String[] parts = topology.get(i).split(" ");
				for(int j=0;j<parts.length;j++)
				{
					int index= Integer.parseInt(parts[j]);
					//System.out.println(hostname.get(index-1)+", listening on: "+ports.get(index-1));
				}
				//System.out.println();
			}

			//System.out.println(SCTPClient.getnodenumber());
			for(int i=0;i<hostname.size();i++)
			{				
				if(hostname.get(i).trim().equals(InetAddress.getLocalHost().getHostName()))
				{
					hosts.add(hostname.get(i));					
					SCTPServer.listen_port = ports.get(i);
					// Start the listener port to accept any incoming connections.					
					//System.out.println("Starting SCTP listener thread");
					Thread listener = new Thread(new SCTPServer());
					listener.start();
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public static void generateSpanningTree(int node, int root)
	{
		String[] parts = topology.get(node-1).split(" ");
		for (int j = 0; j < parts.length; j++)
		{
			int index= Integer.parseInt(parts[j]);
			//System.out.println(index+", "+ports.get(index-1)+", "+root);
			SCTPClient.Sctpsend_applicationMsg(index, ports.get(index-1), root);
			//System.out.println(hostname.get(index-1)+", listening on: "+ports.get(index-1));
		}
	}
	
	public static void sendBroadcast()
	{
		for(int childNode: children)
		{
			SCTPClient.Sctpsend_broadcastMsg(childNode, getPort(childNode));
		}
	}
	
	
	public static void printNeighbours()
	{		
		System.out.println("Printing neighbours");
		System.out.println("Number of neighbours: " + neighbours.size());
		for (String neighbour: neighbours)
		{
			System.out.println(neighbour + ", ");
		}
	}
	
	public static void printChildren()
	{
		System.out.println("Printing children");
		System.out.println("Number of children: "+children.size());
		for (int child: children)
		{
			System.out.println(child + ", ");
		}		
	}
	
	
	public static int getPort(int node)
	{	
		return ports.get(node-1);
	}
}