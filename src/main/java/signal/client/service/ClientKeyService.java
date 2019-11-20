package signal.client.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.whispersystems.libsignal.SignalProtocolAddress;

import signal.client.entities.KeyPairEntity;
import signal.util.KeyType;

/**
 * @author goran
 * Klasa koja vrsi upis kljuceva ili cita kljuceve iz baze bota
 */
@Stateless
public class ClientKeyService {

	@Inject
	private EntityManager em;

	@Inject
	private Logger logger;

	public KeyPairEntity merge(SignalProtocolAddress address, byte[] keyPair, KeyType keyType) {
		return em.merge(new KeyPairEntity(address, keyPair, keyType));
	}

	public KeyPairEntity merge(SignalProtocolAddress address, byte[] keyPair, Integer index, KeyType keyType) {
		return em.merge(new KeyPairEntity(address, keyPair, keyType, index));
	}
	
	public boolean deactivateKeyPair(SignalProtocolAddress address, KeyType keyType, Integer index) {
		KeyPairEntity keyPair = this.getKeyPair(address, keyType, index);
		if(keyPair == null) {
			logger.warnf("%s sa indeksom %d nije pronadjen prilikom uklanjanja!", keyType, index);
		} else {
			keyPair.setActive(false);
			em.merge(keyPair);			
		}
		return true;
	}

	public KeyPairEntity getKeyPair(SignalProtocolAddress address, KeyType keyType) {
		List<KeyPairEntity> keyPairs = em
				.createQuery("SELECT k FROM KeyPairEntity k WHERE k.active = true AND k.keyType = :keyType AND k.name = :name AND k.deviceId = :deviceId",
						KeyPairEntity.class)
				.setParameter("keyType", keyType).setParameter("name", address.getName()).setParameter("deviceId", address.getDeviceId()).getResultList();
		if (keyPairs.isEmpty()) {
			logger.warnf("query ne vraca ni jedan kljuc %s!", keyType);
			return null;
		}
		return keyPairs.get(0);
	}
	
	public KeyPairEntity getKeyPair(SignalProtocolAddress address, KeyType keyType, Integer index) {
		List<KeyPairEntity> keyPairs = em
				.createQuery("SELECT k FROM KeyPairEntity k WHERE k.active = true AND k.keyType = :keyType AND k.name = :name AND k.deviceId = :deviceId AND k.index = :index",
						KeyPairEntity.class)
				.setParameter("keyType", keyType)
				.setParameter("name", address.getName())
				.setParameter("deviceId", address.getDeviceId())
				.setParameter("index", index)
				.getResultList();
		if (keyPairs.isEmpty()) {
			logger.warnf("query ne vraca ni jedan kluc! %s sa indeksom %d", keyType, index);
			return null;
		}
		return keyPairs.get(0);
	}
}
