package ie.tcd.groupManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Hashtable;
import java.util.Scanner;

//The readthread should be open first before anything else begins
//Maintaining seperate thread for recieving becasue member of group not sure about the number of senders and everytime a msg comes to the multicast group must be 
//received by reciever.So keeping the reading thread run in infinite loop continuosly listening to the multicast port for any message
//
//class readThread extends Thread {
//
//	InetAddress group;
//	int multcastPort;
//	// Setting size for maximum message length to be multicasted across
//	int MAX_MSG_LEN = 100;
//
//	readThread(InetAddress g, int port) {
//
//		group = g;
//		multcastPort = port;
//	}
//
//	public void run() {
//
//		try {
//
//			//System.out.println("receive\n\n");
//
//			// creating read socket
//			MulticastSocket readSocket = new MulticastSocket(multcastPort);
//			// making the socket join the multicast group
//			readSocket.joinGroup(group);
//
//			// keeping the listening thread always open and listening to the multicast port
//			// for any incoming message
//			while (true) {
//
//				byte[] incoming_message = new byte[MAX_MSG_LEN];
//				DatagramPacket packet = new DatagramPacket(incoming_message, incoming_message.length, group,
//						multcastPort);
//				
//
//				// receiving the incoming message
//				readSocket.receive(packet);
//				System.out.println("packet- "+packet.getData());
//
//				// printing the incoming message
//				String msg = new String(packet.getData());
//				System.out.println("Mesage read: " + msg);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}

public class Peer {

	public static void main(String[] args) throws Exception {

		// Scanner scanner = new Scanner(System.in);
		// String nodeId = null;
		Hashtable<String,Integer> groupTable = new Hashtable<String,Integer>();
		try {
			
			// creating thread to listen to multiple sources,if subscribed to multiple
			// groups
			int multcastPort = 8900;
			InetAddress group = InetAddress.getByName("226.4.5.6");
			

			// Listener messageListener = new Listener();
			// messageListener.start(id, port, this);
			ReadThread rt = new ReadThread(group, multcastPort,groupTable);
			rt.start();

			// giving process id as the identifier
			String processId = args[0];
			//String message = args[1];
			
			// Accepting message
			// TODO to accept heartbeat managemnet group list

			//SendHeartbeat shb = new SendHeartbeat(processId, message,multcastPort,group);
			SendHeartbeat shb = new SendHeartbeat(processId,multcastPort,group,groupTable);
			shb.start();
			
			int Counter = 0;
//			MulticastSocket testSocket = new MulticastSocket(multcastPort);
//			String heartbeatMessage = "ID:" + processId + ",BeatCounter:" + Counter; 
//			DatagramPacket packet = new DatagramPacket(heartbeatMessage.getBytes(), heartbeatMessage.length(), group, multcastPort);
//			testSocket.send(packet);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
