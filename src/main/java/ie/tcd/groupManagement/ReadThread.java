package ie.tcd.groupManagement;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Hashtable;

//The readthread should be open first before anything else begins
//Maintaining seperate thread for recieving becasue member of group not sure about the number of senders and everytime a msg comes to the multicast group must be 
//received by reciever.So keeping the reading thread run in infinite loop continuosly listening to the multicast port for any message

public class ReadThread extends Thread {

	InetAddress group;
	int multcastPort;
	// Setting size for maximum message length to be multicasted across
	int MAX_MSG_LEN = 100;
	Hashtable<String,Integer> groupTable = new Hashtable<String,Integer> ();

	ReadThread(InetAddress g, int port,Hashtable<String,Integer> grouptable) {

		group = g;
		multcastPort = port;
		groupTable = grouptable;
	}

	public void run() {

		try {

			// creating read socket
			MulticastSocket readSocket = new MulticastSocket(multcastPort);
			// making the socket join the multicast group
			readSocket.joinGroup(group);

			// keeping the listening thread always open and listening to the multicast port
			// for any incoming message
			while (true) {

				byte[] incoming_message = new byte[MAX_MSG_LEN];
				DatagramPacket packet = new DatagramPacket(incoming_message, incoming_message.length, group,
						multcastPort);

				// receiving the incoming message
				readSocket.receive(packet);

				// parsing the input heartbeat
				// String[] commands = inMessage.split(",");

				// printing the incoming message
				String msg = new String(packet.getData());
				String[] commands = msg.split(",");
				int heartbeatCounter = 0;
				//int localLogicalTimeCounter = 0;
				String processid = null;

				for (String command : commands) {
					String[] messages = command.split(":");

					if (messages[0].equals("ID")) {
						processid = messages[1];
						// System.out.println("Member: " + memberId);
					} else if (messages[0].equals("BeatCounter")) {
						// memberList.put(memberId, messages[1]);
						// memberCount++;
						// System.out.println("CONNECT for " + messages[1] + " " + memberCount);
						heartbeatCounter = Integer.parseInt(messages[1]);
					} /*else if (messages[0].equals("logicalTime")) {

						localLogicalTimeCounter = Integer.parseInt(messages[1]);

					}*/
					groupTable.put(processid, heartbeatCounter);
					System.out.println("Group Table: " + groupTable);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
