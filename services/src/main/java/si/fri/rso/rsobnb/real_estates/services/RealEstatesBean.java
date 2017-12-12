package si.fri.rso.rsobnb.real_estates.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import si.fri.rso.rsobnb.real_estates.RealEstate;

@ApplicationScoped
public class RealEstatesBean {

    @Inject
    private EntityManager em;

    public List<RealEstate> getRealEstates(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, RealEstate.class, queryParameters);

    }

    public List<RealEstate> getRealEstateFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, RealEstate.class, queryParameters);
    }

    public RealEstate getRealEstate(String realEstateId) {


        RealEstate realEstate = em.find(RealEstate.class, realEstateId);

        if (realEstate == null) {
            throw new NotFoundException();
        }

        return realEstate;
    }


    public RealEstate createdRealEstate(RealEstate realEstate) {

        try {
            beginTx();
            em.persist(realEstate);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return realEstate;
    }

    public RealEstate putRealEstate(String realEstateId, RealEstate realEstate) {

        RealEstate r = em.find(RealEstate.class, realEstateId);

        if (r == null) {
            return null;
        }

        try {
            beginTx();
            realEstate.setId(r.getId());
            realEstate = em.merge(realEstate);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return realEstate;
    }

    public boolean deleteRealEstate(String realEstateId) {

        RealEstate realEstate = em.find(RealEstate.class, realEstateId);

        if (realEstate != null) {
            try {
                beginTx();
                em.remove(realEstate);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
