package signal.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("messages")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface MessagesRest {

	@GET
	@Path("{userId}")
	public Response getMessages(@PathParam("userId") String userId);
	
	@POST
	public Response setMessage(String rawMessage);

}
