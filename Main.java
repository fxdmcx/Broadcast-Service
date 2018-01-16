import java.util.Scanner;
import javax.naming.InitialContext;

public class Main
{
	private int initiator = 1;
	
	public int getInitiator() {
		return initiator;
	}
	
	public void setInitiator(int initiator) {
		this.initiator = initiator;
	}
	
	
	public static void main(String[] args) {
		Parser.checkNodes();
		
		// Wait till the SCTP listener is up and running.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Process the nodes, number them and if I am node 1, build spanning
		// tree by sending application messages.
		SCTPClient.nodeProcess();
		System.out.println("Calling method nodeProcess() completed");
		try 
		{
			Thread.sleep(8000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		Parser.printNeighbours();
		Parser.printChildren();
		
		if(SCTPClient.getnodenumber() == new Main().getInitiator())
		{	
				System.out.println("\n\nSpanning tree built\n");
				System.out.println("Initiating broadcast...\n");
		}
		
		try 
		{
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		//If I am initiator start broadcasting.
		new Main().doBroadcast();
	}
	
	public void doBroadcast()
	{
		if(SCTPClient.getnodenumber() == new Main().getInitiator())
		{	
			System.out.println("Broadcast initiated\n");
			Parser.sendBroadcast();			
		}
	}
}