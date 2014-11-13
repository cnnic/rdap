package org.restfulwhois.rdap.filters.httpFilter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.filter.FilterHelper;
import org.restfulwhois.rdap.core.common.filter.HttpFilter;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * This class is used to filter not implemented object type .
 * <p>
 * If a server receives a query that it cannot process because it 
 * is not implemented it will return  an HTTP 501.
 * <p> 
 *  
 * @author zhanyq
 * 
 */
public class NotImplementedUriFilter implements HttpFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NotImplementedUriFilter.class);

    /**
     * constructor.
     */
    public NotImplementedUriFilter() {
        super();
        LOGGER.debug("init RDAP filter:{}", this.getName());
    }

    /**
     * do pre process request not implement.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {       
        String path = request.getRequestURI();
        LOGGER.info("request URI: {} ", path);           
        String uri = path.substring(request.getContextPath().length());      
        if (containNotImplementedType(uri)) {
            ResponseEntity<ErrorMessage> responseEntity =
                    RestResponseUtil.createResponse501();
            FilterHelper.writeResponse(responseEntity, response);
            return false;
        }
        return true;
    }
    /**
     * check if decodeUri contain not implemented object types.
     * 
     * @param uri
     *            decodeUri.
     * @return true if contain, false if not.
     */
    public boolean containNotImplementedType(String uri) {        
        List<String> notImplementedUriList = RdapProperties
             .getNotImplementedUriList();
        for (String notImplementedUri :notImplementedUriList) {
            if (uri.startsWith(notImplementedUri)) {
                return true;             
            }
        }       
        return false;
    }
   
    /**
     * do post process.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true .
     */
    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
    }

    /**
     * @return this class name.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
