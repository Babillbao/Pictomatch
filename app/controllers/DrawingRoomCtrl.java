package controllers;

import models.DrawingRoom;
import models.User;



public class DrawingRoomCtrl extends LoggedApplication {

	public static void join(Long id) {
		User connectedUser = getConnectedUser();
        DrawingRoom selectedRoom = DrawingRoom.get(id);
        
        connectedUser.join(selectedRoom);
        refreshConnectedUser(connectedUser);
        
        showUserCurrentRoom();
    }
	
	public static void showUserCurrentRoom() {
		User connectedUser = getConnectedUser();
		DrawingRoom selectedRoom = DrawingRoom.get(connectedUser.currentRoom.id);
		
		render(connectedUser, selectedRoom);
	}
}
