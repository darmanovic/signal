package signal.client;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.KeyStoreSpi;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;

import signal.client.service.ClientKeyService;
import signal.client.service.CommunicationService;
import signal.entities.MessageModel;
import signal.server.service.KeyService;

/**
 * @author goran
 * Centralna klasa bota!
 */
@Singleton
public class SignalClient implements Serializable {
	private static final long serialVersionUID = -6287374610118397096L;

	/**
	 * Instanciranje servisa!
	 */
	@Inject
	private Logger logger;

	@Inject
	private SignalProtocolStore store;

	@Inject
	private MyIdentityStore myIdentityStore;

	@Inject
	private CommunicationService communicationService;

	/**
	 * Definisanje korisnicke adrese, registracionog ID-a i ID-a uredjaja
	 */
	private final int REGISTRATION_ID = 100;
	private final String UID = "12345";
	final int DEVICE_ID = 1;

	@SuppressWarnings("unused")
	@PostConstruct
	public void init() {
		logger.info("SignalClient start! User: " + UID);
		myIdentityStore.setMyAddress(new SignalProtocolAddress(UID, DEVICE_ID));
		myIdentityStore.setRegistrationID(REGISTRATION_ID);

		IdentityKeyPair identityKeyPair = store.getIdentityKeyPair();

		/**
		 * Provjera da li se radi o prvom pokretanju. U koliko je prvo pokretanje generisi kljuceve
		 */
		if (identityKeyPair == null) {
			logger.info("Identitetski kljuc ne postoji!");
			this.initialKeyGeneration();
			identityKeyPair = store.getIdentityKeyPair();
		} else {
			logger.info("Kljucevi su prethodno generisani!");
		}

		//this.slanjePoruke();
	}

	/**
	 * Metod koji na svake dvije sekunde poziva server, i provjerava da li ima novih poruka bota
	 */
	@Schedule(second = "*/2", minute = "*", hour = "*", dayOfWeek = "*", persistent = false, info = "MESSAGE_TIMER")
	public void getNewMessages() {
		//logger.infof("Provjeravam da li ima novih poruka za %s", myIdentityStore.getMyAddress().getName());
		List<MessageModel> newMessages = communicationService.getNewMessages(myIdentityStore.getMyAddress());
		//logger.infof("Broj poruka: %s", newMessages.size());

		/**
		 * U koliko ima novih poruka, obradi svaku od njih
		 */
		for(MessageModel message : newMessages) {
			/**
			 * Instanciranje sessionCiphera, koji predstavlja stanje u double ratchetu
			 */
			SignalProtocolAddress senderAddress = new SignalProtocolAddress(message.getUserIdSender(), message.getDeviceIdSender());
			SessionCipher sessionCipher = new SessionCipher(store, senderAddress);
			try {
				String decryptedMessage = null;

				/**
				 * Provjera da li se radi o inicijalizacionoj ili regularnoj poruci i dekripcija poruke na osnovu stanja u trenutnoj sesiji
				 */
				if(message.isPreKeyMessage()) {
					PreKeySignalMessage pksm = new PreKeySignalMessage(KeyService.decode(message.getMessage()));
					decryptedMessage = new String(sessionCipher.decrypt(pksm), StandardCharsets.UTF_8);
				} else {
					SignalMessage sm = new SignalMessage(KeyService.decode(message.getMessage()));
					decryptedMessage = new String(sessionCipher.decrypt(sm), StandardCharsets.UTF_8);
				}

				logger.infof("DEKRIPTOVANA PORUKA: %s", decryptedMessage);
				slanjePoruke(decryptedMessage);
			} catch (InvalidMessageException e) {
				e.printStackTrace();
			} catch (InvalidVersionException e) {
				e.printStackTrace();
			} catch (DuplicateMessageException e) {
				e.printStackTrace();
			} catch (LegacyMessageException e) {
				e.printStackTrace();
			} catch (InvalidKeyIdException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (UntrustedIdentityException e) {
				e.printStackTrace();
			} catch (NoSessionException e) {
				e.printStackTrace();
			}
}
	}
	/**
	 * Generisanje kljuca prilikom prvog pokretanja bota
	 */
	private void initialKeyGeneration() {
		
		IdentityKeyPair myIdentityKeyPair = KeyHelper.generateIdentityKeyPair();
		List<PreKeyRecord> preKeys = KeyHelper.generatePreKeys(1, 10);

		SignedPreKeyRecord signedPreKey = null;
		try {
			signedPreKey = KeyHelper.generateSignedPreKey(myIdentityKeyPair, 1);
		} catch (InvalidKeyException e) {
			logger.error("Doslo je do greske prilikom generisanja SignedPreKeyRecord!");
			logger.error(e.getMessage());
			return;
		}

		/**
		 * Cuvanje generisanih kljuceva u bazi
		 */
		myIdentityStore.saveMyIdentity(myIdentityKeyPair);
		store.storeSignedPreKey(1, signedPreKey);

		preKeys.stream().forEach(preKey -> {
			store.storePreKey(preKey.getId(), preKey);
		});
		
		/**
		 * Slanje javnih kljuceva na server prilikom registrovanja
		 */
		communicationService.sendKeys(myIdentityStore.getMyAddress(), store.getLocalRegistrationId(), myIdentityKeyPair.getPublicKey(), signedPreKey, preKeys);
		communicationService.saveAddress(myIdentityStore.getMyAddress());

	}

	public String getUid() {
		return UID;
	}
	
	
	private void slanjePoruke(String message) {	
		CiphertextMessage cipherText = null;
		boolean preKeyMessage = true;
		/**
		 * Instanciranje adrese kojoj saljemo poruku. Provjera da li sa njom postoji sesija.
		 */
		SignalProtocolAddress remoteAddress = new SignalProtocolAddress("068000001", 1);
		if (store.containsSession(remoteAddress) == false) {
			/**
			 * U koliko sesija ne postoji nabavi kljuceve sa servera. Ovo se trenutno nikad nece izvrstiti jer bot samo odgovara na poruke, ne salje ih prvi!
			 */
			PreKeyBundle preKeyBundle = communicationService.getKeys(remoteAddress.getName());
			SessionBuilder sb = new SessionBuilder(store, remoteAddress);
			try {
				/**
				 * Obrada primljenog seta kljuceva
				 */
				sb.process(preKeyBundle);
				/**
				 * Instanciranje sesije
				 */
				SessionCipher sessionChiper = new SessionCipher(store, remoteAddress);
				/**
				 * Enkriptovanje poruke
				 */
				cipherText = sessionChiper.encrypt(message.getBytes(StandardCharsets.UTF_8));
				preKeyMessage = true;
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (UntrustedIdentityException e) {
				e.printStackTrace();
			}
		} else {
			/**
			 * Za slucaj da je sesija prethodno postojala, instancira se sesija i poruka enkriptuje.
			 */
			SessionCipher sessionCipher = new SessionCipher(store, remoteAddress);
			try {
				cipherText = sessionCipher.encrypt(message.getBytes(StandardCharsets.UTF_8));
				preKeyMessage = false;
			} catch (UntrustedIdentityException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Slanje poruke na server
		 */
		communicationService.sendMessage(KeyService.encode(cipherText.serialize()), remoteAddress.getName(), preKeyMessage, myIdentityStore.getMyAddress());

	}

}
