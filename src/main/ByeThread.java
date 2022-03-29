package main;
/**
 * Represents the ByeThread which constantly 
 * listen the Bye Request from the others Users
 * Class : ByeThread
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ByeThread extends Thread {
    
	// NO CONSTANTS
	// ATTRIBUTES
    private DatagramSocket udpClientSocket;
    private User currentUser;
    public static ArrayList<User> users = new ArrayList<User>();

    // CONSTRUCTOR
	/** Creates a ByeThread Object with the specified DatagramSocket and the local User.
	 * @param ds A DatagramSocket
	 * @param currentUser A User (represent the local User)
	 * @exception SocketException if the socket are not valid
	*/
    public ByeThread(DatagramSocket ds, User currentUser) throws SocketException {
        this.udpClientSocket = ds;
        setCurrentUser(currentUser);
    }

	/** Start the ByeThread
	 * @exception IOException
	 * @exception ClassNotFoundException
	*/
    public void run() {
    	/** Represent the incoming bytes data
    	 */
    	byte[] incomingData = new byte[1024];
    	boolean isInsideList;
        
        while (true) {            
        	// Create a UDP Packet from the incoming packet
            DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);
            try {
				udpClientSocket.receive(receivePacket);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            // Get the Data from the received packet
            byte[] receiveData = receivePacket.getData();
            
            // Deserialize the data
            ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
            ObjectInputStream is;
            
            // Create the space memory to receive the Bye Request
            Bye byeResponse = null;
			try {
				is = new ObjectInputStream(in);
				// Get the Bye request
				byeResponse = (Bye) is.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			isInsideList = false;
			// Check if the User was connected before his disconnection
			if(!HelloThread.getUsersList().isEmpty()) {
				for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
					if(HelloThread.getUsersList().get(i).isEqual(byeResponse.getUser()))
						isInsideList = true;
				}
			}
			// If the user was connected and now disconnected, we add this User to the ArrayList
			if(isInsideList) {
				users.add(byeResponse.getUser());
				System.out.println("BYE FROM : " + byeResponse.getUser().toString() + "\n");
			}
			// The thread is not doing anything particularly important and 
			// if any other threads/processes need to be run, they should run
			Thread.yield();
        }
    }

	/** Gets the Local User Object.
	 * @return An User Object representing the Local User
	*/
	public User getCurrentUser() {
		return currentUser;
	}

	/** Sets the Local User Object
	 * @param currentUser A User Object containing the local User
	*/
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	/** Gets the all Users Object.
	 * @return An List<User> Object representing all recently disconnected users
	*/
	public static ArrayList<User> getUsersList() {
		return users;
	}
	
	/** Sets all Users Object.
	 * @param newUsers A List<User> Object containing all new disconnected users
	*/
	public static void setUsersList(ArrayList<User> newUsers) {
		users = newUsers;
	}
}
