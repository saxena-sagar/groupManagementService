package ie.tcd.groupManagement.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Member {

	public ServerSocket server = null;
	private Thread ListenIncomingMessages = null;

	public Handler memberArray[] = new Handler[100];
	public int memberCount = 0;

	public String sysId;
	public int sysport;

	public Member(String id, int port, Integer conn, boolean formGroup) {
		try {
			sysId = id;
			sysport = port;
			//System.out.println("Starting this Member: " + sysId + " on port number: " + sysport);
			
			Listener messageListener = new Listener();
			messageListener.start(id, port, this);
			
			///ListenIncomingMessages = new Thread(this); // starting a thread to accept incoming connections
			///ListenIncomingMessages.start();

			// Connect to mentioned server port to join the group
			if (conn != null) {
				ConnectToServer(conn, sysport);
				if (formGroup == true) {
					// Form a Group
				}
			}
		} catch (Exception ex) {

		}
	}

	// Server main Thread to accept and complete incoming connection
	/*public void run() {
		while (ListenIncomingMessages != null) {
			try {
				System.out.println("Waiting for a client ...");

				Socket socket = server.accept();
				DataInputStream dataInput = new DataInputStream(socket.getInputStream());
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

				String inMessage = dataInput.readUTF();
				String[] commands = inMessage.split(",");

				for (String command : commands) {
					String[] messages = command.split(":");

					if (messages[0] == "ID") {
						sysId = messages[1];
					}
					if (messages[0] == "CONNECT") {
						System.out.println("HERE for " + messages[1] + " " + memberCount);
						System.out.println();
						// ADD socket to handler
					}
					if (messages[0] == "LEAVE") {
						System.out.println("LEAVE for " + messages[1] + " " + memberCount);
					}
					if (messages[0] == "HEARTBEAT") {
						System.out.println("HEARTBEAT for " + messages[1] + " " + memberCount);
					} else {
						System.out.println("INVALID MESSAGE");
					}
				}
			} catch (IOException ioe) {
				System.out.print("Server accept error: " + ioe);
				// stop();
			}
		}
	}*/

	private void addMember(Socket socket) {
		if (memberCount < memberArray.length) {
			try {
				memberArray[memberCount] = new Handler(this, socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			memberCount++;

		} else {
			System.out.println("Max length " + memberArray.length + " reached.");
		}
	}

	public void ConnectToServer(Integer conn, int port) throws UnknownHostException, IOException {
		Socket memberSocket = new Socket("localhost", conn);
		DataInputStream dataInput = new DataInputStream(memberSocket.getInputStream());
		DataOutputStream dataOutput = new DataOutputStream(memberSocket.getOutputStream());

		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

		String outMessage = "ID:" + sysId + ",CONNECT:" + port;
		System.out.println("@@@@@@@@@" + outMessage);
		String inMessage = null;
		dataOutput.writeUTF(outMessage);
		inMessage = dataInput.readUTF();
		System.out.println(inMessage);

		dataInput.close();
		dataOutput.close();

	}

	public static void main(String[] args) {

		// String ipAddress = null;
		int port = 8080;
		Integer conn = 0000; // used to connect to the already up and running node
		String usage = "Please Input" + " [-id identification Code] [-port port Number]" + " [-con connect to a port]"
				+ "[-form True/False]";
		String id = null;
		boolean formGroup = false;

		for (int i = 0; i < args.length; i++) {
			if ("-id".equals(args[i])) {
				id = args[i + 1];
				i++;
			} else if ("-port".equals(args[i])) {
				port = Integer.parseInt(args[i + 1]);
				i++;
			} else if ("-con".equals(args[i])) {
				conn = Integer.parseInt(args[i + 1]);
				i++;
			} else if ("-form".equals(args[i])) {
				formGroup = Boolean.parseBoolean(args[i + 1]);
				i++;
			} else {
				System.out.println(usage);
				System.exit(0);
			}
		}
		new Member(id, port, conn, formGroup);

	}

}
