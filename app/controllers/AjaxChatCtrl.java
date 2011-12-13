package controllers;

import java.util.List;

import models.Chat;
import models.DrawingRoom;
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
    	DrawingRoom room = DrawingRoom.get(roomId);
    	
    	//if(!joueursGagnants.contains(pseudo))
		//{
			boolean isAnswer = /*(compteur.estEnPause) ? false : */room.dictionnary.isAnswer(message);
			if(!isAnswer)
				Chat.get(roomId).write(userLogin, message);
			else
			{
				Chat.get(roomId).write(userLogin, "youhouh !!");
				//chat.addMessage(pseudo, " a trouvé la solution !! (+ "+(dictionnaire.getPointsParMot()-joueursGagnants.size())+" points)", MessageChat.STYLE_GAGNE);
				/*int coeffPointsDessinateur = (joueursGagnants.size()*2 > 0) ? joueursGagnants.size()*2 : 1;
				listeUtilisateurs.addPointDessinTrouve(dictionnaire.getPointParDessinTrouve()/coeffPointsDessinateur);
				listeUtilisateurs.addPoint(pseudo, dictionnaire.getPointsParMot()-(this.joueursGagnants.size()));
				this.joueursGagnants.add(pseudo);

				if(this.joueursGagnants.size() == 3 || this.joueursGagnants.size() == this.listeUtilisateurs.size()-1)		
					setNouveauTour();*/
			}
		//}
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