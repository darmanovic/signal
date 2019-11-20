package signal.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.whispersystems.libsignal.SignalProtocolAddress;

/**
 * @author goran
 * Klasa koja modelira korisnicku adresu u bazi
 */
@Entity
public class UserAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	private String userId;
	private int deviceId;

	public UserAddress() {}

	public UserAddress(SignalProtocolAddress address) {
		this.userId = address.getName();
		this.deviceId = address.getDeviceId();
	}

	public SignalProtocolAddress getAddress() {
		return new SignalProtocolAddress(userId, deviceId);
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

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

}
