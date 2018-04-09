package ie.tcd.groupManagement;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

public class TimerThread extends Thread {

	String MyID = null;
	Hashtable<String, Integer> groupTable = new Hashtable<String, Integer>();
	Hashtable<String, Integer> groupTimer = new Hashtable<String, Integer>();

	TimerThread(String MyId, Hashtable<String, Integer> timerTable, Hashtable<String, Integer> grouptable) {
		groupTable = grouptable;
		groupTimer = timerTable;
		MyID = MyId;
	}

	public void run() {
		try {
			// Constantly check for Nodes who stoped messaging infinitely
			while (true) {
				// Check if anyone in the Group is present or not apart from self
				if (groupTable.size() > 1) {
					List<String> toRemove = new ArrayList<>();
					Set<String> keys = groupTimer.keySet();
					for (String key : keys) {
						
						//Ignore Self
						if (key == MyID) {
							continue;
						}
						
						Integer dt = groupTimer.get(key);

						SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
						Date now = new Date();
						Integer strTime = Integer.parseInt(sdfTime.format(now));
						
						//calculate if the last message got was more than 5 seconds ago
						if ((strTime - dt) > 5) {
							toRemove.add(key);
						}

					}
					//Check if any nodes to remove from the group
					if (!toRemove.isEmpty()) {
						for (String key : toRemove) {
							System.out.println("Removing key: " + key);
							groupTable.remove(key);
							groupTimer.remove(key);
						}
					}

				}
				Thread.sleep(5000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
