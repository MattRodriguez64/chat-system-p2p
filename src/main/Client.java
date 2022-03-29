package main;

/**
 * Represents the entry point of the P2P Chat System
 * Class : Client
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/

import views.Login;

public class Client{
	   /**
	   * This is the main method which is the starting point of the P2P Chat System.
	   * @param args Unused.
	   * @return Nothing.
	   */
	public static void main(String args[]){ 
		// Create a new Login Window
        Login loginWindow = new Login();
        loginWindow.initComponents();
        loginWindow.pack();
        loginWindow.setVisible(true);
    }
}      