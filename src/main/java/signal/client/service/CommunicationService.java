package signal.client.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import signal.entities.MessageModel;
import signal.entities.UserAddress;
import signal.model.FirstMessageKeyBundle;
import signal.model.RegisterUserKeyBundle;
import signal.rest.AddressService;
import signal.rest.ExchangeKeysRest;
import signal.rest.MessagesRest;
import signal.server.service.KeyService;

/**
 * @author Radoje
 * Servis za komunikaciju sa serverom
 */
@Stateless
public class CommunicationService {

	@Inject
	private Logger logger;

	private Gson Gson() {
		return new GsonBuilder().disableHtmlEscaping().create();
	}

	/**
	 * @author Radoje
	 * Kreiranje resteasy targeta
	 */
	private ResteasyWebTarget clientBuilder() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target("http://localhost:8080/signal/rest/");
		return target;
	}

	/**
	 * @author Radoje
	 * Metod za slanje kljuceva na server
	 */
	public void sendKeys(SignalProtocolAddress address, int registrationId, IdentityKey identityKey, SignedPreKeyRecord signedPreKey,
			List<PreKeyRecord> preKeys) {
		ExchangeKeysRest exchangeKeys = this.clientBuilder().proxy(ExchangeKeysRest.class);

		String identityKeyPublic = KeyService.encode(identityKey.getPublicKey().serialize());

		String signedPreKeyPublic = KeyService.encode(signedPreKey.getKeyPair().getPublicKey().serialize());
		String signedPreKeySignature = KeyService.encode(signedPreKey.getSignature());
		int signedPreKeyId = signedPreKey.getId();

		Map<Integer, String> preKeysPublic = new HashMap<>();
		preKeys.stream().forEach(preKey -> {
			Integer preKeyId = preKey.getId();
			String preKeyPublic = KeyService.encode(preKey.getKeyPair().getPublicKey().serialize());
			preKeysPublic.put(preKeyId, preKeyPublic);
		});
		RegisterUserKeyBundle keyBundle = new RegisterUserKeyBundle(address.getName(), registrationId, address.getDeviceId(), identityKeyPublic, signedPreKeyId,
				signedPreKeyPublic, signedPreKeySignature, preKeysPublic);
		String jsonString = Gson().toJson(keyBundle);
		logger.info(jsonString);
		exchangeKeys.setkeys(jsonString);

	}

	/**
	 * @author Radoje
	 * Metod za dobavljanje kljuceva sa servera
	 */
	public PreKeyBundle getKeys(String userId) {
		ExchangeKeysRest exchangeKeys = this.clientBuilder().proxy(ExchangeKeysRest.class);
		Response response = exchangeKeys.getKeys(userId);
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.errorf("Greska prilikom getKeys zahtjeva! %s %s", response.getStatus(), response.getEntity());
			return null;
		}
		FirstMessageKeyBundle firstKeyBundle = response.readEntity(FirstMessageKeyBundle.class);
		if (firstKeyBundle == null) {
			return null;
		}
		ECPublicKey preKey = null;
		ECPublicKey signedPreKey = null;
		try {
			preKey = Curve.decodePoint(KeyService.decode(firstKeyBundle.getPreKey()), 0);
			signedPreKey = Curve.decodePoint(KeyService.decode(firstKeyBundle.getSignedPreKey()), 0);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
		byte[] signedPreKeySignature = KeyService.decode(firstKeyBundle.getSignedPreKeySignature());
		IdentityKey identityKey = null;
		try {
			identityKey = new IdentityKey(KeyService.decode(firstKeyBundle.getIdentityKey()), 0);
		} catch (InvalidKeyException e) {
			logger.error("Doslo je do greske prilikom kreiranja identitetskog kljuca!");
			e.printStackTrace();
		}

		return new PreKeyBundle(firstKeyBundle.getRegistrationId(), firstKeyBundle.getDeviceId(), firstKeyBundle.getPreKeyId(), preKey,
				firstKeyBundle.getSignedPreKeyId(), signedPreKey, signedPreKeySignature, identityKey);
	}

	/**
	 * @author Radoje
	 * Metod za dobavljanje korisnicke adrese sa servera
	 */
	public SignalProtocolAddress getUserAddress(String userId) {
		AddressService addressService = this.clientBuilder().proxy(AddressService.class);
		Response response = addressService.getAddress(userId);
		
		if(response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			return null;
		}
		String jsonString = response.readEntity(String.class);
		UserAddress ua = new Gson().fromJson(jsonString, UserAddress.class);
		SignalProtocolAddress spa = ua.getAddress();
		return spa;
	}

	/**
	 * @author Radoje
	 * Cuvanje korisnicke adrese na serveru
	 */
	public void saveAddress(SignalProtocolAddress address) {
		AddressService addressService = this.clientBuilder().proxy(AddressService.class);
		String jsonString = new Gson().toJson(new UserAddress(address));
		Response response = addressService.setAddress(jsonString);
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.errorf("Greska prilikom saveAddress zahtjeva! %s %s", response.getStatus(), response.getEntity());
			return;
		}
	}

	public List<MessageModel> getNewMessages(SignalProtocolAddress address) {
		return this.getNewMessages(address.getName());
	}

	/**
	 * @author Radoje
	 * Dobavljanje novih poruka sa servera
	 */
	public List<MessageModel> getNewMessages(String userId) {
		MessagesRest messagesService = this.clientBuilder().proxy(MessagesRest.class);
		Response response = messagesService.getMessages(userId);
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.errorf("Greska prilikom checkForMessagesZahtjeva! %s %s", response.getStatus(), response.getEntity());
			return null;
		}
		return Arrays.asList(response.readEntity(MessageModel[].class));
	}

	/**
	 * @author Radoje
	 * Slanje poruka na server
	 */
	public void sendMessage(String message, String receiverName, boolean preKeyMessage, SignalProtocolAddress address) {
		MessagesRest messagesService = this.clientBuilder().proxy(MessagesRest.class);
		messagesService.setMessage(Gson().toJson(new MessageModel(address.getName(), address.getDeviceId(), receiverName, message, preKeyMessage)));
	}

}
