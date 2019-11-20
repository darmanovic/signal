package signal.client.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.whispersystems.libsignal.SignalProtocolAddress;

/**
 * @author goran
 * Klasa koja modeluje trenutno stanje sesije u bazi bota
 */
@Entity
public class SessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private boolean active = true;
	private String name;
	private int deviceId;
	private byte[] session;

	public SessionEntity() {}

	public SessionEntity(SignalProtocolAddress address, byte[] session) {
		this.name = address.getName();
		this.deviceId = address.getDeviceId();
		this.session = session;
	}

	public Long getId() {
		return id;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public byte[] getSession() {
		return session;
	}

	public void setSession(byte[] session) {
		this.session = session;
	}


}
