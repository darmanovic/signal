package signal.entities;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author goran
 * Klasa koja modelira potpisani pred kljuc u bazi
 */
@Entity
public class SignedPreKeyModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	private int signedPreKeyId;
	private byte[] signedPreKeyPublic;
	private byte[] signedPreKeySignature;
	
	public SignedPreKeyModel() {}

	public SignedPreKeyModel(String userId, int signedPreKeyId, byte[] signedPreKeyPublic, byte[] signedPreKeySignature) {
		super();
		this.userId = userId;
		this.signedPreKeyId = signedPreKeyId;
		this.signedPreKeyPublic = signedPreKeyPublic;
		this.signedPreKeySignature = signedPreKeySignature;
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

	public int getSignedPreKeyId() {
		return signedPreKeyId;
	}

	public void setSignedPreKeyId(int signedPreKeyId) {
		this.signedPreKeyId = signedPreKeyId;
	}

	public byte[] getSignedPreKeyPublic() {
		return signedPreKeyPublic;
	}

	public void setSignedPreKeyPublic(byte[] signedPreKeyPublic) {
		this.signedPreKeyPublic = signedPreKeyPublic;
	}

	public byte[] getSignedPreKeySignature() {
		return signedPreKeySignature;
	}

	public void setSignedPreKeySignature(byte[] signedPreKeySignature) {
		this.signedPreKeySignature = signedPreKeySignature;
	}

	@Override
	public String toString() {
		return "SignedPreKeyModel [id=" + id + ", userId=" + userId + ", signedPreKeyId=" + signedPreKeyId + ", signedPreKeyPublic="
				+ Arrays.toString(signedPreKeyPublic) + ", signedPreKeySignature=" + Arrays.toString(signedPreKeySignature) + "]";
	}

}
