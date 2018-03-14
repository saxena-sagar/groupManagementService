import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.TreeSet;
public class Sender {
	
	public static void main(String[] args) throws Exception {
	
	MulticastSocket multSocket = null;
	Scanner scanner = new Scanner(System.in);
	int multcastPort = 1234;
	System.out.println("Enter Username!!");
	String userName = new String(scanner.nextLine());
	System.setProperty("java.net.preferIPv4Stack", "true");

	try {
		System.out.println("Please Enter the GroupID !! ");
		System.out.println("Groups available from 1 to 10");
		int groupID = scanner.nextInt();
		if (groupID < 1 && groupID > 10) {
			System.out.println("Invalid Group number.");
			throw new Exception("Invalid Group number");
		}

		String groupName = new String("225.8.4.");
		InetAddress group = InetAddress.getByName(groupName + groupID);

		multSocket = new MulticastSocket();

		Receiver receiver = new Receiver(group, multcastPort);
		receiver.start();

		

		System.out.println("Enter message and press Enter!!");
		while (true) {
			String message = scanner.nextLine();
			if (message.equals("/users")) {
				printUsers(receiver.getUsers());
				continue;
			} else {
				String messageString = new String("Message is received from " + userName + " :: " + message);
				byte[] messagePacketInBytes = messageString.getBytes();

				DatagramPacket packet = new DatagramPacket(messagePacketInBytes, messagePacketInBytes.length, group,
						multcastPort);
				

				multSocket.send(packet);
			}
		}

	} catch (UnknownHostException e) {
		
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	} finally {
		multSocket.close();
		scanner.close();
	}

}

private static void printUsers(TreeSet<Integer> userPorts) {
	for (Integer a : userPorts) {
		System.out.println(a);
	}
}

}
