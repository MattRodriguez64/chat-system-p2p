package main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ByeThread extends Thread {
    
    private DatagramSocket udpClientSocket;
    private User currentUser;
    public static ArrayList<User> users = new ArrayList<User>();

    public ByeThread(DatagramSocket ds, User currentUser) throws SocketException {
        this.udpClientSocket = ds;
        setCurrentUser(currentUser);
    }

    public void run() {
    	byte[] incomingData = new byte[1024];
    	boolean isInsideList;
        
        while (true) {            

            DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);
            try {
				udpClientSocket.receive(receivePacket);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            byte[] receiveData = receivePacket.getData();
            
            ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
            ObjectInputStream is;
            Bye byeResponse = null;
			try {
				is = new ObjectInputStream(in);
				byeResponse = (Bye) is.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			isInsideList = false;
			if(!HelloThread.getUsersList().isEmpty()) {
				for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
					if(HelloThread.getUsersList().get(i).isEqual(byeResponse.getUser()))
						isInsideList = true;
				}
			}
			if(isInsideList) {
				users.add(byeResponse.getUser());
				System.out.println("BYE FROM : " + byeResponse.getUser().toString() + "\n");
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
	
	public static void setUsersList(ArrayList<User> newUsers) {
		users = newUsers;
	}
}
