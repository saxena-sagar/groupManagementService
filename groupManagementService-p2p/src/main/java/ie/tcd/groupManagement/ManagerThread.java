package ie.tcd.groupManagement;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Hashtable;

public class ManagerThread extends Thread {

	// To keep a track of the members in the group (ID, Heartbeat count)
	Hashtable<String, Integer> groupTable = new Hashtable<String, Integer>();
	// To keep a track of the time the members last communicated
	Hashtable<String, Integer> groupTimer = new Hashtable<String, Integer>();
	
	String multicastPort;

	// Id of this node
	String MyID;

	ManagerThread(String MyId, String portNo) {

		MyID = MyId;
		multicastPort = portNo;
		
	}

	public void run() {

		try {

			// Adding self to group table
			groupTable.put(MyID, 0);

			InetAddress group = InetAddress.getByName("226.4.5.6");

			System.out.println("Node-" + MyID + " Started");

			// Thread to listen to messages
			ReadThread rt = new ReadThread(group, multicastPort, groupTable, MyID, groupTimer);
			rt.start();

			// Thread to send heartbeats
			SendHeartbeat shb = new SendHeartbeat(MyID, multicastPort, group, groupTable);
			shb.start();

			// Thread to check last time a node messaged
			TimerThread tt = new TimerThread(MyID, groupTimer, groupTable);
			tt.start();

			// To Trigger initial connection
			String heartbeatMessage = "ID:" + MyID + ",JoinGroup:" + 0;
			MulticastSocket sendSocket = new MulticastSocket();
			DatagramPacket packet = new DatagramPacket(heartbeatMessage.getBytes(), heartbeatMessage.length(), group,
					Integer.parseInt(multicastPort));
			sendSocket.send(packet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
