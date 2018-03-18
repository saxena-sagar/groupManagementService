package ie.tcd.groupManagement.node;

import java.net.Socket;

public class Handler
{
	public Handler(Member member, Socket socket) {
		System.out.println("HERE for" + socket.getLocalPort());
	}

}
