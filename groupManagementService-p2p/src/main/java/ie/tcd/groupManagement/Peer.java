package ie.tcd.groupManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.joda.time.DateTime;

public class Peer {

	public static void main(String[] args) throws Exception {

		// Id of this node
		String MyID = args[0];

		List<String> portList = new ArrayList<String>();

		while (true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter Network to Join");

			String input = br.readLine();

			if (input != null) {
				
				String[] ports = input.split(",");
				for(String port : ports)
				{
					portList.add(port);
					
					ManagerThread mgT = new ManagerThread(MyID, port);
					mgT.start();
				}

			} else {
				System.out.println("Please enter a valid input:");
			}
		}

	}
}
