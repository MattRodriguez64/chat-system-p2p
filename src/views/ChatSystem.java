package views;

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

  /**
	 * 
	 */
	private User localUser;
	private ReceiverThread receiver;
	private static final long serialVersionUID = 1L;
	private JButton sendButton;
	private JButton reloadDataButton;
	private JLabel receiveLabel;
	private JLabel sendLabel;
	private JTextArea sendTextArea;
  	private static JTextArea receiveTextArea;
  	private static String currentRemoteUserIpAddress;
  	

	  /** Creates a new instance of Fenetre 
	 * @throws IOException */
  	public ChatSystem(User localUser, ReceiverThread receiver) throws IOException {
  		setLocalUser(localUser);

  		setReceiver(receiver);
  		getReceiver().start();
  		
	}

  	public void actionPerformed(ActionEvent e) {
  		if(e.getSource() == sendButton) {
  			
  			try {
  				SenderThread sender = new SenderThread(InetAddress.getByName(currentRemoteUserIpAddress), 7777);
  				sender.start();
  	  			sender.setLocalMessage(sendTextArea.getText());
  	  			sender.interrupt();
  				
			} catch (UnknownHostException | SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  		}
  		else if(e.getSource() == reloadDataButton) {
  			//System.out.println("bye" + ByeThread.getUsersList().size());
  			//System.out.println("HELLO" + HelloThread.getUsersList().size());
  			for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
  				for(int j = 0; j < ByeThread.getUsersList().size(); j++) {
  					if(HelloThread.getUsersList().get(i).isEqual(ByeThread.getUsersList().get(j))) {
  						HelloThread.getUsersList().remove(i);
  					}
  				}
  			}
  			//System.out.println("HELLO2" + HelloThread.getUsersList().size());
  			ByeThread.setUsersList(new ArrayList<User>());
  			UserList userListView = new UserList(HelloThread.getUsersList());
  			userListView.initComponents();
  			userListView.pack();
  			userListView.setVisible(true);
  		}
  	}

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
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("BYE");
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

	public User getLocalUser() {
		return localUser;
	}

	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}

	public ReceiverThread getReceiver() {
		return receiver;
	}

	public void setReceiver(ReceiverThread receiver) {
		this.receiver = receiver;
	}

	public static String getCurrentRemoteUserIpAddress() {
		return currentRemoteUserIpAddress;
	}

	public static void setCurrentRemoteUserIpAddress(String currentRemoteUserIpAddress) {
		ChatSystem.currentRemoteUserIpAddress = currentRemoteUserIpAddress;
	}
	
	public static void updateReceivedMessage(String newMessage) {
		if(!HelloThread.getUsersList().isEmpty()) {
			for(int i = 0; i < HelloThread.getUsersList().size(); i++) {
				if(HelloThread.getUsersList().get(i).getIpAddress().equals(getCurrentRemoteUserIpAddress())) {
					try {
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
	
	/*protected void finalize() throws Throwable{
		System.out.println("BYE");
		getLocalUser().bye();
	}*/

}