package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class User implements Serializable{
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 869804864982492186L;
	private String nickName;
	private String ipAddress;
	
	public User() {}
	
	public User(String nickName) throws SocketException {
		setNickName(nickName);
		setIpAddress();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	private void setIpAddress() throws SocketException {
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  this.ipAddress = (String) socket.getLocalAddress().getHostAddress();
			  System.out.println(getIpAddress());
			}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String message;
		message = "User : " + getNickName() + " - IpAddress : " + getIpAddress();
		return message;
	}
	
	public boolean isEqual(User u) {
		if((u.getNickName().equals(getNickName())) && (u.getIpAddress().equals(getIpAddress())))
			return true;
		return false;
	}
	
	public void send() {

	}
	
	public void receive() {
		
	}
	
	public void hello(InetAddress address, int serverport) throws IOException {
        // Create client DatagramSocket
        DatagramSocket udpClientSocket;
		try {
            HelloThread helloThread = new HelloThread(new DatagramSocket(serverport), this);
            helloThread.start();
			udpClientSocket = new DatagramSocket();
	        udpClientSocket.setBroadcast(true);
	        udpClientSocket.connect(address, serverport);

	        Hello helloMessage = new Hello(this, true);
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(helloMessage);

	        byte [] sendData = bos.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, serverport);
                
            // Send the UDP packet to server
            udpClientSocket.send(sendPacket);

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void bye() {
		
	}
	
	protected void finalize() throws Throwable{
		
	}
}
