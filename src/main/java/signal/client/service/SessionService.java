package signal.client.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.logging.Logger;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;

import signal.client.entities.SessionEntity;

/**
 * @author goran
 * Klasa koja ucitava/upisuje sesiju u bazu
 */
public class SessionService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger logger;

	public void storeSession(SessionEntity session) {
		em.merge(session);
	}

	/**
	 * @author goran
	 * Metod za skaldistenje sesije u bazi
	 */
	public void storeSession(SignalProtocolAddress address, SessionRecord record) {
		SessionEntity sessionEntity = null;
		try {
			sessionEntity = em
					.createQuery("SELECT s FROM SessionEntity s WHERE s.active = TRUE AND s.name = :name AND s.deviceId = :deviceId", SessionEntity.class)
					.setParameter("name", address.getName()).setParameter("deviceId", address.getDeviceId()).getSingleResult();
			sessionEntity.setSession(record.serialize());
		} catch (NoResultException e) {
			logger.info("Sesija se upisuje po prvi put!");
			sessionEntity = new SessionEntity(address, record.serialize());
		}
		em.merge(sessionEntity);
	}

	/**
	 * @author goran
	 * Metod za iscitavanje sesije iz baze
	 */
	public SessionEntity getSession(SignalProtocolAddress address) {
		List<SessionEntity> sessions = em
				.createQuery("SELECT s FROM SessionEntity s WHERE s.active = TRUE and s.name = :name AND s.deviceId = :deviceId ORDER BY s.id DESC",
						SessionEntity.class)
				.setParameter("name", address.getName()).setParameter("deviceId", address.getDeviceId()).getResultList();
		if (sessions.isEmpty()) {
			logger.error("Ne postoji trazena sesije u bazi!");
			return null;
		}
		return sessions.get(0);
	}

}
