package signal.server.rest;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import signal.model.RegisterUserKeyBundle;
import signal.model.RestResponse;
import signal.rest.ExchangeKeysRest;
import signal.server.exception.UserExistsException;
import signal.server.service.KeyService;

/**
 * @author Radoje
 * Klasa zdje klijenti salju i zahtjevaju kljuceve sa servera
 */
public class ExchangeKeysRestImpl implements ExchangeKeysRest {

	@Inject
	private KeyService keyService;

	@Inject
	private Logger logger;
	
	private Gson Gson() {
		return new GsonBuilder().disableHtmlEscaping().create();
	}

	/**
	 * @author Radoje
	 * Metod koji vraca set kljuceva za datog korisnika
	 */
	public Response getKeys(@PathParam("userId") String userId) {
		logger.info("Pregledaju se kljucevi za " + userId);
		String json = Gson().toJson(keyService.getUserKeyBundle(userId));
		return Response.ok(json).build();
	}

	/**
	 * @author Radoje
	 * Metod koji postavlja kljuceve korisnika, enkodovane u base64 string!
	 */
	public Response setkeys(String userKeys) {
		logger.infof("Primljeni JSON je: %s", userKeys);
		try {
			RegisterUserKeyBundle keyBundle = Gson().fromJson(userKeys, RegisterUserKeyBundle.class);
			keyService.saveKeyBundle(keyBundle);
		} catch (JsonSyntaxException e) {
			logger.errorf("Greska prilikom deserilizacije JSON-a! %s", e.getMessage());
			Response.status(503).entity(new RestResponse(false, e.getMessage())).build();
		} catch (UserExistsException e) {
			logger.error("Greska prilikom postavljanja kljuceva!");
			logger.error(e.getMessage());
			Response.status(503).entity(new RestResponse(false, e.getMessage())).build();
		}
		return Response.ok(userKeys).build();
	}
}
