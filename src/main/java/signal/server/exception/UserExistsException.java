package signal.server.exception;

public class UserExistsException extends Exception {

	private static final long serialVersionUID = 2573595173519352694L;

	public UserExistsException(String message) {
		super(message);
	}

}
