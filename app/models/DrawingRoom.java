package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.data.validation.Unique;

@Entity
public class DrawingRoom extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private static final int ROUND_NB = 20;
	private static final int PAUSE_TIMELAPS = 10;

	@Required
	@Unique
	public String name;

	@Required
	public int nbMaxUser;
	
	@OneToMany(mappedBy="currentRoom", cascade=CascadeType.ALL)
	public List<User> users = new ArrayList<User>();
	
	@Required
	@ManyToOne
	public Dictionnary dictionnary;
	
	/*public int currentRound;
	public List<User> winners = new ArrayList<User>();*/
	
	
	public DrawingRoom() {
		this("Test", 10);
	}
	
	public DrawingRoom(String name, int nbMaxUser) {
		this.name = name;
		this.nbMaxUser = nbMaxUser;
		//this.currentRound = 1;
	}
	
	@Override
	public String toString() {
		return name + " (" + nbMaxUser + ")";
	}
	

    // ~~~~~~~~~ Chat room factory

    static Map<Long, DrawingRoom> instances = new HashMap<Long, DrawingRoom>();
    public static DrawingRoom get(Long roomId) {
    	DrawingRoom instance;
        
    	if(instances.containsKey(roomId)) {
    		instance = instances.get(roomId);
        } else {
        	instance = DrawingRoom.findById(roomId);
        	instances.put(roomId, instance);
        }
        return instance.merge();
    }
}
