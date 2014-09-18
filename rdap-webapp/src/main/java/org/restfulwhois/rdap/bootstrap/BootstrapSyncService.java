package org.restfulwhois.rdap.bootstrap;

import java.util.List;

import javax.annotation.Resource;

import org.restfulwhois.rdap.bootstrap.handler.RegistryHandler;
import org.springframework.stereotype.Service;

/**
 * This class is used to update bootstrap data from IANA registry.
 * <p>
 * {@link http://tools.ietf.org/html/draft-ietf-weirds-bootstrap-06#section-11
 * bootstrap-IANA consideration}
 * </p>
 * 
 * @author jiashuo
 * 
 */
@Service
public class BootstrapSyncService {

    /**
     * registryHandlers is defined in spring configuration file.
     */
    @Resource(name = "registryHandlers")
    private List<RegistryHandler> registryHandlers;

    /**
     * synchronize all registry data.
     */
    public void syncAllRegistry() {
        for (RegistryHandler handler : registryHandlers) {
            handler.handle();
        }
    }

}
