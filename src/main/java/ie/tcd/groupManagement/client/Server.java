package ie.tcd.groupManagement.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.ServerSocket;

public class Server {
	public static ArrayList<Socket> connectionList = new ArrayList<Socket>();
	public static ArrayList<String> nodeList = new ArrayList<String>();
	
	public static void main(String[] args)throws IOException {
		try {
			final int port = 4567;
			ServerSocket server = new ServerSocket(port);
			System.out.println("Waiting for node on port " + server.getLocalPort() + "...");
			
			while(true) {
				Socket new_connection_socket = server.accept();
				connectionList.add(new_connection_socket);
				
				System.out.println("Node connected with address:" + new_connection_socket.getLocalAddress().getHostName());
				
				UpdateGroupList(new_connection_socket);
				
				Connectionserver conn_serv = new Connectionserver(new_connection_socket);
				Thread X = new Thread(conn_serv);
				X.start();
				
				
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void UpdateGroupList(Socket new_node) throws IOException{
		
		Scanner Input = new Scanner(new_node.getInputStream());
		String node_name = Input.nextLine();
		nodeList.add(node_name);
		
		for(int i = 1;i<= Server.connectionList.size();i++) {
			Socket temp_sock = (Socket) Server.connectionList.get(i-1);
			PrintWriter OUT = new PrintWriter(temp_sock.getOutputStream());
			OUT.println("_$#!" + nodeList);
			OUT.flush();
		}
	}
	
}
