package si.fri.rso.rsobnb.real_estates.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso.rsobnb.real_estates.*;

@RequestScoped
public class RealEstatesBean {

    private Logger log = LogManager.getLogger(RealEstatesBean.class.getName());

    private Client httpClient;

    @Context
    protected UriInfo uriInfo;

    @Inject
    private EntityManager em;

    @Inject
    private RealEstatesBean realEstateBean;

    @Inject
    @DiscoverService("images")
    private Optional<String> baseUrlImages;

    @Inject
    @DiscoverService("reviews")
    private Optional<String> baseUrlReviews;

    @Inject
    @DiscoverService("property_rental")
    private Optional<String> baseUrlPropertyRental;

    @Inject
    @DiscoverService("property_lease")
    private Optional<String> baseUrlPropertyLease;

    @Inject
    @DiscoverService("payments")
    private Optional<String> baseUrlPayments;



    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }


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

        List<Image> images = realEstateBean.getImages(realEstateId);
        realEstate.setImages(images);

        List<Review> reviews = realEstateBean.getReviews(realEstateId);
        realEstate.setReviews(reviews);

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

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getImagesFallback")
    @Timeout
    public List<Image> getImages(String realEstateId) {

        System.out.println("Base url: "+baseUrlImages);

        if (baseUrlImages.isPresent()) {
        //if (true) {
            System.out.println("Base url: "+baseUrlImages);

            try {
                return httpClient
                        .target(baseUrlImages.get() + "/v1/images?where=realEstateId:EQ:" + realEstateId)
                        //.target("http://172.17.0.1:8085" + "/v1/images?where=realEstateId:EQ:" + realEstateId)
                        .request().get(new GenericType<List<Image>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getReviewsFallback")
    @Timeout
    public List<Review> getReviews(String realEstateId) {

        System.out.println("Base url: "+baseUrlReviews);

        if (baseUrlReviews.isPresent()) {
        //if (true) {
            System.out.println("Base url: "+baseUrlReviews);

            try {
                return httpClient
                        .target(baseUrlReviews.get() + "/v1/reviews?where=realEstateId:EQ:" + realEstateId)
                        //.target("http://172.17.0.1:8083" + "/v1/reviews?where=realEstateId:EQ:" + realEstateId)
                        .request().get(new GenericType<List<Review>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getPropertyRentalFallback")
    @Timeout
    public List<PropertyRental> getPropertyRentals(String realEstateId) {

        System.out.println("Base url: "+baseUrlImages);

        if (baseUrlPropertyRental.isPresent()) {
            //if (true) {
            System.out.println("Base url: "+baseUrlPropertyRental);

            try {
                return httpClient
                        .target(baseUrlPropertyRental.get() + "/v1/property_rental?where=realEstateId:EQ:" + realEstateId)
                        .request().get(new GenericType<List<PropertyRental>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getPropertyLeaseFallback")
    @Timeout
    public List<PropertyLease> getPropertyLeases(String realEstateId) {

        System.out.println("Base url: "+baseUrlPropertyLease);

        if (baseUrlPropertyLease.isPresent()) {
            //if (true) {
            System.out.println("Base url: "+baseUrlPropertyLease);

            try {
                return httpClient
                        .target(baseUrlPropertyLease.get() + "/v1/property_lease?where=realEstateId:EQ:" + realEstateId)
                        .request().get(new GenericType<List<PropertyLease>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getPaymentFallback")
    @Timeout
    public List<Payment> getPayments(String realEstateId) {

        System.out.println("Base url: "+baseUrlPayments);

        if (baseUrlPayments.isPresent()) {
            //if (true) {
            System.out.println("Base url: "+baseUrlPayments);

            try {
                return httpClient
                        .target(baseUrlPayments.get() + "/v1/payments?where=realEstateId:EQ:" + realEstateId)
                        .request().get(new GenericType<List<Payment>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                System.out.println("Error: "+e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    public List<Image> getImagesFallback(String realEstateId) {
        System.out.println("Fallback called");

        List<Image> images = new ArrayList<>();

        Image image = new Image();

        image.setDescription("N/A");
        image.setId("N/A");
        image.setPath("N/A");

        images.add(image);

        return images;
    }

    public List<Review> getReviewsFallback(String realEstateId) {
        System.out.println("Fallback called");

        List<Review> reviews = new ArrayList<>();

        Review review = new Review();

        review.setTitle("N/A");
        review.setDescription("N/A");
        review.setId("N/A");
        review.setUserId("N/A");

        reviews.add(review);

        return reviews;
    }

    public List<PropertyRental> getPropertyRentalsFallback(String realEstateId) {
        System.out.println("Fallback called");

        List<PropertyRental> property_rentals = new ArrayList<>();

        PropertyRental property_rental = new PropertyRental();

        property_rental.setId("N/A");
        property_rental.setRealEstateId("N/A");
        property_rental.setRenterId("N/A");
        property_rental.setDuration(0);

        property_rentals.add(property_rental);

        return property_rentals;
    }

    public List<PropertyLease> getPropertyLeasesFallback(String realEstateId) {
        System.out.println("Fallback called");

        List<PropertyLease> property_leases = new ArrayList<>();

        PropertyLease property_lease = new PropertyLease();

        property_lease.setId("N/A");
        property_lease.setRealEstateId("N/A");
        property_lease.setRenterId("N/A");
        property_lease.setLeaserId("N/A");
        property_lease.setDuration(0);
        property_lease.setAvailable(false);

        property_leases.add(property_lease);

        return property_leases;
    }

    public List<Payment> getPaymentsFallback(String realEstateId) {
        System.out.println("Fallback called");

        List<Payment> payments = new ArrayList<>();

        Payment payment = new Payment();

        payment.setId("N/A");
        payment.setRealEstateId("N/A");
        payment.setLeaseId("N/A");
        payment.setAmount("N/A");

        payments.add(payment);

        return payments;
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
