package main;
/**
 * Represents an Hello Request
 * Class : Hello
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.Serializable;

public class Hello implements Serializable{

	// CONSTANTS
	private static final long serialVersionUID = -8025178853746544446L;
	
	// ATTRIBUTES
	/** Represents the User Object.
	*/
	private User user;
	
	/** Indicates if an answer is needed or not
	 * true = yes
	 * false = no
	*/
	private boolean answer;
	
	// CONSTRUCTOR
	/** Creates a Hello Object with the specified User and the answer.
	 * @param user The User.
	 * @param answer true if an answer is needed, else false.
	*/
	public Hello(User user, boolean answer) {
		setUser(user);
		setAnswer(answer);
	}
	
	/** Gets the User Object.
	 * @return An User Object representing the User
	*/
	public User getUser() {
		return user;
	}

	/** Sets the User Object
	 * @param user A User Object containing the user
	*/
	public void setUser(User user) {
		this.user = user;
	}

	/** Gets the Answer.
	 * @return A Boolean representing the answer
	*/
	public boolean isAnswer() {
		return answer;
	}

	/** Sets the Answer
	 * @param answer A boolean containing the answer
	*/
	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
	
	//STANDARD METHODS 	
	/** Display the current Hello Object.
	 * @return A String containing the user value and answer value
	*/
	public String toString() {
		return getUser().toString() + " - Answer : " + isAnswer();
	}
	
	//NO SPECIFIC METHODS
}
