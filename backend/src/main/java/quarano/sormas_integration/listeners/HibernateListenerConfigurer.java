package quarano.sormas_integration.listeners;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * @author Federico Grasso
 *
 * Hibernate listener configurer
 */
@Component
public class HibernateListenerConfigurer {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Inject
    private UpdateListener updateListener;
    @Inject
    private InsertListener insertListener;

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        //registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(updateListener);
        //registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(insertListener);
    }
}
