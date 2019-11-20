package signal.util;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

/*
 * Log producer i entityManager producer, da bi se omogucio rad sa bazom.
 */
public class FixedProducer {

	@Default
	@Produces
	@PersistenceContext
	private EntityManager em;
	
	@Produces  
    public Logger produceLogger(InjectionPoint injectionPoint) {  
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());  
    }
}
