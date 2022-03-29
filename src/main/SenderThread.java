package main;
/**
 * Represents the SenderThread which is used to 
 * send message to the others users
 * Class : SenderThread
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SenderThread extends Thread {
    
	// NO CONSTANTS
	// ATTRIBUTES
    private InetAddress remoteIPAddress;
    private DatagramSocket udpClientSocket;
    private int remotePort;
    private String localMessage = "";

    // CONSTRUCTOR
	/** Creates a SenderThread Object with the specified IP Address and port.
	 * @param address An InetAddress
	 * @param remotePort An Integer
	 * @exception SocketException if the socket are not valid
	*/
    public SenderThread(InetAddress address, int remotePort) throws SocketException {
        this.remoteIPAddress = address;
        this.remotePort = remotePort;
        // Create client DatagramSocket
        this.udpClientSocket = new DatagramSocket();
        this.udpClientSocket.setBroadcast(true);
        this.udpClientSocket.connect(remoteIPAddress, remotePort);
    }

	/** Gets the current DatagramSocket.
	 * @return A DatagramSocket Object representing the current UDP socket
	*/
    public DatagramSocket getSocket() {
        return this.udpClientSocket;
    }

	/** Start the HelloThread
	 * @exception IOException
	*/
    public void run() {       
        try {         
            while (true) {
            	// Check if the message to send is not "empty"
                if(!getLocalMessage().equals("")) {
                	// Message to send
	                String clientMessage = getLocalMessage();

	                // Create byte buffer to hold the message to send
	                byte[] sendData = new byte[1024];
	                  
	                // Put this message into our empty buffer/array of bytes
	                sendData = clientMessage.getBytes();
	                    
	                // Create a DatagramPacket with the data, IP address and port number
	                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, remoteIPAddress, remotePort);
	                    
	                // Send the UDP packet to remote user
	                udpClientSocket.send(sendPacket);
                    setLocalMessage("");
          
                }
    			// The thread is not doing anything particularly important and 
    			// if any other threads/processes need to be run, they should run
                Thread.yield();
            }
        }
        catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
    
	/** Gets the Local Message.
	 * @return A String Object representing the message
	*/
	public String getLocalMessage() {
		return localMessage;
	}
	
	/** Sets the Local Message.
	 * @param localMessage A String Object containing the message
	*/
	public void setLocalMessage(String localMessage) {
		this.localMessage = localMessage;
	}
	
	/** Gets the Remote User IP Address.
	 * @return A InetAddress Object representing the Remote User IP Address
	*/
	public InetAddress getRemoteIPAddress() {
		return this.remoteIPAddress;
	}
	
	/** Sets the Remote User IP Address.
	 * @param remoteIPAddress A InetAddress Object containing the Remote User IP Address
	*/
	public void setRemoteIpAddress(InetAddress remoteIPAddress) {
		this.remoteIPAddress = remoteIPAddress;
	}
}   