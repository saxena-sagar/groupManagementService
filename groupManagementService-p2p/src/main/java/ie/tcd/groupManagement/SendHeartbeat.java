package ie.tcd.groupManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Hashtable;

public class SendHeartbeat extends Thread {

	InetAddress group;
	String multicastPort;
	int heartbeatCounter = 1;
	String MyID = null;
	Hashtable<String,Integer> groupTable = new Hashtable<String,Integer>();
	
	SendHeartbeat(String pid, String port,InetAddress address, Hashtable<String,Integer> grouptable) {

		MyID = pid;
		multicastPort = port;
		group = address;
		groupTable = grouptable;
	}

	// Send heartbeats after regular intervals of time infinitely
	public void run() {

		try {

			MulticastSocket sendSocket = new MulticastSocket();
			
			while(true) {
				// Check if anyone in the Group is present or not apart from self
				if(groupTable.size()>1) {
				heartbeatCounter++;
				String heartbeatMessage = "ID:" + MyID + ",BeatCounter:" + heartbeatCounter; 
				
				DatagramPacket packet = new DatagramPacket(heartbeatMessage.getBytes(), 
						heartbeatMessage.length(), group, Integer.parseInt(multicastPort));					
				sendSocket.send(packet);
				
				groupTable.put(MyID, heartbeatCounter);

				}	
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
