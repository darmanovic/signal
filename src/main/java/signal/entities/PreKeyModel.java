package signal.entities;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author goran
 * Klasa koja modelira predkljuc u bazi
 */
@Entity
public class PreKeyModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;

	private int preKeyId;
	private byte[] preKeyPublic;

	public PreKeyModel() {
	}

	public PreKeyModel(String userId, int preKeyId, byte[] preKeyPublic) {
		super();
		this.userId = userId;
		this.preKeyId = preKeyId;
		this.preKeyPublic = preKeyPublic;
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

	public int getPreKeyId() {
		return preKeyId;
	}

	public void setPreKeyId(int preKeyId) {
		this.preKeyId = preKeyId;
	}

	public byte[] getPreKeyPublic() {
		return preKeyPublic;
	}

	public void setPreKeyPublic(byte[] preKeyModel) {
		this.preKeyPublic = preKeyModel;
	}

	@Override
	public String toString() {
		return "PreKeyModel [id=" + id + ", userId=" + userId + ", preKeyId=" + preKeyId + ", preKeyPublic=" + Arrays.toString(preKeyPublic) + "]";
	}

}