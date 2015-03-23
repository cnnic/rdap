package org.restfulwhois.rdap.common.dto;

import java.util.List;

import org.restfulwhois.rdap.common.dto.embedded.EntityAddressDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityTelephoneDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;

/**
 * EntityDto.
 * 
 * @author jiashuo.
 * 
 */
public class EntityDto extends BaseDto {
    /**
     * fn.
     */
    private String fn;
    /**
     * kind.
     */
    private String kind;
    /**
     * email.
     */
    private String email;
    /**
     * title.
     */
    private String title;
    /**
     * org.
     */
    private String org;
    /**
     * url.
     */
    private String url;
    /**
     * lang.
     */
    private String lang;
    /**
     * addresses.
     */
    private List<EntityAddressDto> addresses;
    /**
     * telephones.
     */
    private List<EntityTelephoneDto> telephones;
    /**
     * publicIds.
     */
    private List<PublicIdDto> publicIds;

    /**
     * get fn.
     * 
     * @return fn.
     */
    public String getFn() {
        return fn;
    }

    /**
     * set fn.
     * 
     * @param fn
     *            fn.
     */
    public void setFn(String fn) {
        this.fn = fn;
    }

    /**
     * get kind.
     * 
     * @return kind.
     */
    public String getKind() {
        return kind;
    }

    /**
     * set kind.
     * 
     * @param kind
     *            kind.
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * get email.
     * 
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email.
     * 
     * @param email
     *            email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get title.
     * 
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title.
     * 
     * @param title
     *            title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get org.
     * 
     * @return org.
     */
    public String getOrg() {
        return org;
    }

    /**
     * set org.
     * 
     * @param org
     *            org.
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * get url.
     * 
     * @return url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * set url.
     * 
     * @param url
     *            url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * get lang.
     * 
     * @return lang.
     */
    public String getLang() {
        return lang;
    }

    /**
     * set lang.
     * 
     * @param lang
     *            lang.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * get telephones.
     * 
     * @return telephones.
     */
    public List<EntityTelephoneDto> getTelephones() {
        return telephones;
    }

    /**
     * set telephones.
     * 
     * @param telephones
     *            telephones.
     */
    public void setTelephones(List<EntityTelephoneDto> telephones) {
        this.telephones = telephones;
    }

    /**
     * get addresses.
     * 
     * @return addresses.
     */
    public List<EntityAddressDto> getAddresses() {
        return addresses;
    }

    /**
     * set addresses.
     * 
     * @param addresses
     *            addresses.
     */
    public void setAddresses(List<EntityAddressDto> addresses) {
        this.addresses = addresses;
    }

    /**
     * get publicIds.
     * 
     * @return publicIds.
     */
    public List<PublicIdDto> getPublicIds() {
        return publicIds;
    }

    /**
     * set publicIds.
     * 
     * @param publicIds
     *            publicIds.
     */
    public void setPublicIds(List<PublicIdDto> publicIds) {
        this.publicIds = publicIds;
    }

}
