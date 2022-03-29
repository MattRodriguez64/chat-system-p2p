package views;

import main.*;

import javax.swing.*;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;

import java.io.IOException;
import java.util.ArrayList;

public class UserList extends JFrame implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[] usersBtn;
	private ArrayList<User> users;



	  /** Creates a new instance of Fenetre 
	 * @throws IOException */
  	public UserList(ArrayList<User> users){
  		setUsers(users);
	}

  	public void actionPerformed(ActionEvent e) {

  	}

  	public void initComponents() {
	    
  		usersBtn = new JButton[getUsers().size()];
  		//System.out.println(getUsers().size());
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	    panel.setLayout(new GridLayout(getUsers().size(), 1, 10, 10));
	    
	    for(int i = 0; i < getUsers().size(); i++) {
	    	usersBtn[i] = new JButton(getUsers().get(i).getNickName());
	    	usersBtn[i].setActionCommand(getUsers().get(i).getIpAddress());
	    	usersBtn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String pressedBtn = e.getActionCommand();
					ChatSystem.setCurrentRemoteUserIpAddress(pressedBtn);
					JOptionPane.showMessageDialog(null, "User Pressed : " + pressedBtn);
					close();
				}
	    		
	    	});
	    	panel.add(usersBtn[i]);
	    }
	    this.add(panel, BorderLayout. NORTH);
  	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	public void close() {
		this.dispose();
	}


}
