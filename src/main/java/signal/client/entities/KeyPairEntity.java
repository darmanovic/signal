package signal.client.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.whispersystems.libsignal.SignalProtocolAddress;

import signal.util.KeyType;

/**
 * @author goran
 * Klasa koja modeluje par kljuceva u bazi bota
 */
@Entity
public class KeyPairEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Integer index;
	private boolean active = true;
	private byte[] keyPair;
	@Enumerated(EnumType.STRING)
	private KeyType keyType;
	private String name;
	private int deviceId;
	
	public KeyPairEntity() {}

	public KeyPairEntity(SignalProtocolAddress address, byte[] keyPair, KeyType keyType) {
		this.keyPair = keyPair;
		this.keyType = keyType;
		this.name = address.getName();
		this.deviceId = address.getDeviceId();
	}
	
	public KeyPairEntity(SignalProtocolAddress address, byte[] keyPair, KeyType keyType, Integer index) {
		this.keyPair = keyPair;
		this.keyType = keyType;
		this.index = index;
		this.name = address.getName();
		this.deviceId = address.getDeviceId();
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

	public byte[] getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(byte[] keyPair) {
		this.keyPair = keyPair;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
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
	
}
