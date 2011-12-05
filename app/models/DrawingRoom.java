package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;

import play.data.validation.Required;
import play.data.validation.Unique;

@Entity
public class DrawingRoom extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Required
	@Unique
	public String name;

	@Required
	public int nbMaxUser;
	
	@OneToMany(mappedBy="currentRoom", cascade=CascadeType.ALL)
	public List<User> users = new ArrayList();
	
	public DrawingRoom() {
		this("Test", 10);
	}
	
	public DrawingRoom(String name, int nbMaxUser) {
		this.name = name;
		this.nbMaxUser = nbMaxUser;
	}
	
	@Override
	public String toString() {
		return name + " (" + nbMaxUser + ")";
	}
}
