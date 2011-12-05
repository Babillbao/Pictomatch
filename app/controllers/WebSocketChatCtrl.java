package controllers;

import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.Equals;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;
import models.Chat;
import models.Chat.Event;
import play.libs.F.Either;
import play.libs.F.EventStream;
import play.libs.F.Promise;
import play.mvc.WebSocketController;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;

public class WebSocketChatCtrl extends WebSocketController {
	
	public static void subscribeSocket(Long roomId, String userLogin) {
        Chat room = Chat.get(roomId);
        
        // Socket connected, join the chat room
        EventStream<Chat.Event> roomMessagesStream = room.subscribe(userLogin);
     
        // Loop while the socket is open
        while(inbound.isOpen()) {
            
            // Wait for an event (either something coming on the inbound socket channel, or Chat messages)
            Either<WebSocketEvent,Chat.Event> e = await(Promise.waitEither(
                inbound.nextEvent(), 
                roomMessagesStream.nextEvent()
            ));
            
            // Case: User typed 'quit'
            for(String userMessage: TextFrame.and(Equals("quit")).match(e._1)) {
                room.unsubscribe(userLogin);
                outbound.send("quit:ok");
                disconnect();
            }
            
            // Case: New message in the chat instance (impl 1)
            for(String userMessage: TextFrame.match(e._1)) {
                room.write(userLogin, userMessage);
            }
            
            // Case: Someone subscribed to the chat instance
            for(Chat.SubscribeEvent event: ClassOf(Chat.SubscribeEvent.class).match(e._2)) {
                outbound.send("subscribe:%s", event.userLogin);
            }
            
            // Case: New message in the chat instance (impl 2)
            for(Chat.MessageEvent event: ClassOf(Chat.MessageEvent.class).match(e._2)) {
                outbound.send("message:%s:%s", event.userLogin, event.text);
            }
            
            // Case: Someone unsubscribed from the chat instance
            for(Chat.UnsubscribeEvent event: ClassOf(Chat.UnsubscribeEvent.class).match(e._2)) {
                outbound.send("unsubscribe:%s", event.userLogin);
            }
            
            // Case: The socket has been closed
            for(WebSocketClose closed: SocketClosed.match(e._1)) {
                room.unsubscribe(userLogin);
                disconnect();
            }
            
        }
	}
}