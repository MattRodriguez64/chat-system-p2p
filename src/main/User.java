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
			  //System.out.println(getIpAddress());
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
            ByeThread byeThread = new ByeThread(new DatagramSocket(Bye.byePort), this);
            byeThread.start();
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
	
	public void bye() throws IOException {
		DatagramSocket udpClientSocket;
		try {
			udpClientSocket = new DatagramSocket();
	        
	        Bye byeMessage = new Bye(this);
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(byeMessage);
	        
	        byte [] sendData = bos.toByteArray();
	        //if(!HelloThread.getUsersList().isEmpty()) {
		        //for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
		        	InetAddress currentIpAddress = InetAddress.getByName("192.168.1.255");
		        	//System.out.println(HelloThread.getUsersList().get(i).getIpAddress());
		        	udpClientSocket.setBroadcast(true);
			        udpClientSocket.connect(currentIpAddress, Bye.byePort);
		            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, currentIpAddress, Bye.byePort);
		            udpClientSocket.send(sendPacket);
		        //}
	        //}
		}catch (SocketException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
