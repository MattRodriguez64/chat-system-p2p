package views;

import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;
//import java.io.BufferedReader;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Login extends JFrame implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton connectButton;
	private JLabel nickNameLabel;
  	private JTextArea nickNameTextArea;
  	private final int helloport = 7778;
  	private final String helloBroadcast = "192.168.1.255";
  	private final int clientport = 7777;


	  /** Creates a new instance of Fenetre 
	 * @throws IOException */
  	public Login(){

	}

  	public void actionPerformed(ActionEvent e) {
  		if(e.getSource() == connectButton){
  			User localUser;
			try {
				localUser = new User(nickNameTextArea.getText());
				//System.out.println(localUser.toString());
				InetAddress ia = InetAddress.getByName(helloBroadcast);
				localUser.hello(ia, helloport);

		        ReceiverThread receiver = new ReceiverThread(new DatagramSocket(clientport));
				
				ChatSystem chatSystem = new ChatSystem(localUser, receiver);
				chatSystem.initComponents();
				chatSystem.pack();
				chatSystem.setVisible(true);
				this.dispose();
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			

  		}
  	}

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