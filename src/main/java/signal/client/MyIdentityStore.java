package signal.client;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;

import signal.client.service.ClientKeyService;
import signal.util.KeyType;

/*
 * Klasa pedstavlja trenutnog korisnika
 */
@Singleton
public class MyIdentityStore {
	
	@Inject private ClientKeyService keyService;

	private SignalProtocolAddress myAddress;
	
	private int REGISTRATION_ID = 0;
	
	public boolean saveMyIdentity(IdentityKeyPair identityKey) {
		keyService.merge(myAddress, identityKey.serialize(), KeyType.IDENTITY);
		return true;
	}

	public SignalProtocolAddress getMyAddress() {
		return myAddress;
	}

	public void setMyAddress(SignalProtocolAddress myAddress) {
		this.myAddress = myAddress;
	}

	public int getRegistrationID() {
		return REGISTRATION_ID;
	}

	public void setRegistrationID(int REGISTRATION_ID) {
		this.REGISTRATION_ID = REGISTRATION_ID;
	}
	
}
