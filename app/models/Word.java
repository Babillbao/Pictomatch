package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Word == false) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		Word w = (Word) obj;
		return new EqualsBuilder()
						.appendSuper(super.equals(obj))
						.append(value, w.value)
						.isEquals();
	}
	

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 39)
						.append(value)
						.toHashCode();
	}
}
