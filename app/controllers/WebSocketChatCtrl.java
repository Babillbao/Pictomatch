package controllers;

import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.Equals;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;
import models.Chat;
import models.DrawingRoom;
import play.libs.F.Either;
import play.libs.F.EventStream;
import play.libs.F.Promise;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

public class WebSocketChatCtrl extends WebSocketController {
	
	public static void subscribeSocket(Long roomId, String userLogin) {
        Chat chat = Chat.get(roomId);
        DrawingRoom room = DrawingRoom.get(roomId);
        
        // Socket connected, join the chat room
        EventStream<Chat.Event> roomMessagesStream = chat.subscribe(userLogin);
     
        // Loop while the socket is open
        while(inbound.isOpen()) {
            
            // Wait for an event (either something coming on the inbound socket channel, or Chat messages)
            Either<WebSocketEvent,Chat.Event> e = await(Promise.waitEither(
                inbound.nextEvent(), 
                roomMessagesStream.nextEvent()
            ));
            
            // Case: User typed 'quit'
            for(String userMessage: TextFrame.and(Equals("quit")).match(e._1)) {
            	chat.unsubscribe(userLogin);
                outbound.send("quit:ok");
                disconnect();
            }
            
            // Case: New message in the chat instance (impl 1)
            for(String userMessage: TextFrame.match(e._1)) {
            	if(!room.winnerLogins.contains(userLogin))
        		{
        			boolean isAnswer = /*(compteur.estEnPause) ? false : */room.dictionnary.isAnswer(userMessage);
        			if(!isAnswer) {
        				chat.write(userLogin, userMessage);
        			} else {
        				chat.findAnswer(userLogin, (room.dictionnary.level.foundReward - room.winnerLogins.size()));
        				room.winnerLogins.add(userLogin);
        			}
        		}
            }
            
            // Case: Someone subscribed to the chat instance
            for(Chat.SubscribeEvent event: ClassOf(Chat.SubscribeEvent.class).match(e._2)) {
                outbound.send("subscribe:%s", event.userLogin);
            }
            
            // Case: Someone aded a new message
            for(Chat.MessageEvent event: ClassOf(Chat.MessageEvent.class).match(e._2)) {
        		outbound.send("message:%s:%s", event.userLogin, event.text);

            }
            
            // Case: Someone unsubscribed from the chat instance
            for(Chat.UnsubscribeEvent event: ClassOf(Chat.UnsubscribeEvent.class).match(e._2)) {
                outbound.send("unsubscribe:%s", event.userLogin);
            }
            
            // Case: Someone found the correct answer
            for(Chat.FoundAnswerEvent event: ClassOf(Chat.FoundAnswerEvent.class).match(e._2)) {
            	outbound.send("foundAnswer:%s", event.userLogin, event.reward);
            }
            
            // Case: The socket has been closed
            for(WebSocketClose closed: SocketClosed.match(e._1)) {
            	chat.unsubscribe(userLogin);
                disconnect();
            }
            
        }
	}
}