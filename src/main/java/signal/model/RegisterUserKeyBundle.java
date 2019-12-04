package signal.model;

import java.util.Map;

/**
 * @author Radoje
 * Data klasa koja modelira kljuceve koji se dodaju na server prilikom registracije
 */
public class RegisterUserKeyBundle {

	private String userId;
	private int registrationId;
	private int deviceId;
	private String identityKey;
	private int signedPreKeyId;
	private String signedPreKey;
	private String signedPreKeySignature;
	private Map<Integer, String> preKeys;
	
	public RegisterUserKeyBundle() {}

	public RegisterUserKeyBundle(String userId, int registrationId, int deviceId, String identityKey, int signedPreKeyId, String signedPreKey,
			String signedPreKeySignature, Map<Integer, String> preKeys) {
		super();
		this.userId = userId;
		this.registrationId = registrationId;
		this.deviceId = deviceId;
		this.identityKey = identityKey;
		this.signedPreKeyId = signedPreKeyId;
		this.signedPreKey = signedPreKey;
		this.signedPreKeySignature = signedPreKeySignature;
		this.preKeys = preKeys;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(int registrationId) {
		this.registrationId = registrationId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getIdentityKey() {
		return identityKey;
	}

	public void setIdentityKey(String identityKey) {
		this.identityKey = identityKey;
	}

	public int getSignedPreKeyId() {
		return signedPreKeyId;
	}

	public void setSignedPreKeyId(int signedPreKeyId) {
		this.signedPreKeyId = signedPreKeyId;
	}

	public String getSignedPreKey() {
		return signedPreKey;
	}

	public void setSignedPreKey(String signedPreKey) {
		this.signedPreKey = signedPreKey;
	}

	public String getSignedPreKeySignature() {
		return signedPreKeySignature;
	}

	public void setSignedPreKeySignature(String signedPreKeySignature) {
		this.signedPreKeySignature = signedPreKeySignature;
	}

	public Map<Integer, String> getPreKeys() {
		return preKeys;
	}

	public void setPreKeys(Map<Integer, String> preKeys) {
		this.preKeys = preKeys;
	}

	@Override
	public String toString() {
		return "RegisterUserKeyBundle [userId=" + userId + ", registrationId=" + registrationId + ", deviceId=" + deviceId + ", identityKey=" + identityKey
				+ ", signedPreKeyId=" + signedPreKeyId + ", signedPreKey=" + signedPreKey + ", signedPreKeySignature=" + signedPreKeySignature + ", preKeys="
				+ preKeys + "]";
	}

}
