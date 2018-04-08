package ie.tcd.groupManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Hashtable;

public class SendHeartbeat extends Thread {

	String processid = null;
	//String Heartbeat = null;
	InetAddress group;
	int multicastPort;
	int heartbeatCounter = 0;
	//int localLogicalTimeCounter = 0;
	Hashtable<String,Integer> heartBeatMessage = new Hashtable<String,Integer> ();
	
	//SendHeartbeat(String pid, String heartbeat,int port,InetAddress address) {
	SendHeartbeat(String pid, int port,InetAddress address,Hashtable<String,Integer> heartBeatMsg) {

		processid = pid;
		//Heartbeat = heartbeat;
		multicastPort = port;
		group = address;
		heartBeatMessage = heartBeatMsg;
	}

	public void run() {

		try {

			// hardcoding port and multicast address from the range
			
			// single sender in the groupcase
			MulticastSocket sendSocket = new MulticastSocket();
			
			
			
			
			
			//String multicastMessage = "Message from process#" + processid + " : " + Heartbeat;
			//+ ",logicalTime:" + localLogicalTimeCounter;
			
			//byte[] msg = multicastMessage.getBytes();
			//byte[] msg = heartbeatMessage.getBytes();
			
			//DatagramPacket packet = new DatagramPacket(msg, msg.length, group, multicastPort);

			// wait for all clients to enter the group to send message
			//System.out.println("Hit return to send message\n\n");

			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//br.readLine();
			// continuos sending logic...
			// Scanner scanner = new Scanner(System.in);
			// String readString = scanner.nextLine();
			// while (readString != null) {
			// if (readString.isEmpty()) {
			// sendSocket.send(packet);
			// }
			// }
			

			// Here sending only one message so not running in loop but TODO need to
			// substitute with sending heartbeat every 't' secs which would be in loop
			// sendSocket.send(packet);

			
			
			//Send the heartbeat every 't' sec
			while(true) {
				
				
				heartbeatCounter++;
				String heartbeatMessage = "ID:" + processid + ",BeatCounter:" + heartbeatCounter; 

				//heartBeatMessage.put(processid, heartbeatCounter);
				
				DatagramPacket packet = new DatagramPacket(heartbeatMessage.getBytes(), heartbeatMessage.length(), group, multicastPort);
				
				//if there are any other nodes in the group the source node would multicast in the group only then
				if(heartBeatMessage.size()>1) {
					
					sendSocket.send(packet);
					//delaying the sending of heartbeat for 5 sec to decerese the entwork load and reduce packet drop
					Thread.sleep(5000);
				}
				
				
				
				
			}
			
			//sendSocket.close();
			
			//System.out.println("post sock close\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
