package views;
/**
 * Represents the Login Window
 * Class : Login
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Login extends JFrame implements ActionListener {
	
	// CONSTANTS
	private static final long serialVersionUID = 1L;
  	private final int helloport = 7778;
  	private final int clientport = 7777;
  	
	// ATTRIBUTES
	private JButton connectButton;
	private JLabel nickNameLabel;
  	private JTextArea nickNameTextArea;
  	private static InetAddress broadcastIPAddress;

    // CONSTRUCTOR
	/** Creates a new instance of Login 
	*/
  	public Login(){

	}

	/**
	 * Make Actions on click button
	 * @param e A ActionEvent
	 * @exception Exception
	 */
  	public void actionPerformed(ActionEvent e) {
  		// Check if the Connect Button was pressed
  		if(e.getSource() == connectButton){
  			User localUser;

			try {
				// Create the local User based on the nickName he choose
				localUser = new User(nickNameTextArea.getText());
				
				// Get all the Network Interfaces
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				InetAddress broadcast;
				boolean ipFound = false;
				
				while (interfaces.hasMoreElements()) 
				{
				    NetworkInterface networkInterface = interfaces.nextElement();
				    if (networkInterface.isLoopback())
				        continue;    
				    
				    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) 
				    {
				    	// Get the Broadcast address from the current interface
				        broadcast = interfaceAddress.getBroadcast();
				        if (broadcast == null)
				            continue;

				        // Get the digits
				        String localUserIP = localUser.getIpAddress();
				        String[] localUserIPArray = localUserIP.split("\\.");
				        
				        String broadcastIP = ((String) broadcast.getHostAddress()).replace("/", "");
				        String[] broadcastIPArray = broadcastIP.split("\\.");
				        
				        // Check if the digits number are equals
				        if(broadcastIPArray.length == localUserIPArray.length) {
				        	int matchIPcounter = 0;
				        	for(int i = 0; i < broadcastIPArray.length; i++) {
				        		if(localUserIPArray[i].equals(broadcastIPArray[i])) {
				        			matchIPcounter++;
				        		}
				        	}
				        	if(matchIPcounter == (broadcastIPArray.length-1)) {
								// Prepare and send an Hello Request
				        		ipFound = true;
				        		setBroadcastIPAddress(broadcast);
				        		localUser.hello(broadcast, helloport);
				        	}
				        }
				    }
				    
				    // If the right IP found, we stop
				    if(ipFound)
				    	break;
				}

				// Create the ReceiverThread
		        ReceiverThread receiver = new ReceiverThread(new DatagramSocket(clientport));
				
		        // Create the ChatSystem Window
				ChatSystem chatSystem = new ChatSystem(localUser, receiver);
				chatSystem.initComponents();
				chatSystem.pack();
				chatSystem.setVisible(true);
				
				// Close the Login Window
				this.dispose();
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			

  		}
  	}

	/**
	 * Initialize all the components
	 */
  	public void initComponents() {
	    connectButton = new JButton("Connect");
	    connectButton.addActionListener(this);
	    JPanel panel = new JPanel();
	
	    nickNameLabel = new JLabel("NickName");
	    nickNameTextArea = new JTextArea(1, 20);
	
	    panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	
	    panel.setLayout(new GridLayout(3, 1, 10, 10));
	  
	    panel.add(nickNameLabel, BorderLayout.CENTER);
	    panel.add(nickNameTextArea, BorderLayout.CENTER);
	    panel.add(connectButton, BorderLayout.CENTER);

	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.add(panel, BorderLayout. NORTH);
  	}

	public static InetAddress getBroadcastIPAddress() {
		return broadcastIPAddress;
	}

	public static void setBroadcastIPAddress(InetAddress broadcastIPAddress) {
		Login.broadcastIPAddress = broadcastIPAddress;
	}

}