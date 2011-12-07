package models;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import play.libs.*;
import play.libs.F.*;

public class Chat {
    
    // ~~~~~~~~~ Let's chat! 
    
    final ArchivedEventStream<Chat.Event> chatEvents = new ArchivedEventStream<Chat.Event>(100);
    
    /**
     * Called when a user suscribes to the chat instance.
     * @return the continuous event stream to be used by WebSocket implementation.
     */
    public EventStream<Chat.Event> subscribe(String userLogin) {
        chatEvents.publish(new SubscribeEvent(userLogin));
        return chatEvents.eventStream();
    }
    
    /**
     * Called when a user unsuscribes from the chat instance (either leaves the page or disconnect).
     */
    public void unsubscribe(String userLogin) {
        chatEvents.publish(new UnsubscribeEvent(userLogin));
    }
    
    /**
     * Called when a user says something in the chat instance.
     */
    public void write(String userLogin, String text) {
        if(StringUtils.isBlank(text)) {
            return;
        }
        chatEvents.publish(new MessageEvent(userLogin, text));
    }
    
    /**
     * Returns all the messages sent after the specified message id in the chat instance.
     * Used with long polling ajax implementation, as user is sometimes disconnected, it is necessary to give 
     * the last seen event id, to be sure not to miss any message.
     */
    public Promise<List<IndexedEvent<Chat.Event>>> nextMessages(long lastReceivedMsgId) {
        return chatEvents.nextEvents(lastReceivedMsgId);
    }
    
    
    // ~~~~~~~~~ Chat room events

    public static abstract class Event {
        
        final public String type;
        final public Long timestamp;
        
        public Event(String type) {
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
    }
    
    public static class SubscribeEvent extends Event {
        
        final public String userLogin;
        
        public SubscribeEvent(String userLogin) {
            super("subscribe");
            this.userLogin = userLogin;
        }
        
    }
    
    public static class UnsubscribeEvent extends Event {
        
        final public String userLogin;
        
        public UnsubscribeEvent(String userLogin) {
            super("unsubscribe");
            this.userLogin = userLogin;
        }
        
    }
    
    public static class MessageEvent extends Event {
        
        final public String userLogin;
        final public String text;
        
        public MessageEvent(String userLogin, String text) {
            super("message");
            this.userLogin = userLogin;
            this.text = text;
        }
        
    }
    
    // ~~~~~~~~~ Chat room factory

    static Map<Long, Chat> instances = new HashMap<Long, Chat>();
    public static Chat get(Long roomId) {
        Chat instance;
        
    	if(instances.containsKey(roomId)) {
    		instance = instances.get(roomId);
        } else {
        	instance = new Chat();
        	instances.put(roomId, instance);
        }
        return instance;
    }
}
