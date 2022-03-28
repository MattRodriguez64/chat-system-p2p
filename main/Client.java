package main;



import views.Login;

public class Client{

	public static void main(String args[]) throws Exception {  
      
        Login loginWindow = new Login();
        loginWindow.initComponents();
        loginWindow.pack();
        loginWindow.setVisible(true);
    }
}      