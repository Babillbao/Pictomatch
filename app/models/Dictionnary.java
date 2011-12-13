package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.data.validation.Unique;

@Entity
public class Dictionnary extends BaseModel {


	@Required
	@Unique
	public String name;
	
	@Required
	public Level level;

	@Required
	public String language;
	
	@OneToMany(mappedBy="dictionnary", cascade=CascadeType.ALL)
	public List<Word> words = new ArrayList<Word>();
	

	@Override
	public String toString() {
		return name + " - " + level;
	}
	
	
	
	
	
	
	public enum Level {
		BEGINNER("lbl_a_dictionnary_level_beginner", 5, 1, 45),
		EASY("lbl_a_dictionnary_level_easy", 8, 3, 60),
		INTERMEDIATE("lbl_a_dictionnary_level_intermediate", 10, 5, 75),
		DIFFICULT("lbl_a_dictionnary_level_difficult", 15, 7, 85);
		
		public String key;
		public int foundReward;
		public int drawnReward;
		public int timeLapse;
		
		private Level(String key, int foundReward, int drawnReward, int timeLapse) {
			this.key = key;
			this.foundReward = foundReward;
			this.drawnReward = drawnReward;
			this.timeLapse = timeLapse;
		}
	}
}
