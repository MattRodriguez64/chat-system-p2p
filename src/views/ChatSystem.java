package views;
/**
 * Represents the ChatSystem Window
 * Class : ChatSystem
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import main.*;

import javax.swing.*;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatSystem extends JFrame implements ActionListener {
	
	// CONSTANTS
	private static final long serialVersionUID = 1L;
	// ATTRIBUTES
	private User localUser;
	private ReceiverThread receiver;
	private JButton sendButton;
	private JButton reloadDataButton;
	private JLabel receiveLabel;
	private JLabel sendLabel;
	private JTextArea sendTextArea;
  	private static JTextArea receiveTextArea;
  	private static String currentRemoteUserIpAddress;
  	
    // CONSTRUCTOR
	/** Creates a new instance of ChatSystem 
	 * @param localUser A local User
	 * @param receiver A ReceiverThread to listen the incoming message
	 * @throws IOException 
	 */
  	public ChatSystem(User localUser, ReceiverThread receiver) throws IOException {
  		setLocalUser(localUser);
  		setReceiver(receiver);
  		// Start the Receiver Thread
  		getReceiver().start();
	}

	/**
	 * Make Actions on click button
	 * @param e A ActionEvent
	 * @exception UnknownHostException
	 * @exception  SocketException
	 */
  	public void actionPerformed(ActionEvent e) {
  		// Check if the send Button was pressed
  		if(e.getSource() == sendButton) {
  			
  			try {
  				// Create a new SenderThread
  				SenderThread sender = new SenderThread(InetAddress.getByName(currentRemoteUserIpAddress), 7777);
  				// Start the SenderThread
  				sender.start();
  	  			sender.setLocalMessage(sendTextArea.getText());
  	  			// Stop the SenderThread
  	  			sender.interrupt();
  				
			} catch (UnknownHostException | SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  		}
  		// Check if the reload Button was pressed
  		else if(e.getSource() == reloadDataButton) {
  			// Update the Connected User List if some users have disconnected themselves
  			for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
  				for(int j = 0; j < ByeThread.getUsersList().size(); j++) {
  					if(HelloThread.getUsersList().get(i).isEqual(ByeThread.getUsersList().get(j))) {
  						HelloThread.getUsersList().remove(i);
  					}
  				}
  			}
  			// Reset the disconnected user list
  			ByeThread.setUsersList(new ArrayList<User>());
  			// Create a new UserListView Window
  			UserList userListView = new UserList(HelloThread.getUsersList());
  			userListView.initComponents();
  			userListView.pack();
  			userListView.setVisible(true);
  		}
  	}

	/**
	 * Initialize all the components
	 * @exception IOException
	 */
  	public void initComponents() {
	    sendButton = new JButton("Send");
	    sendButton.addActionListener(this);
	    
	    reloadDataButton = new JButton("Reload");
	    reloadDataButton.addActionListener(this);
	

	    JPanel panel = new JPanel();
	
	    receiveLabel = new JLabel("Receive Message");
	    sendLabel = new JLabel("Message to send");
	
	    sendTextArea = new JTextArea(5, 20);
	    receiveTextArea = new JTextArea(5, 20);
	    	
	    panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	
	    panel.setLayout(new GridLayout(3, 2, 10, 10));
	  
	    panel.add(sendLabel, BorderLayout.CENTER);
	    panel.add(sendTextArea, BorderLayout.CENTER);
	    panel.add(sendButton, BorderLayout.CENTER);
	    panel.add(reloadDataButton, BorderLayout.CENTER);
	    panel.add(receiveLabel, BorderLayout.CENTER);
	    panel.add(receiveTextArea, BorderLayout.CENTER);
	    
	    this.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	    	/**
	    	 * On closing Window, we send a Bye Request to all the users
	    	 * @exception IOException
	    	 */
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		try {
					getLocalUser().bye();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.add(panel, BorderLayout. NORTH);
	    
	    
  	}

	/** Gets the Local User Object.
	 * @return An User Object representing the Local User
	*/
	public User getLocalUser() {
		return localUser;
	}

	/** Sets the Local User Object
	 * @param localUser A User Object containing the local User
	*/
	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}

	/** Gets the ReceiverThread.
	 * @return An ReceiverThread Object representing the current ReceiverThread
	*/
	public ReceiverThread getReceiver() {
		return receiver;
	}

	/** Sets the ReceiverThread Object
	 * @param receiver A ReceiverThread Object containing the ReceiverThread
	*/
	public void setReceiver(ReceiverThread receiver) {
		this.receiver = receiver;
	}

	/** Gets the Current Remote User IP Address
	 * @return An String Object representing the IP Address of the current Remote User
	*/
	public static String getCurrentRemoteUserIpAddress() {
		return currentRemoteUserIpAddress;
	}

	/** Sets the IP Address of the current Remote User
	 * @param currentRemoteUserIpAddress A String Object containing the IP Address of the current Remote User
	*/
	public static void setCurrentRemoteUserIpAddress(String currentRemoteUserIpAddress) {
		ChatSystem.currentRemoteUserIpAddress = currentRemoteUserIpAddress;
	}
	
	/** Update the received messages in the ChatSystem View
	 * @param newMessage A String Object containing new Message
	 * @exception Exception
	*/
	public static void updateReceivedMessage(String newMessage) {
		if(!HelloThread.getUsersList().isEmpty()) {
			for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
				// Get the sender of the message
				if(HelloThread.getUsersList().get(i).getIpAddress().equals(getCurrentRemoteUserIpAddress())) {
					try {
						// Update the ChatSystem View
						receiveTextArea.append("FROM [" + HelloThread.getUsersList().get(i).getNickName() + "] : " + newMessage + "\n");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

		}

	}
}