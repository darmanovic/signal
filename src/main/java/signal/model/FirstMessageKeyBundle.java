package signal.model;

/**
 * @author Radoje
 * Data klasa koja modelira set kljuceva koje korisnik dobavlja sa servera prije slanja prve poruke drugom korisniku
 */
public class FirstMessageKeyBundle {

	private String userId;
	private int registrationId;
	private int deviceId;
	private String identityKey;
	private int signedPreKeyId;
	private String signedPreKey;
	private String signedPreKeySignature;
	private int preKeyId;
	private String preKey;

	public FirstMessageKeyBundle() {}

	public FirstMessageKeyBundle(String userId, int registrationId, int deviceId, String identityKey, int signedPreKeyId, String signedPreKey,
			String signedPreKeySignature, int preKeyId, String preKey) {
		super();
		this.userId = userId;
		this.registrationId = registrationId;
		this.deviceId = deviceId;
		this.identityKey = identityKey;
		this.signedPreKeyId = signedPreKeyId;
		this.signedPreKey = signedPreKey;
		this.signedPreKeySignature = signedPreKeySignature;
		this.preKeyId = preKeyId;
		this.preKey = preKey;
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

	public int getPreKeyId() {
		return preKeyId;
	}

	public void setPreKeyId(int preKeyId) {
		this.preKeyId = preKeyId;
	}

	public String getPreKey() {
		return preKey;
	}

	public void setPreKey(String preKey) {
		this.preKey = preKey;
	}

}
