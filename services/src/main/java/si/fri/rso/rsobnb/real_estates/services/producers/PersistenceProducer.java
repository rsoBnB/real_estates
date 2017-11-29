package si.fri.rso.rsobnb.real_estates.services.producers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class PersistenceProducer {

    @PersistenceUnit(unitName = "real_estates-jpa")
    private EntityManagerFactory emf;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void disposeEntityManager(@Disposes EntityManager entityManager) {

        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
