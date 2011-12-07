package controllers;

import java.util.List;

import models.Chat;
import models.Chat.Event;
import play.libs.F.IndexedEvent;
import play.mvc.Controller;

import com.google.gson.reflect.TypeToken;

/**
 * Controller used to manage chat for IE users (non-websocket compliant).
 * Originally based on PlayFramework sample chat.
 * 
 * @author baptiste.oudin
 */
public class AjaxChatCtrl extends Controller {
	
	public static void subscribeAjax(Long roomId, String userLogin) {
		Chat.get(roomId).subscribe(userLogin);
	}
	
    public static void write(Long roomId, String userLogin, String message) {
        Chat.get(roomId).write(userLogin, message);
    }
    
    public static void listen(Long roomId, Long lastReceivedMsgId) {        
        // Here we use continuation to suspend 
        // the execution until a new message has been received
        List messages = await(Chat.get(roomId).nextMessages(lastReceivedMsgId));
        renderJSON(messages, new TypeToken<List<IndexedEvent<Chat.Event>>>() {}.getType());
    }
    
    public static void unsubscribe(Long roomId, String userLogin) {
        Chat.get(roomId).unsubscribe(userLogin);
        Application.index();
    }
}