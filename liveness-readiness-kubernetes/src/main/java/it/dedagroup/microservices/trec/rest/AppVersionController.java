package it.dedagroup.microservices.trec.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.dedagroup.microservices.trec.appversionchecker.enums.Platform;
import it.dedagroup.microservices.trec.rest.requests.AppVersionRequest;

@Path("/api/appversion")
public class AppVersionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppVersionController.class);
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAppVersion(@QueryParam("platform") Platform platform) {
		
		return "1";
		
	}
	
	@POST
	@Path("/admin")
	public Response setAppVersion(AppVersionRequest request) {
		
		return Response.status(Status.CREATED).build();
		
	}
	
	@DELETE
	@Path("/admin")
	public Response deleteAppVersion(AppVersionRequest request) {
		
		LOGGER.info("Cancellazione versione "+request.getVersion()+" per "+request.getPlatform());
		return Response.status(Status.NO_CONTENT).build();
		
	}

}
