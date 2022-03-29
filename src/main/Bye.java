package main;
/**
 * Represents a Bye Request
 * Class : Bye
 * @version: V1
 * @author: RODRIGUEZ Matt
 * @date: 29/03/2022
 **/
import java.io.Serializable;

public class Bye implements Serializable{
	
	// CONSTANTS
	private static final long serialVersionUID = -8605376919550527202L;
	/** By agreement, all the Bye Request will be sent and received from port 7779
	 */
	public static int byePort = 7779;
	
	// ATTRIBUTES
	/** Represents the User Object.
	*/
	private User user;
	
	// CONSTRUCTOR
	/** Creates a Bye Object with the specified User.
	 * @param user The User.
	*/
	public Bye(User user) {
		setUser(user);
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
	
	//STANDARD METHODS 	
	/** Display the current Bye Object.
	 * @return A String containing the user value
	*/
	public String toString() {
		return getUser().toString();
	}
	
	//NO SPECIFIC METHODS
}
