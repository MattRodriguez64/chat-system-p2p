package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import views.ChatSystem;

public class ReceiverThread extends Thread {
    
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;
    private String oldMessage = "";
    private String newMessage = "";
    private boolean update = true;

    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.udpClientSocket = ds;
    }

    public void halt() {
        this.stopped = true;
    }

    public void run() {
        
        // Create a byte buffer/array for the receive Datagram packet
        byte[] receiveData = new byte[1024];
        
        while (true) {            
            if (stopped)
            return;
            
            // Set up a DatagramPacket to receive the data into
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                // Receive a packet from the server (blocks until the packets are received)
                udpClientSocket.receive(receivePacket);
                
                // Extract the reply from the DatagramPacket      
                setNewMessage(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                
                // print to the screen
                System.out.println("UDPClient: Response from Server: \"" + getNewMessage() + "\"\n");
                ChatSystem.updateReceivedMessage(getNewMessage());
                
                Thread.yield();
            } 
            catch (IOException ex) {
            System.err.println(ex);
            }
        }
    }

	public String getOldMessage() {
		return oldMessage;
	}

	public void setOldMessage(String oldMessage) {
		this.oldMessage = oldMessage;
	}

	public String getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
}