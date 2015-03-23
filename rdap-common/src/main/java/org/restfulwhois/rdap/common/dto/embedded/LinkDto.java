package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * LinkDto.
 * 
 * @author jiashuo.
 * 
 */
public class LinkDto extends BaseDto {
    /**
     * link value.
     */
    private String value;
    /**
     * rel.
     */
    private String rel;
    /**
     * href.
     */
    private String href;
    /**
     * type.
     */
    private String type;
    /**
     * hreflang.
     */
    private List<String> hreflang;
    /**
     * title of link.
     */
    private String title;
    /**
     * media of link.
     */
    private String media;

    /**
     * get value.
     * 
     * @return value.
     */
    public String getValue() {
        return value;
    }

    /**
     * set value.
     * 
     * @param value
     *            value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * get rel.
     * 
     * @return rel.
     */
    public String getRel() {
        return rel;
    }

    /**
     * set rel.
     * 
     * @param rel
     *            rel.
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * get href.
     * 
     * @return href.
     */
    public String getHref() {
        return href;
    }

    /**
     * set href.
     * 
     * @param href
     *            href.
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get hreflang.
     * 
     * @return hreflang.
     */
    public List<String> getHreflang() {
        return hreflang;
    }

    /**
     * set hreflang.
     * 
     * @param hreflang
     *            hreflang.
     */
    public void setHreflang(List<String> hreflang) {
        this.hreflang = hreflang;
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
     * get media.
     * 
     * @return media.
     */
    public String getMedia() {
        return media;
    }

    /**
     * set media.
     * 
     * @param media
     *            media.
     */
    public void setMedia(String media) {
        this.media = media;
    }

}
