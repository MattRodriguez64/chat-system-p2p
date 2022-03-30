package main;
/**
 * Represents the HelloThread which constantly 
 * listen the Hello Request from the others Users
 * Class : HelloThread
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class HelloThread extends Thread {
    
	// NO CONSTANTS
	// ATTRIBUTES
    private DatagramSocket udpClientSocket;
    private User currentUser;
    public static ArrayList<User> users = new ArrayList<User>();

    // CONSTRUCTOR
	/** Creates a HelloThread Object with the specified DatagramSocket and the local User.
	 * @param ds A DatagramSocket
	 * @param currentUser A User (represent the local User)
	 * @exception SocketException if the socket are not valid
	*/
    public HelloThread(DatagramSocket ds, User currentUser) throws SocketException {
        this.udpClientSocket = ds;
        setCurrentUser(currentUser);
    }

	/** Start the HelloThread
	 * @exception IOException
	 * @exception ClassNotFoundException
	*/
    public void run() {
    	/** Represent the incoming bytes data
    	 */
    	byte[] incomingData = new byte[1024];
        
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
            // Create the space memory to receive the Hello Request
            Hello helloResponse = null;
			try {
				is = new ObjectInputStream(in);
				// Get the Hello request
				helloResponse = (Hello) is.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			// Check the user who send the Hello Request
			if(!helloResponse.getUser().isEqual(currentUser)) {
				System.out.println("HELLO received from : " + helloResponse.getUser().getNickName() + " IP ADDR : " + helloResponse.getUser().getIpAddress() + " Answer needed : " + helloResponse.isAnswer());
				// Check if the User is already connected
				boolean isInsideList = false;
				if(!users.isEmpty()) {
					for(int i = 0; i < users.size(); i++) {
						if(users.get(i).isEqual(helloResponse.getUser()))
							isInsideList = true;
					}
				}
				// If not already connected, we add him
				if(!isInsideList)
					users.add(helloResponse.getUser());
			}
			
			// Check if an answer is needed
			if(helloResponse.isAnswer()) {
				// Create client DatagramSocket
		        DatagramSocket udpClientSocket;
				try {
					udpClientSocket = new DatagramSocket();
					InetAddress ia = InetAddress.getByName(helloResponse.getUser().getIpAddress());
					udpClientSocket.connect(ia, 7778);
					
					// Create an Hello Request
			        Hello helloMessage = new Hello(getCurrentUser(), false);
			        // Serialize the Hello Request
			        ByteArrayOutputStream bos = new ByteArrayOutputStream();
			        ObjectOutputStream oos = new ObjectOutputStream(bos);
			        oos.writeObject(helloMessage);
	
			        // Convert the Hello Request into bytes
			        byte [] sendData = bos.toByteArray();
			        
			        // Create the UPD Packet
		            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ia, 7778);

		            // Send the UDP packet to the sender of the previous Hello Request
		            udpClientSocket.send(sendPacket);
				}	catch(IOException e) {
					e.printStackTrace();
				}
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
	 * @return An List<User> Object representing all connected users
	*/
	public static ArrayList<User> getUsersList() {
		return users;
	}
}