package si.fri.rso.rsobnb.real_estates.api;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@RegisterService
public class RealEstateApplication extends Application {
}
