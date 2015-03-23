package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * EventDto.
 * 
 * @author jiashuo.
 * 
 */
public class EventDto extends BaseDto {
    /**
     * eventAction.
     */
    private String eventAction;
    /**
     * eventActor.
     */
    private String eventActor;
    /**
     * eventDate.
     */
    private String eventDate;

    /**
     * get eventAction.
     * 
     * @return eventAction.
     */
    public String getEventAction() {
        return eventAction;
    }

    /**
     * set eventAction.
     * 
     * @param eventAction
     *            eventAction.
     */
    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    /**
     * get eventActor.
     * 
     * @return eventActor.
     */
    public String getEventActor() {
        return eventActor;
    }

    /**
     * set eventActor.
     * 
     * @param eventActor
     *            eventActor.
     */
    public void setEventActor(String eventActor) {
        this.eventActor = eventActor;
    }

    /**
     * get eventDate.
     * 
     * @return eventDate.
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * set eventDate.
     * 
     * @param eventDate
     *            eventDate.
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
