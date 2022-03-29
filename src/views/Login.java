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

public class Login extends JFrame implements ActionListener {
	
	// CONSTANTS
	private static final long serialVersionUID = 1L;
  	private final int helloport = 7778;
  	private final String helloBroadcast = "192.168.1.255";
  	private final int clientport = 7777;
  	
	// ATTRIBUTES
	private JButton connectButton;
	private JLabel nickNameLabel;
  	private JTextArea nickNameTextArea;

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
				// Prepare and send an Hello Request
				InetAddress ia = InetAddress.getByName(helloBroadcast);
				localUser.hello(ia, helloport);
				
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

}