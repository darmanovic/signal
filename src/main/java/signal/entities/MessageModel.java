package signal.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Radoje
 * Klasa koja modelira poruku u bazi
 */
@Entity
public class MessageModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private boolean read;
	private String userIdSender;
	private int deviceIdSender;
	private String userIdReceiver;
	private String message;
	private boolean preKeyMessage;

	public MessageModel() {}

	public MessageModel(String userIdSender, int deviceIdSender, String userIdReceiver, String message, boolean preKeyMessage) {
		this.userIdSender = userIdSender;
		this.deviceIdSender = deviceIdSender;
		this.userIdReceiver = userIdReceiver;
		this.message = message;
		this.preKeyMessage = preKeyMessage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getUserIdSender() {
		return userIdSender;
	}

	public void setUserIdSender(String userIdSender) {
		this.userIdSender = userIdSender;
	}

	public String getUserIdReceiver() {
		return userIdReceiver;
	}

	public void setUserIdReceiver(String userIdReceiver) {
		this.userIdReceiver = userIdReceiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	public int getDeviceIdSender() {
		return deviceIdSender;
	}

	public void setDeviceIdSender(int deviceIdSender) {
		this.deviceIdSender = deviceIdSender;
	}
	
	public boolean isPreKeyMessage() {
		return preKeyMessage;
	}

	public void setPreKeyMessage(boolean preKeyMessage) {
		this.preKeyMessage = preKeyMessage;
	}

	@Override
	public String toString() {
		return "MessageModel [id=" + id + ", read=" + read + ", userIdSender=" + userIdSender + ", deviceIdSender=" + deviceIdSender + ", userIdReceiver="
				+ userIdReceiver + ", message=" + message + ", preKeyMessage=" + preKeyMessage + "]";
	}

}
