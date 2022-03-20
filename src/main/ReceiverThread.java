package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

class ReceiverThread extends Thread {
    
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;

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
                String serverReply =  new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                // print to the screen
                System.out.println("UDPClient: Response from Server: \"" + serverReply + "\"\n");
                
                Thread.yield();
            } 
            catch (IOException ex) {
            System.err.println(ex);
            }
        }
    }
}