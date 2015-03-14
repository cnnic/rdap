package org.restfulwhois.rdap.bootstrap;

import java.util.List;

import javax.annotation.Resource;

import org.restfulwhois.rdap.bootstrap.handler.RegistryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BootstrapSyncService.class);

    /**
     * registryHandlers is defined in spring configuration file.
     */
    @Resource(name = "registryHandlers")
    private List<RegistryHandler> registryHandlers;

    /**
     * synchronize all registry data.
     */
    public void syncAllRegistry() {
        LOGGER.info("syncAllRegistry begin...");
        for (RegistryHandler handler : registryHandlers) {
            LOGGER.info("sync registry :{} begin", handler);
            handler.handle();
        }
        LOGGER.info("syncAllRegistry end.");
    }

}
