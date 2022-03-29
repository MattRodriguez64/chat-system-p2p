package main;

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
    
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;
    private User currentUser;
    public static ArrayList<User> users = new ArrayList<User>();

    public HelloThread(DatagramSocket ds, User currentUser) throws SocketException {
        this.udpClientSocket = ds;
        setCurrentUser(currentUser);
    }

    public void halt() {
        this.stopped = true;
    }

    public void run() {
    	byte[] incomingData = new byte[1024];
        
        while (true) {            
            if (stopped)
            	return;
            
            DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);
            try {
				udpClientSocket.receive(receivePacket);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            byte[] receiveData = receivePacket.getData();
            
            ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
            ObjectInputStream is;
            Hello helloResponse = null;
			try {
				is = new ObjectInputStream(in);
				helloResponse = (Hello) is.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(!helloResponse.getUser().isEqual(currentUser)) {
				System.out.println("HELLO received from : " + helloResponse.getUser().getNickName() + " IP ADDR : " + helloResponse.getUser().getIpAddress() + " Answer needed : " + helloResponse.isAnswer());
				boolean isInsideList = false;
				if(!users.isEmpty()) {
					for(int i = 0; i < users.size(); i++) {
						if(users.get(i).isEqual(helloResponse.getUser()))
							isInsideList = true;
					}
				}
				if(!isInsideList)
					users.add(helloResponse.getUser());
			}
			
			if(helloResponse.isAnswer()) {
		        DatagramSocket udpClientSocket;
				try {
					udpClientSocket = new DatagramSocket();
					InetAddress ia = InetAddress.getByName(helloResponse.getUser().getIpAddress());
					udpClientSocket.connect(ia, 7778);
	
			        Hello helloMessage = new Hello(getCurrentUser(), false);
			        ByteArrayOutputStream bos = new ByteArrayOutputStream();
			        ObjectOutputStream oos = new ObjectOutputStream(bos);
			        oos.writeObject(helloMessage);
	
			        byte [] sendData = bos.toByteArray();
			        
		            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ia, 7778);

		            udpClientSocket.send(sendPacket);
				}	catch(IOException e) {
					e.printStackTrace();
				}
			}
			Thread.yield();
        }
    }

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public static ArrayList<User> getUsersList() {
		return users;
	}
}