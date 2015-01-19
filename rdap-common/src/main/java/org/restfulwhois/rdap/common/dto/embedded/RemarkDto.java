package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

public class RemarkDto {
    /**
     * title.
     */
    private String title;
    /**
     * description.
     */
    private List<String> description;
    /**
     * links.
     */
    private List<LinkDto> links;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }

}