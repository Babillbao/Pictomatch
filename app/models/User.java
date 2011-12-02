package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;

@Entity
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Required
	public String login;

	@Required
	public String password;
	
	@Email
	public String email;
	
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
