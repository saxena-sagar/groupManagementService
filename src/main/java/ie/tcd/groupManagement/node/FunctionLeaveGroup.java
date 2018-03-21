package ie.tcd.groupManagement.node;

import java.io.IOException;
import java.net.ServerSocket;


public class FunctionLeaveGroup extends Member implements Runnable {

    public ServerSocket server = null;
    public int memberCount = 0;

    public FunctionLeaveGroup(String id, int port, Integer conn) throws IOException {
        while (true) {
            try {
                removeMember(id, port);
                System.out.println(" Node " + id + " is removed from the group ");
            } catch (IOException e) {
                System.out.print("Node removal error : " + e);
            }
        }
    }

    private void removeMember(String id, int port) throws IOException {
        if (memberCount != 0) {
            memberCount--;
            System.out.println(" Node" + id + "on port" + port + "has been removed from the list ");
        } else System.exit(0);

    }

    public void close() throws IOException {
        try {
            server.close();
        } catch (IOException e) {
            System.out.print("Server closing error: " + e);
        }
    }
}






