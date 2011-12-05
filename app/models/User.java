package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;

@Entity
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;
	public static final String CACHE_KEY = "_user";

	@Required
	@Unique
	public String login;

	@Required
	@Password
	public String password;
	
	@Required
	@Email
	@Unique
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
