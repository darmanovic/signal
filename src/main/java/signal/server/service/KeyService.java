package signal.server.service;

import java.util.Base64;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.logging.Logger;
import org.whispersystems.libsignal.SignalProtocolAddress;

import signal.entities.IdentityKeyModel;
import signal.entities.PreKeyModel;
import signal.entities.SignedPreKeyModel;
import signal.model.FirstMessageKeyBundle;
import signal.model.RegisterUserKeyBundle;
import signal.server.exception.UserExistsException;


/**
 * @author Radoje
 * Klasa koja upisuje/cita kljuceve iz baze.
 */
@Stateless
public class KeyService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger logger;

	/**
	 * @author Radoje
	 *	Metod upisuje poslati set kljuceva u bazu
	 */
	public void saveKeyBundle(RegisterUserKeyBundle userKeys) throws UserExistsException {
		try {
			em.createQuery("SELECT i FROM IdentityKeyModel i WHERE i.userId = :userId", IdentityKeyModel.class).setParameter("userId", userKeys.getUserId())
					.getSingleResult();
			throw new UserExistsException(String.format("Greska prilikom registracije! Korisnik %s vec postoji u bazi!", userKeys.getUserId()));
		} catch (NoResultException e) {
			logger.infof("Korisnik %s nije prethodno postojao u bazi! Dodajem ga.", userKeys.getUserId());
		}
		byte[] identityKeyPublic = Base64.getDecoder().decode(userKeys.getIdentityKey());
		IdentityKeyModel identityKey = new IdentityKeyModel(userKeys.getUserId(), userKeys.getRegistrationId(), userKeys.getDeviceId(), identityKeyPublic);
		em.merge(identityKey);
		byte[] signedPreKeyPublic = Base64.getDecoder().decode(userKeys.getSignedPreKey());
		byte[] signedPreKeySignature = Base64.getDecoder().decode(userKeys.getSignedPreKeySignature());
		SignedPreKeyModel signedPreKey = new SignedPreKeyModel(userKeys.getUserId(), userKeys.getSignedPreKeyId(), signedPreKeyPublic,
				signedPreKeySignature);
		em.merge(signedPreKey);

		userKeys.getPreKeys().forEach((preKeyId, preKeyString) -> {
			byte[] preKeyPublic = Base64.getDecoder().decode(preKeyString);
			PreKeyModel preKey = new PreKeyModel(userKeys.getUserId(), preKeyId, preKeyPublic);
			em.merge(preKey);
		});

	}

	/**
	 * @author Radoje
	 * Metod cita set kljuceva za korisnika iz baze na osnovu userId
	 */
	public FirstMessageKeyBundle getUserKeyBundle(String userId) {
		IdentityKeyModel identityKeyModel = em.createQuery("SELECT i FROM IdentityKeyModel i WHERE i.userId = :userId", IdentityKeyModel.class)
				.setParameter("userId", userId).getSingleResult();
		SignedPreKeyModel signedPreKeyModel = em.createQuery("SELECT s FROM SignedPreKeyModel s WHERE s.userId = :userId", SignedPreKeyModel.class)
				.setParameter("userId", userId).getSingleResult();
		List<PreKeyModel> preKeyFullList = em.createQuery("SELECT p FROM PreKeyModel p WHERE p.userId = :userId", PreKeyModel.class)
				.setParameter("userId", userId).getResultList();

		String identityKey = KeyService.encode(identityKeyModel.getIdentityKeyPublic());
		int registrationId = identityKeyModel.getRegistrationId();
		int deviceId = identityKeyModel.getDeviceId();

		String signedPreKey = KeyService.encode(signedPreKeyModel.getSignedPreKeyPublic());
		String signedPreKeySignature = KeyService.encode(signedPreKeyModel.getSignedPreKeySignature());
		int signedPreKeyId = signedPreKeyModel.getSignedPreKeyId();
		String preKey = null;
		int preKeyId = 0;

		if (preKeyFullList != null && !preKeyFullList.isEmpty()) {
			preKey = KeyService.encode(preKeyFullList.get(0).getPreKeyPublic());
			preKeyId = preKeyFullList.get(0).getPreKeyId();
		}
		
		return new FirstMessageKeyBundle(userId, registrationId, deviceId, identityKey, signedPreKeyId, signedPreKey, signedPreKeySignature, preKeyId, preKey);
	}

	/**
	 * @author Radoje
	 * Pomocni metod, pretvara niz bajtova u base64 string.
	 */
	public static String encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static byte[] decode(String str) {
		return Base64.getDecoder().decode(str);
	}
}
