package si.fri.rso.rsobnb.real_estates.api.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.rsobnb.real_estates.RealEstate;
import si.fri.rso.rsobnb.real_estates.services.RealEstatesBean;
import si.fri.rso.rsobnb.real_estates.api.configuration.RestProperties;
import org.eclipse.microprofile.metrics.annotation.Metered;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@RequestScoped
@Path("/real_estates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class RealEstatesResource {

    @Inject
    private RealEstatesBean realEstatesBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Metered
    @Log
    public Response getRealEstates() {

        List<RealEstate> realEstates = realEstatesBean.getRealEstates(uriInfo);

        return Response.ok(realEstates).build();
    }

    @GET
    @Path("/filtered")
    @Log
    public Response getRealEstatesFiltered() {

        List<RealEstate> realEstates;

        realEstates = realEstatesBean.getRealEstateFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(realEstates).build();
    }

    @GET
    @Path("/{realEstateId}")
    @Log
    public Response getRealEstate(@PathParam("realEstateId") String realEstateId) {

        RealEstate realEstate = realEstatesBean.getRealEstate(realEstateId);

        if (realEstate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(realEstate).build();
    }

    @POST
    @Log
    public Response createRealEstate(RealEstate realEstate) {

        if ((realEstate.getName() == null || realEstate.getName().isEmpty()) || (realEstate.getLocation() == null
                || realEstate.getLocation().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            realEstate = realEstatesBean.createdRealEstate(realEstate);
        }

        if (realEstate.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(realEstate).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(realEstate).build();
        }
    }

    @PUT
    @Path("{realEstateId}")
    public Response putRealEstate(@PathParam("realEstateId") String realEstateId, RealEstate realEstate) {

        realEstate = realEstatesBean.putRealEstate(realEstateId, realEstate);

        if (realEstate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (realEstate.getId() != null)
                return Response.status(Response.Status.OK).entity(realEstate).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{realEstateId}")
    public Response deleteUser(@PathParam("realEstateId") String realEstateId) {

        boolean deleted = realEstatesBean.deleteRealEstate(realEstateId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        return Response.ok().build();
    }

    @GET
    @Path("healthy")
    public Response getHealth() {
        restProperties.isHealthy();
        return Response.ok().entity(restProperties.isHealthy()).build();
    }
}
