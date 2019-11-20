package signal.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("keys")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface ExchangeKeysRest {
	
	@GET
	@Path("{userId}")
	public Response getKeys(@PathParam("userId") String userId);
	
	@POST
	public Response setkeys(String userKeys);
}
