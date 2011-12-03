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
	 * Stores the connected user in the cache for 20mn.
	 */
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byLogin", Security.connected()).first();
			Cache.set(session.getId() + User.CACHE_KEY, user, "20mn");
		}
	}

	/**
	 * Retrieves the connected user.
	 * @return the connected user or null if it does not exist.
	 */
	static User getConnectedUser() {
		if (Security.isConnected()) {
			return (User) Cache.get(session.getId() + User.CACHE_KEY);
		}
		
		return null;
	}
}
