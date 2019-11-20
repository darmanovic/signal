package signal.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("user")
@Produces({"application/json"})
@Consumes({"application/json"})
public interface AddressService {

	@GET
	@Path("{userId}")
	public Response getAddress(@PathParam("userId") String userId);
	
	@POST
	public Response setAddress(String addressString);

}
