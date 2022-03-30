package main;
/**
 * Represents an User
 * Class : User
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import views.Login;

public class User implements Serializable{

	// CONSTANTS
	private static final long serialVersionUID = 869804864982492186L;
	
	// ATTRIBUTES
	private String nickName;
	private String ipAddress;
	
	// CONSTRUCTORS
	public User() {}
	
	/** Creates a User Object with the specified nickName.
	 * @param nickName The User's nickName.
	*/
	public User(String nickName) throws SocketException {
		setNickName(nickName);
		setIpAddress();
	}

	/** Gets the User's nickName.
	 * @return A String representing the User's nickName
	*/
	public String getNickName() {
		return nickName;
	}

	/** Sets the User's nickName
	 * @param nickName A String containing the user's nickName
	*/
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/** Gets the User's IP Address.
	 * @return A String representing the User's IP Address
	*/
	public String getIpAddress() {
		return ipAddress;
	}

	/** Sets the User's IP Address
	 * If we have multiple network interface because we're using Docker, etc ...,
	 * By default getLocalAddress() will return the first address found 
	 * To return the correct address, a connection is made with the Google DNS to get
	 * the current Local IP Address
	*/
	private void setIpAddress() throws SocketException {
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  this.ipAddress = (String) socket.getLocalAddress().getHostAddress();
			}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	//STANDARD METHODS 	
	/** Display the current User Object.
	 * @return A String containing the user values
	*/
	public String toString() {
		String message;
		message = "User : " + getNickName() + " - IpAddress : " + getIpAddress();
		return message;
	}
	
	//STANDARD METHODS 	
	/** Display the current Bye Object.
	 * @param u A User to compare with the current instance of User
	 * @return A boolean containing : true if the two Objects are the same
	 * 								  false else.
	*/
	public boolean isEqual(User u) {
		if((u.getNickName().equals(getNickName())) && (u.getIpAddress().equals(getIpAddress())))
			return true;
		return false;
	}
	
	//SPECIFIC METHODS
	/** Start the Hello and Bye Thread, and send a Hello in broadcast
	 * to manifest itself to others.
	 * @param address A InetAddress to send a Hello Request, 
	 * (it can be the broadcast Address the first time)
	 * @param remotePort A integer which contain the port to use 
	 * (only for Hello Request)
	 * @exception IOException if the serialize process fail
	 * @exception SocketException if the socket are not valid
	*/
	public void hello(InetAddress address, int remotePort) throws IOException {
        // Create client DatagramSocket
        DatagramSocket udpClientSocket;
		try {
			// Create and Start the HelloThread
            HelloThread helloThread = new HelloThread(new DatagramSocket(remotePort), this);
            helloThread.start();
            // Create and Start the ByeThread
            ByeThread byeThread = new ByeThread(new DatagramSocket(Bye.byePort), this);
            byeThread.start();
            
			udpClientSocket = new DatagramSocket();
	        udpClientSocket.setBroadcast(true);
	        udpClientSocket.connect(address, remotePort);

	        // Create an Hello Request
	        Hello helloMessage = new Hello(this, true);
	        // Serialize the Hello Request
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(helloMessage);

	        // Convert the Hello Request into bytes
	        byte [] sendData = bos.toByteArray();
	        
	        // Create the UPD Packet
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, remotePort);
                
            // Send the UDP packet to the broadcast address
            udpClientSocket.send(sendPacket);

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/** Send a Bye Request to the broadcast address
	 * @exception IOException if the serialize process fail
	 * @exception SocketException if the socket are not valid
	 * @exception UnknownHostException if the host is unknown/unreachable
	*/
	public void bye() throws IOException {
		// Create client DatagramSocket
		DatagramSocket udpClientSocket;
		try {
			udpClientSocket = new DatagramSocket();
			
			// Create an Bye Request
	        Bye byeMessage = new Bye(this);
	        // Serialize the Bye Request
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(byeMessage);
	        
	        // Convert the Bye Request into bytes
	        byte [] sendData = bos.toByteArray();
	        
		    udpClientSocket.setBroadcast(true);
			udpClientSocket.connect(Login.getBroadcastIPAddress(), Bye.byePort);
			
			// Create the UPD Packet
		    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Login.getBroadcastIPAddress(), Bye.byePort);
		    
		    // Send the UDP packet to the broadcast address
		    udpClientSocket.send(sendPacket);

		}catch (SocketException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
