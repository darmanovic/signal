package signal.entities;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author goran
 * Klasa koja modelira tabletu u bazi za upis identitetskog kljuca
 */
@Entity
public class IdentityKeyModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;
	private int registrationId;
	private int deviceId;
	private byte[] identityKeyPublic;

	public IdentityKeyModel() {
	}

	public IdentityKeyModel(String userId, int registrationId, int deviceId, byte[] identityKeyPublic) {
		this.userId = userId;
		this.registrationId = registrationId;
		this.deviceId = deviceId;
		this.identityKeyPublic = identityKeyPublic;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public byte[] getIdentityKeyPublic() {
		return identityKeyPublic;
	}

	public void setIdentityKeyPublic(byte[] identityKeyPublic) {
		this.identityKeyPublic = identityKeyPublic;
	}

	@Override
	public String toString() {
		return "IdentityKeyModel [id=" + id + ", userId=" + userId + ", registrationId=" + registrationId + ", deviceId=" + deviceId + ", identityKeyPublic="
				+ Arrays.toString(identityKeyPublic) + "]";
	}

}
