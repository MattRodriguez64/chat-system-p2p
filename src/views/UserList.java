package views;
/**
 * Display all the users connected
 * Class : UserList
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import main.*;

import javax.swing.*;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;

import java.util.ArrayList;

public class UserList extends JFrame implements ActionListener {

	// CONSTANTS
	private static final long serialVersionUID = 1L;
	// ATTRIBUTES
	private JButton[] usersBtn;
	private ArrayList<User> users;



    // CONSTRUCTOR
	/** Creates a new instance of UserList 
	 * @param users An List<User> containing all the connected users
	*/
  	public UserList(ArrayList<User> users){
  		setUsers(users);
	}

	/**
	 * Make Actions on click button
	 * @param e A ActionEvent
	 */
  	public void actionPerformed(ActionEvent e) {

  	}

	/**
	 * Initialize all the components
	 */
  	public void initComponents() {
	    
  		usersBtn = new JButton[getUsers().size()];
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
	    panel.setLayout(new GridLayout(getUsers().size(), 1, 10, 10));
	    
	    // Create a button for each user connected
	    for(int i = 0; i < getUsers().size(); i++) {
	    	usersBtn[i] = new JButton(getUsers().get(i).getNickName());
	    	usersBtn[i].setActionCommand(getUsers().get(i).getIpAddress());
	    	usersBtn[i].addActionListener(new ActionListener() {
				@Override
				/**
				 * Make Actions on click button
				 * @param e A ActionEvent
				 */
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

	/** Gets the all Users Object.
	 * @return An List<User> Object representing all connected users
	*/
	public ArrayList<User> getUsers() {
		return users;
	}

	/** Sets all Users Object.
	 * @param users A List<User> Object containing all connected users
	*/
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	/** Close the current instance of this User List Window
	*/
	public void close() {
		this.dispose();
	}


}
