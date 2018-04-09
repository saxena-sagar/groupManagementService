package ie.tcd.groupManagement;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.joda.time.DateTime;

public class ReadThread extends Thread {

	//To keep a track of the members in the group
	Hashtable<String, Integer> groupTable = new Hashtable<String, Integer>(); 
	
	//To keep a track of the time the members last communicated
	Hashtable<String, Integer> groupTimer = new Hashtable<String, Integer>();
	
	//Id of this node
	String MyID = null;
	
	InetAddress group;
	String multicastPort;
	int MAX_MSG_LEN = 100;


	//Constructor
	ReadThread(InetAddress g, String port, Hashtable<String, Integer> grouptable, String myId,
			Hashtable<String, Integer> timerTable) {
		group = g;
		multicastPort = port;
		groupTable = grouptable;
		MyID = myId;
		groupTimer = timerTable;
	}

	public void run() {

		try {

			String NodeId = null;
			int heartbeatCounter = 0;

			MulticastSocket readSocket = new MulticastSocket(Integer.parseInt(multicastPort));
			readSocket.joinGroup(group);

			//Listen on port infinitely
			while (true) {

				//Get incoming message
				byte[] incoming_message = new byte[MAX_MSG_LEN];
				DatagramPacket packet = new DatagramPacket(incoming_message, incoming_message.length, group,
						Integer.parseInt(multicastPort));
				readSocket.receive(packet);

				//Split the commands
				String msg = new String(packet.getData());
				String[] commands = msg.split(",");

				for (String command : commands) {
					String[] messages = command.split(":");

					if (messages[0].equals("ID")) {
						NodeId = messages[1];
					} 
					// For 'Heart Beat' messages
					else if (!(NodeId.equals(MyID)) && messages[0].equals("BeatCounter")) {
						// call table updater with clock updater
						heartbeatCounter = Integer.parseInt(messages[1].trim());
						groupTable.put(NodeId, heartbeatCounter);
						
						SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
					    Date now = new Date();
						groupTimer.put(NodeId, Integer.parseInt(sdfTime.format(now)));
						System.out.println(groupTable);
						// System.out.println("From " + NodeId + " counter " + heartbeatCounter);
					} 
					// For 'Leaving the Group' messages
					else if (!(NodeId.equals(MyID)) && messages[0].equals("LeaveGroup")) {
						// LeaveGroup Function
					} 
					// For 'Joining the Group' messages
					else if (!(NodeId.equals(MyID)) && messages[0].equals("JoinGroup")) {
						groupTable.put(NodeId, 0);
						SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
					    Date now = new Date();
					    String strTime = sdfTime.format(now);
						groupTimer.put(NodeId, Integer.parseInt(strTime));
						System.out.println("New Node Added:" + NodeId);
					}

				}

			}
			// readSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
