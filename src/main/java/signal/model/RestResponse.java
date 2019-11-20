package signal.model;

public class RestResponse {
	private Boolean ok;
	private String message;
	
	public RestResponse() {}

	public RestResponse(Boolean ok, String message) {
		super();
		this.ok = ok;
		this.message = message;
	}

	public Boolean getOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RestResponse [ok=" + ok + ", message=" + message + "]";
	}
	
}
