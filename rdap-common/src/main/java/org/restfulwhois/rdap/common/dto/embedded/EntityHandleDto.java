package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

/**
 * EntityHandleDto.
 * 
 * @author jiashuo.
 * 
 */
public class EntityHandleDto extends HandleDto {
    /**
     * entity role list.
     */
    private List<String> roles;

    /**
     * get roles.
     * 
     * @return roles.
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * set roles.
     * 
     * @param roles
     *            roles.
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
