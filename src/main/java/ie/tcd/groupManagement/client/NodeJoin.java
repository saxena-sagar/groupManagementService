package ie.tcd.groupManagement.client;

import java.io.PrintWriter;
import java.net.Socket;

public class NodeJoin{
	public static NewNodeHelper newnodehelper;
	public static void main(String[] args) {
		join();
		
	}
	public static void join() {
		try {
			
				final int port = 4567;
				final String host = "Sagar";
				Socket sock = new Socket(host,port);
				System.out.println("Connected to :" + host);
				
				newnodehelper = new NewNodeHelper(sock);
				
				Thread X = new Thread(newnodehelper);
				X.start();
		}catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
	}
}
