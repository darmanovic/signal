package signal.server.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

import signal.entities.UserAddress;
import signal.model.RestResponse;
import signal.rest.AddressService;

/**
 * @author Radoje
 * REST za rad sa  adresama klijenata
 */ 
@Stateless
public class AddressServiceImpl implements AddressService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger logger;

	/**
	 * @author Radoje
	 * REST endpoint koja vraca punu adresu korisnika za dati korisnicki id
	 */
	@Override
	public Response getAddress(String userId) {
		UserAddress existingAddress = this.getUserAddress(userId);
		if (existingAddress == null) {
			return Response.status(500).build();
		}
		return Response.ok(new Gson().toJson(existingAddress)).build();
	}

	/**
	 * @author Radoje
	 * Rest endpoint na kojem korisnici registruju svoju adresu na serveru
	 */
	@Override
	public Response setAddress(String addressJson) {
		UserAddress tempAddress = new Gson().fromJson(addressJson, UserAddress.class);
		UserAddress existingAddress = this.getUserAddress(tempAddress.getUserId());
		if (existingAddress != null) {
			return Response.status(503).entity(new RestResponse(false, "Vec postoji korisnik sa userId " + tempAddress.getUserId())).build();
		}
		UserAddress newAddress = em.merge(tempAddress);
		return Response.ok(newAddress).build();
	}

	private UserAddress getUserAddress(String userId) {
		try {
			return em.createQuery("SELECT u FROM UserAddress u WHERE u.userId = :userId", UserAddress.class).setParameter("userId", userId).getSingleResult();
		} catch (NoResultException e) {
			logger.warnf("U bazi nije pronadjena adresa korisnika sa userId %s", userId);
			return null;
		}
	}

}
