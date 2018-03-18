package ie.tcd.groupManagement.node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Member implements Runnable {

	public ServerSocket server = null;
	private Thread IncomingConnThread = null;
	
	private Handler memberArray[] = new Handler[100];
	public int memberCount = 0;

	public Member(String id, int port, Integer conn, boolean formGroup) {
		try {
			server = new ServerSocket(port);
			System.out.println("Starting Member:" + id + " on port number:" + port);

			IncomingConnThread = new Thread(this); // starting a thread to accept incoming connections
			IncomingConnThread.start();

			// Connect to mentioned server port to join the group
			if(conn != null)
			{
				Socket memberSocket = new Socket("localhost",conn);
				DataInputStream dataInput = new DataInputStream(memberSocket.getInputStream());
				DataOutputStream dataOutput = new DataOutputStream(memberSocket.getOutputStream());
				
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
				
				String outMessage = "CONNECT:"+ conn;
				String inMessage = null;
				dataOutput.writeUTF(outMessage);
				inMessage = dataInput.readUTF();
				System.out.println(inMessage);
							
			}
		} catch (Exception ex) {

		}
	}

	// Server main Thread to accept and complete incoming connection
	public void run() {
		while (IncomingConnThread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addMember(server.accept());
			} catch (IOException ioe) {
				System.out.print("Server accept error: " + ioe);
				// stop();
			}
		}
	}

	private void addMember(Socket socket) {
		if (memberCount < memberArray.length) {
			memberArray[memberCount] = new Handler(this, socket);
			memberCount++;

		} else {
			System.out.println("Max length " + memberArray.length + " reached.");
		}
	}

	public static void main(String[] args) {

		// String ipAddress = null;
		int port = 8080;
		Integer conn = 0000; // used to connect to the already up and running node
		String usage = "Please Input" + " [-id identification Code] [-port port Number]"
				+ " [-con connect to a port]" + "[-form True/False]";
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
