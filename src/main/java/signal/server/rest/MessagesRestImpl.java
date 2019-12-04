package signal.server.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import signal.entities.MessageModel;
import signal.model.RestResponse;
import signal.rest.MessagesRest;
import signal.server.service.MessageService;

/**
 * @author Radoje
 * REST endpoint za slanje i primanje poruka od klijenata
 */
public class MessagesRestImpl implements MessagesRest {
	
	@Inject
	private MessageService messageService;
	
	@Inject
	private Logger logger;
	
	private Gson Gson() {
		return new GsonBuilder().disableHtmlEscaping().create();
	}

	/**
	 * @author Radoje
	 * REST endpoint koji vraca listu neprocitanih poruka
	 */
	public Response getMessages(@PathParam("userId") String userId) {
		//logger.infof("Korisnik %s pregleda nove poruke!", userId);
		List<MessageModel> messages = messageService.getMessages(userId);
		/*
		 * UKLONITI KOMENTAR DA BI SE PORUKE OZNACAVALE KAO PROCITANE!
		 */
		messages.forEach(message->{
			message.setRead(true);
			messageService.setMessage(message);
		});
		return Response.ok(messages).build();
	}
	
	/**
	 * @author Radoje
	 * Endpoint na koji klijenti salju poruke koje zele poslati drugom korisniku
	 */
	public Response setMessage(String rawMessage) {
		MessageModel message = Gson().fromJson(rawMessage, MessageModel.class);
		messageService.setMessage(message);
		return Response.ok(new RestResponse(true, "Poruka upisana!")).build();
	}
}
