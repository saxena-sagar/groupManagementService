package ie.tcd.groupManagement.node;

import java.net.ServerSocket;

public class Member {
	
	public ServerSocket server = null;

	public Member(String id, int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Starting Member:" + id + " on port number:" + port);
			
		}
	}

	public static void main(String[] args) {

		String ipAddress = null;
		int port;
		String usage = "Please Input" + " [-id identification Code] [-port port Number]";
		String id = null;
		for (int i = 0; i < args.length; i++) {
			if ("-id".equals(args[i])) {
				id = args[i + 1];
				i++;
			} else if ("-port".equals(args[i])) {
				port = Integer.parseInt(args[i + 1]);
				i++;
			}
		}
		new Member(id, port);

	}

}
