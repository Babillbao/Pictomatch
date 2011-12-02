package models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import play.db.jpa.Model;

/**
 * Base class for all object model.
 * Implements basic methods such as "toString", "Equals", "hashCode"
 */
public class BaseModel extends Model {

	private static final long serialVersionUID = 1L;

	/**
	 * Use apache commons to generated toString by reflection.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * Use apache commons to generated equals by reflection.
	 */
	@Override
	public boolean equals(final Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	
//	/**
//	 * Use apache commons to generated hashCode by reflection.
//	 */
//	@Override
//	public int hashCode() {
//		return HashCodeBuilder.reflectionHashCode(this);
//	}
}
