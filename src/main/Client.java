package main;

import java.io.*;  // Imported because we need the InputStream and OuputStream classes
import java.net.*; // Imported because the Socket class is needed

public class Client{
    


	public static void main(String args[]) throws Exception {  
    	
    	User u = new User("Matt");
    	//System.out.println(u.toString());
        
        // The default port     
        int clientport = 7777;
        int helloport = 7778;
        String host = "192.168.1.255";

        // Get the IP address of the local machine - we will use this as the address to send the data to
        InetAddress ia = InetAddress.getByName(host);
        u.hello(ia, helloport);
        
        SenderThread sender = new SenderThread(ia, clientport);
        sender.start();
        ReceiverThread receiver = new ReceiverThread(new DatagramSocket(clientport));
        receiver.start();
    }
}      