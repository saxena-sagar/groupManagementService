package ie.tcd.groupManagement.node;

import java.net.ServerSocket;

public class Member {

	public static void main(String[] args) {
		private ServerSocket server = null;
		String ipAddress = null;
		String port = null;
		String usage = "Please Input" + " [-id identification Code] [-port port Number]";
		String id = null;
		for (int i = 0; i < args.length; i++) {
			if ("-id".equals(args[i])) {
				id = args[i + 1];
				i++;
			} else if ("-port".equals(args[i])) {
				port = args[i + 1];
				i++;
			}
		}

	}

}
