package ie.tcd.groupManagement.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Member {

	public ServerSocket server = null;
	private Thread ListenIncomingMessages = null;

	HashMap<String, String> memberList = new HashMap<String, String>();
	public Handler memberArray[] = new Handler[100];
	public int memberCount = 0;

	public String sysId;
	public int sysport;

	public Member(String id, int port, Integer conn, boolean formGroup) {
		try {
			sysId = id;
			sysport = port;

			Listener messageListener = new Listener();
			messageListener.start(id, port, this);

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

		// Send Connection message
		DataOutputStream dataOutput = new DataOutputStream(memberSocket.getOutputStream());
		String outMessage = "ID:" + sysId + ",CONNECT:" + port;
		//System.out.println("@@@@@@@@@" + outMessage);
		dataOutput.writeUTF(outMessage);

		// Read Response
		String inMessage = null;
		DataInputStream dataInput = new DataInputStream(memberSocket.getInputStream());
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
		inMessage = dataInput.readUTF();
		//System.out.println("@@@@@@@@@" + inMessage);

		dataInput.close();
		dataOutput.close();
		memberSocket.close();

		// Parse inMessage and add ID an Port to HashMap

		String[] commands = inMessage.split(",");
		String memberId = null;
		for (String command : commands) {
			String[] messages = command.split(":");
			//System.out.println(messages[0] + " @@@@@@@@@@@ " + messages[1] + " @@@ " + messages.length);

			if (messages[0].equals("ID")) {
				memberId = messages[1];
				System.out.println("Member: " + memberId);
			} else if (messages[0].equals("CONNECT")) {
				memberList.put(memberId, messages[1]);
				memberCount++;
				System.out.println("CONNECT for " + messages[1] + " " + memberCount);
			}
		}

		Iterator iterator = memberList.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			System.out.print("key is: " + mentry.getKey() + " & Value is: ");
			System.out.println(mentry.getValue());
		}
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
