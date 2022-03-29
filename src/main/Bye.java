package main;

import java.io.Serializable;

public class Bye implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8605376919550527202L;
	public static int byePort = 7779;
	private User user;
	
	public Bye(User user) {
		setUser(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}