package se.krillsson.maintenance.health;

import com.codahale.metrics.health.HealthCheck;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SigarHealthCheck extends HealthCheck{

    @Override
    protected Result check() throws Exception {
        try {
            Sigar.load();
        } catch (SigarException e) {
            return Result.unhealthy(e);
        }
        return Result.healthy("Sigar loads fine");
    }
}
