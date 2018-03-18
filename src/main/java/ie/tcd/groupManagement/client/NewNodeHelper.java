package ie.tcd.groupManagement.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NewNodeHelper implements Runnable{
	
	Socket sock;
	Scanner input;
	Scanner send = new Scanner(System.in);
	PrintWriter out;
	
	public NewNodeHelper(Socket X) {
		this.sock = X;
	}
	
	public void run() {
		
		try {
			try {
				input = new Scanner(sock.getInputStream());
				out = new PrintWriter(sock.getOutputStream());
				out.flush();
				//CheckStream();
				
			}finally {
				sock.close();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void Leave() throws IOException{
		sock.close();
		System.exit(0);
	}
	
//	public void CheckStream() {
//		while(true) {
//			Receive();
//		}
//	}
//	
//	public void Receive() {
//		
//		
//	}
}
