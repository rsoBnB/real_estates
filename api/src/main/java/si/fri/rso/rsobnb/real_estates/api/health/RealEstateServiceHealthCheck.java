package si.fri.rso.rsobnb.real_estates.api.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.rso.rsobnb.real_estates.api.configuration.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class RealEstateServiceHealthCheck implements HealthCheck{

    @Inject
    private RestProperties restProperties;

    private Logger log = Logger.getLogger(RealEstateServiceHealthCheck.class.getName());

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(RealEstateServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(RealEstateServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }
}
