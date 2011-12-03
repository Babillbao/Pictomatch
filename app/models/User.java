package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Email;
import play.data.validation.Required;

@Entity
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;
	public static final String CACHE_KEY = "_user";

	@Required
	public String login;

	@Required
	public String password;
	
	@Email
	public String email;
	
	@ManyToOne
	public DrawingRoom currentRoom;
	
	public boolean isAdmin;
	
	public User(String login, String password, boolean isAdmin) {
		this.login = login;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	

	@Override
	public String toString() {
		return login;
	}
}
