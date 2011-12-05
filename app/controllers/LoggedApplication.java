package controllers;

import models.User;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Uses the Secure class to manage authentication.
 * It is related to the Secure module
 */
@With(Secure.class)
public class LoggedApplication extends Controller {

	/**
	 * Retrieves the connected user.
	 * @return the connected user or null if it does not exist.
	 */
	static User getConnectedUser() {
		if (Security.isConnected()) {
			User user = Cache.get(session.getId() + User.CACHE_KEY, User.class);
			if (user == null) {
				user = User.find("byLogin", Security.connected()).first();
				Cache.set(session.getId() + User.CACHE_KEY, user, "20mn");
			}
			
			if(user != null) {
				return user.merge();
			}
		}
		
		return null;
	}
	

	/**
	 * Refresh the connected user object in cache.
	 * @return the connected user or null if it does not exist.
	 */
	static void refreshConnectedUser(User connectedUser) {
		if (Security.isConnected()
				&& connectedUser != null
				&& Security.connected().equals(connectedUser.login)) {
				
			Cache.set(session.getId() + User.CACHE_KEY, connectedUser, "20mn");
		}
	}
}
