package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;

@Entity
public class Word extends BaseModel {

	@Required
	public String value;

	@Required
	@ManyToOne
	public Dictionnary dictionnary;
	
	


	@Override
	public String toString() {
		return value + " (" + dictionnary.name + " - " + dictionnary.level + ")";
	}
}
