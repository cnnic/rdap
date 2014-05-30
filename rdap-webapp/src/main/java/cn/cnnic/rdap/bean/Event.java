/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package cn.cnnic.rdap.bean;

import java.util.List;

/**
 * event represents events that have occurred on an instance of an object class
 * .
 * 
 * @author jiashuo
 * 
 */
public class Event extends BaseModel {
    /**
     * a string denoting the reason for the event.
     */
    private String eventAction;
    /**
     * an optional identifier denoting the actor responsible for the event.
     */
    private String eventActor;
    /**
     * the time and date the event occurred,UTC.
     */
    private String eventDate;

    /**
     * links
     */
    private List<Link> links;

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getEventActor() {
        return eventActor;
    }

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
     * 
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
