package main;

import java.io.Serializable;

public class Hello implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8025178853746544446L;
	private User user;
	private boolean answer;
	
	public Hello(User user, boolean answer) {
		setUser(user);
		setAnswer(answer);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

}
