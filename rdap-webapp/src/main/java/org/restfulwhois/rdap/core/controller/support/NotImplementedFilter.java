package org.restfulwhois.rdap.core.controller.support;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.RdapProperties;
import org.restfulwhois.rdap.core.common.util.DecodeUriUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.model.ErrorMessage;
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
public class NotImplementedFilter implements RdapFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NotImplementedFilter.class);

    /**
     * constructor.
     */
    public NotImplementedFilter() {
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
        String decodeUri = DecodeUriUtil.decodeUri(request);
        if (StringUtils.isBlank(decodeUri)) {
            ResponseEntity<ErrorMessage> responseEntity =
                    RestResponseUtil.createResponse400();
            FilterHelper.writeResponse(responseEntity, response);
             return false;
        }
        if (containNotImplementedType(decodeUri)) {
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
     * @param decodeUri
     *            decodeUri.
     * @return true if contain, false if not.
     */
    private boolean containNotImplementedType(String decodeUri) {
        if (StringUtils.isBlank(decodeUri)) {
            return false;
        }
        String substringBeforeLast =
              StringUtils.substringBeforeLast(decodeUri, "/");
        List<String> notImplType = RdapProperties.getNotImplementedTypeList();
        if (notImplType.contains(substringBeforeLast)) {
            return true;
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
