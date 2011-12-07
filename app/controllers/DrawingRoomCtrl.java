package controllers;

import play.cache.Cache;

import models.Chat;
import models.DrawingRoom;
import models.User;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import play.mvc.Http.*;

import static play.libs.F.*;
import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.*;

import java.util.*;



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
