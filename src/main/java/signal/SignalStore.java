package signal;

import java.io.IOException;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import signal.client.entities.KeyPairEntity;
import signal.client.entities.SessionEntity;
import signal.client.service.ClientKeyService;
import signal.client.service.SessionService;
import signal.util.KeyType;

/*
 * Implementacija metoda za perzistenciju koji su potrebni signal-protocol biblioteci da bi funkcionisala. Koristi se kod bota.
 */
@Singleton
public class SignalStore implements SignalProtocolStore {

	@Inject
	private ClientKeyService keyService;
	@Inject
	private SessionService sessionService;
	@Inject
	private MyIdentityStore identityKeyStore;
	@Inject
	private Logger logger;

	
	@Override
	public int getLocalRegistrationId() {
		return identityKeyStore.getRegistrationID();
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		KeyPairEntity keyPair = keyService.getKeyPair(identityKeyStore.getMyAddress(), KeyType.IDENTITY);
		if (keyPair == null) {
			logger.warn("Ne postoji moj idenitetski kljuc!");
			return null;
		}
		try {
			return new IdentityKeyPair(keyPair.getKeyPair());
		} catch (InvalidKeyException e) {
			logger.errorf("Doslo je do problema prilikom kreiranaj identitetskog kljuca iz entiteta! %s", e.getMessage());
			return null;
		}
	}

	@Override
	public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
		IdentityKey existing = this.getIdentity(address);
		if (identityKey.equals(existing) == false) {
			keyService.merge(address, identityKey.serialize(), KeyType.IDENTITY);
			return true;
		}
		return false;
	}

	@Override
	public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
		IdentityKey trusted = this.getIdentity(address);
		return (trusted == null || trusted.equals(identityKey));
	}

	@Override
	public IdentityKey getIdentity(SignalProtocolAddress address) {
		KeyPairEntity keyPairEntity = keyService.getKeyPair(address, KeyType.IDENTITY);
		if(keyPairEntity == null) {
			logger.info("keyPairEntity je null!");
			return null;
		}
		try {
			//IdentityKey identityKeyPublic = new IdentityKeyPair(keyPairEntity.getKeyPair()).getPublicKey();
			IdentityKey identityKeyPublic = new IdentityKey(keyPairEntity.getKeyPair(), 0);
			return identityKeyPublic;
		} catch (InvalidKeyException e) {
			logger.errorf("Doslo je do greske prilikom dobavljanja identitetetskog kljuca!");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
		KeyPairEntity keyPair = keyService.getKeyPair(identityKeyStore.getMyAddress(), KeyType.PREKEY, preKeyId);
		if (keyPair == null) {
			logger.warn("Ne postoji predkljuc!");
			return null;
		}
		try {
			return new PreKeyRecord(keyPair.getKeyPair());
		} catch (IOException e) {
			logger.errorf("Greska prilikom kreiranja prekey-a! %s", e.getMessage());
			return null;
		}
	}

	@Override
	public void storePreKey(int preKeyId, PreKeyRecord record) {
		keyService.merge(identityKeyStore.getMyAddress(), record.serialize(), preKeyId, KeyType.PREKEY);
	}

	@Override
	public boolean containsPreKey(int preKeyId) {
		logger.warn("METOD containsPreKey je PRAZAN");
		return false;
		/*
		 * PRAZNO
		 */
	}

	@Override
	public void removePreKey(int preKeyId) {
		keyService.deactivateKeyPair(identityKeyStore.getMyAddress(), KeyType.PREKEY, preKeyId);
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
		KeyPairEntity keyPair = keyService.getKeyPair(identityKeyStore.getMyAddress(), KeyType.SIGNEDPREKEY, signedPreKeyId);
		if (keyPair == null) {
			logger.warnf("Ne postoji signedPreKeyId %d", signedPreKeyId);
			return null;
		}
		try {
			return new SignedPreKeyRecord(keyPair.getKeyPair());
		} catch (IOException e) {
			logger.errorf("Greska prilikom kreiranja signedPreKeyId-a! %s", e.getMessage());
			return null;
		}
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		/*
		 * PRAZNO
		 */
		logger.warn("METOD loadSignedPreKeys JE PRAZAN");
		return null;
	}

	@Override
	public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
		keyService.merge(identityKeyStore.getMyAddress(), record.serialize(), signedPreKeyId, KeyType.SIGNEDPREKEY);
	}

	@Override
	public boolean containsSignedPreKey(int signedPreKeyId) {
		/*
		 * PRAZNO
		 */
		logger.warn("METOD containsSignedPreKey JE PRAZAN");
		return false;
	}

	@Override
	public void removeSignedPreKey(int signedPreKeyId) {
		/*
		 * PRAZNO
		 */
		logger.warn("METOD removeSignedPreKey JE PRAZAN");

	}

	@Override
	public void storeSession(SignalProtocolAddress address, SessionRecord record) {
		//sessionService.storeSession(new SessionEntity(address, record.serialize()));
		sessionService.storeSession(address, record);
	}

	@Override
	public SessionRecord loadSession(SignalProtocolAddress address) {
		SessionEntity sessionEntity = sessionService.getSession(address);
		if (sessionEntity == null) {
			logger.warnf("Trazena sesija %s %s ne postoji", address.getName(), address.getDeviceId());
			return new SessionRecord();
		}
		try {
			return new SessionRecord(sessionEntity.getSession());
		} catch (IOException e) {
			logger.error("Greska prilikom deserilizacije sesije iz baze!");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getSubDeviceSessions(String name) {
		/*
		 * PRAZNO
		 */
		logger.warn("Metod getSubDeviceSessions je prazan!");
		return null;
	}

	@Override
	public boolean containsSession(SignalProtocolAddress address) {
		//SessionRecord session = this.loadSession(address);
		SessionEntity entity = sessionService.getSession(address);
		return (entity != null);
	}

	@Override
	public void deleteSession(SignalProtocolAddress address) {
		/*
		 * PRAZNO
		 */
		logger.warn("Metod deleteSession je prazan!");
	}

	@Override
	public void deleteAllSessions(String name) {
		/*
		 * PRAZNO
		 */
		logger.warn("Metod deleteAllSessions je prazan!");

	}

}
