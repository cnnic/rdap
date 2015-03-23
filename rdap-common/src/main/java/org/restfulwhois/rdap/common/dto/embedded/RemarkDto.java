package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * RemarkDto.
 * 
 * @author jiashuo.
 * 
 */
public class RemarkDto extends BaseDto {
    /**
     * title.
     */
    private String title;
    /**
     * description.
     */
    private List<String> description;

    /**
     * get description.
     * 
     * @return description.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * set description.
     * 
     * @param description
     *            description.
     */
    public void setDescription(List<String> description) {
        this.description = description;
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

}
