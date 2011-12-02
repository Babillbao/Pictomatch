package models;

import javax.persistence.Entity;

import play.data.validation.Required;

@Entity
public class DrawingRoom extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Required
	public String name;

	@Required
	public int nbMaxUser;
	
	public DrawingRoom() {
		this("Test", 10);
	}
	
	public DrawingRoom(String name, int nbMaxUser) {
		this.name = name;
		this.nbMaxUser = nbMaxUser;
	}
}
