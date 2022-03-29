package main;
/**
 * Represents the ReceiverThread which constantly 
 * listen the Message from the others Users
 * Class : ReceiverThread
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import views.ChatSystem;

public class ReceiverThread extends Thread {
	
	// NO CONSTANTS
	// ATTRIBUTES
    private DatagramSocket udpClientSocket;
    private String newMessage = "";

    // CONSTRUCTOR
	/** Creates a ReceiverThread Object with the specified DatagramSocket.
	 * @param ds A DatagramSocket
	 * @exception SocketException if the socket are not valid
	*/
    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.udpClientSocket = ds;
    }

	/** Start the HelloThread
	 * @exception IOException
	*/
    public void run() {
        
        // Create a byte buffer/array for the receive Datagram packet
        byte[] receiveData = new byte[1024];
        
        while (true) {            

            // Set up a DatagramPacket to receive the data into
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                // Receive a packet from the server (blocks until the packets are received)
                udpClientSocket.receive(receivePacket);
                
                // Extract the reply from the DatagramPacket and send the Message to the Chat System View      
                setNewMessage(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                
                // print to the screen
                ChatSystem.updateReceivedMessage(getNewMessage());
                
    			// The thread is not doing anything particularly important and 
    			// if any other threads/processes need to be run, they should run
                Thread.yield();
            } 
            catch (IOException ex) {
            	ex.printStackTrace();
            }
        }
    }
    
	/** Gets the new message received.
	 * @return A String Object representing the last message received
	*/
	public String getNewMessage() {
		return newMessage;
	}

	/** Sets the new message received.
	 * @param newMessage A String Object containing the new message
	*/
	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

}