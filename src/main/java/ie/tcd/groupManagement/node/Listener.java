package ie.tcd.groupManagement.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class Listener implements Runnable {

	private Thread ListenIncomingMessages = null;
	ServerSocket server = null;
	public Member systemMember = null;

	public void start(String sysId, int sysPort, Member systemMember) {

		System.out.println("Starting Listener on Port " + sysPort);
		this.systemMember = systemMember;

		try {
			server = new ServerSocket(sysPort);

			System.out.println("Starting this Member: " + sysId + " on port number: " + sysPort);
			if (ListenIncomingMessages == null) {
				ListenIncomingMessages = new Thread(this, "messageListener");
				ListenIncomingMessages.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String memberId = null;
		while (ListenIncomingMessages != null) {
			try {
				System.out.println("Waiting for a client ...");

				Socket socket = server.accept();
				DataInputStream dataInput = new DataInputStream(socket.getInputStream());
				// BufferedReader buffReader = new BufferedReader(new
				// InputStreamReader(System.in));

				String inMessage = dataInput.readUTF();
				System.out.println(inMessage);
				String[] commands = inMessage.split(",");

				for (String command : commands) {
					String[] messages = command.split(":");
					// System.out.println(messages[0] + " @@@@@@@@@@@ " + messages[1] + " @@@ " +
					// messages.length);

					if (messages[0].equals("ID")) {
						memberId = messages[1];
						// System.out.println("Member: " + memberId);
					} else if (messages[0].equals("CONNECT")) {
						System.out.println("CONNECT for " + messages[1] + " " + systemMember.memberCount);
						systemMember.memberList.put(memberId, messages[1]);

						// Send a responce to the member ID and Port
						DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
						String outMessage = "ID:" + systemMember.sysId + ",CONNECT:" + systemMember.sysport;
						// System.out.println("@@@@@@@@@ To member after Connect" + outMessage);
						dataOutput.writeUTF(outMessage);

						Iterator iterator = systemMember.memberList.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry mentry = (Map.Entry) iterator.next();
							System.out.print("key is: " + mentry.getKey() + " & Value is: ");
							System.out.println(mentry.getValue());
						}

					} else if (messages[0].equals("LEAVE")) {
						System.out.println("LEAVE for " + messages[1] + " " + systemMember.memberCount);
						// Call function to remove member port and id from global, also reduce count
					} else if (messages[0].equals("HEARTBEAT")) {
						System.out.println("HEARTBEAT for " + messages[1] + " " + systemMember.memberCount);
						// Call function to increase Timeout for the given member
					} else {
						System.out.println("INVALID MESSAGE");
					}
				}
			} catch (IOException ioe) {
				System.out.print("Server accept error: " + ioe);
				// stop();
			}
		}
	}
}
