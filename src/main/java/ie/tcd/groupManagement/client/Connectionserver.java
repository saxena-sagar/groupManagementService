package ie.tcd.groupManagement.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connectionserver implements Runnable {

	Socket sock;
	private Scanner INPUT;
	private PrintWriter OUT;
	String message = "";

	public Connectionserver(Socket X) {
		this.sock = X;
	}

	public void CheckConnection() throws IOException
	{
		
		if(!sock.isConnected()) {
			
			for(int i = 1;i<=Server.connectionList.size();i++) {
				
				if(Server.connectionList.get(i) == sock){
					
					Server.connectionList.remove(i);
				}
				
			}
			
			for(int i = 1;i<=Server.connectionList.size();i++) {
				
				Socket temp_sock = (Socket) Server.connectionList.get(i-1);
				PrintWriter temp_out = new PrintWriter(temp_sock.getOutputStream());
				temp_out.println(temp_sock.getLocalAddress().getHostName() + " disconnected");
				temp_out.flush();
				//Show  disconnection at server
				System.out.println(temp_sock.getLocalAddress().getHostName() + " disconnected");
			}
			
			
		}
		
	}
	
	public void run() {
		
		try
		{	try
				{
					INPUT = new Scanner(sock.getInputStream());
					OUT = new PrintWriter(sock.getOutputStream());
					while(true) {
						
						CheckConnection();
						
						if(!INPUT.hasNext())
						{return;}
						message = INPUT.nextLine();
						System.out.println("Client said:" + message);
						
						for(int i = 1;i<=Server.connectionList.size();i++) {
							Socket temp_sock = (Socket) Server.connectionList.get(i-1);
							PrintWriter temp_out = new PrintWriter(temp_sock.getOutputStream());
							temp_out.println(message);
							temp_out.flush();
							//Show  disconnection at server
							System.out.println("Sent to: " + temp_sock.getLocalAddress().getHostName());
						}
				}
		}
		finally {
			sock.close();
		}
	}catch(Exception X){
		System.out.println(X);
	}
  }

}