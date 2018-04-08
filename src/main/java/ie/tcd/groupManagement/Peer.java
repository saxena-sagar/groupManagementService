package ie.tcd.groupManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

//The readthread should be open first before anything else begins
//Maintaining sepea=rate thread for recieving becasue member of group not sure about the number of senders and everytime a msg comes to the multicast group must be 
//received by reciever.So keeping the reading thread run in infinite loop continuosly listening to the multicast port for any message
class readThread extends Thread {

	InetAddress group;
	int multcastPort;
	// Setting size for maximum message length to be multicasted across
	int MAX_MSG_LEN = 100;

	readThread(InetAddress g, int port) {

		group = g;
		multcastPort = port;
	}

	public void run() {

		try {

			System.out.println("receive\n\n");

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
				System.out.println("packet- "+packet.getData());

				// printing the incoming message
				String msg = new String(packet.getData());
				System.out.println("Mesage read: " + msg);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

public class Peer {

	public static void main(String[] args) throws Exception {

		// Scanner scanner = new Scanner(System.in);
		// String nodeId = null;
		try {

			// hardcoding port and multicast address from the range
			int multcastPort = 8900;
			InetAddress group = InetAddress.getByName("226.4.5.6");

			// single sender in the groupcase
			MulticastSocket sendSocket = new MulticastSocket();

			// creating thread to listen to mulitple sources,if subscribed to multiple
			// groups
			readThread rt = new readThread(group, multcastPort);
			rt.start();

			// giving process id as the identifier
			int processId = Integer.parseInt(args[0]);

			// Accepting message
			// TODO to accept heartbeat managemnet group list

			String message = args[1];

			String multicastMessage = "Message from process#" + processId + " : " + message;
			byte[] msg = multicastMessage.getBytes();

			DatagramPacket packet = new DatagramPacket(msg, msg.length, group, multcastPort);

			// wait for all clients to enter the group to send message
			System.out.println("Hit return to send message\n\n");


			Scanner scanner = new Scanner(System.in);
			String readString = scanner.nextLine();
			while (readString != null) {
				if (readString.isEmpty()) {
					sendSocket.send(packet);
				}
			}

			

			// Here sending only one message so not running in loop but TODO need to
			// substitute with sending heartbeat every 't' secs which would be in loop
			// sendSocket.send(packet);

			sendSocket.close();
			System.out.println("post sock close\n\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
