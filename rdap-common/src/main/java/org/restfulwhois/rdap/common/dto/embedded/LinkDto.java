package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

public class LinkDto {
    private String value;
    private String rel;
    private String href;
    /**
     * href language of link.
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
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getHreflang() {
        return hreflang;
    }

    public void setHreflang(List<String> hreflang) {
        this.hreflang = hreflang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

}