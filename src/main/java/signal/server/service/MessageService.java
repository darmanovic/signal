package signal.server.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import signal.entities.MessageModel;

/**
 * @author Radoje
 * Klasa koja upisuje i iscitava poruke za datog korisnika u bazu
 */
@Stateless
public class MessageService {

	@Inject
	private EntityManager em;
	
//	@Inject
//	private Logger logger;
	
	/**
	 * @author Radoje
	 * Metod cita sve neprocitane poruke za datog korisnika
	 */
	public List<MessageModel> getMessages(String userId){
		List<MessageModel> messages = em.createQuery("SELECT m FROM MessageModel m WHERE m.userIdReceiver = :userId AND m.read = FALSE", MessageModel.class).setParameter("userId", userId).getResultList();
		for(MessageModel message : messages) {
			message.setRead(true);
			this.setMessage(message);
		}
		return messages;
	}
	/**
	 * @author Radoje
	 * Cuvanje prosledjene poruke u bazu
	 */
	public MessageModel setMessage(MessageModel message) {
		return em.merge(message);
	}

}
