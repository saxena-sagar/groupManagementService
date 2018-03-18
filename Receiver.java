import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.TreeSet;
public class Receiver extends Thread {
	
	InetAddress address;
int portNumber;
MulticastSocket multSocket;
TreeSet<Integer> userPorts = new TreeSet<Integer>();

 Receiver(InetAddress address, int portNumber) {
	this.address = address;
	this.portNumber = portNumber;
}

public void run() {

	try {
		multSocket = new MulticastSocket(portNumber);
		multSocket.joinGroup(address);
		while (true) {
			byte[] buffer = new byte[100];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, portNumber);

			multSocket.receive(packet);
			String message = new String(packet.getData());
			userPorts.add(packet.getPort());
			System.out.println(message);
		}
	} catch (IOException e) {
		
		e.printStackTrace();
	} finally {
		multSocket.close();
	}

}

public TreeSet<Integer> getUsers() {
	return userPorts;
}

@SuppressWarnings("unused")
private void printUsers() {
	for (Integer a : userPorts) {
		System.out.println(a);
	}
}

}



