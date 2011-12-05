package controllers;

import play.cache.Cache;
import models.DrawingRoom;
import models.User;

public class DrawingRoomCtrl extends LoggedApplication {

	public static void join(Long id) {
		User connectedUser = getConnectedUser();
        DrawingRoom selectedRoom = DrawingRoom.findById(id);
        
        connectedUser.join(selectedRoom);
        refreshConnectedUser(connectedUser);
        
        showUserCurrentRoom();
    }
	
	public static void showUserCurrentRoom() {
		User connectedUser = getConnectedUser();
		DrawingRoom selectedRoom = DrawingRoom.findById(connectedUser.currentRoom.id);
		
		render(connectedUser, selectedRoom);
	}
}
