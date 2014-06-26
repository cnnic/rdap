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
package cn.cnnic.rdap.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainSearch;
import cn.cnnic.rdap.bean.DsData;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityAddress;
import cn.cnnic.rdap.bean.EntityTel;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.IPAddress;
import cn.cnnic.rdap.bean.KeyData;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverSearch;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.Notice;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.bean.SecureDns;
import cn.cnnic.rdap.bean.Variant;
import cn.cnnic.rdap.bean.Variants;
import cn.cnnic.rdap.common.util.HiddenColumnUtil;
import cn.cnnic.rdap.service.PolicyControlManager;

/**
 * PolicyControlManager implementation.
 * 
 * @author weijunkai
 * 
 */
@Service
public class PolicyControlManagerImpl implements PolicyControlManager {
    public void setPolicy(final Object model) {
        if (model != null) {
            // HiddenColumnUtil.setHiddenColumnNull(model, strObj);
            // HiddenColumnUtil.setListHiddenColumnNull(model, strObj);

            // notice
            BaseModel objBase = (BaseModel) model;
            final String strNotice = "notice";
            List<Notice> listNotice = objBase.getNotices();
            setListPolicy(listNotice, strNotice);

            final String strDomain = "domain";
            final String strNameserver = "nameServer";

            // nameserver and search
            if (model.getClass() == NameserverSearch.class) {
                HiddenColumnUtil.setListHiddenColumnNull(model, strNameserver);
                NameserverSearch obj = (NameserverSearch) model;
                List<Nameserver> listNS = obj.getNameserverSearchResults();
                setListNameserverPolicy(listNS, strNameserver);
            }
            if (model.getClass() == Nameserver.class) {
                Nameserver objModel = (Nameserver) model;
                setNameserverPolicy(objModel, strNameserver);
            }
            // domain and search
            if (model.getClass() == Domain.class) {
                Domain objModel = (Domain) model;
                setDomainPolicy(objModel, strDomain);
            }
            if (model.getClass() == DomainSearch.class) {
                DomainSearch obj = (DomainSearch) model;
                List<Domain> listDomain = obj.getDomainSearchResults();
                setListDomainPolicy(listDomain, strDomain);
            }
            // autnum query
            final String strAutnum = "autnum";
            if (model.getClass() == Autnum.class) {
                BaseModel objModel = (BaseModel) model;
                setFourObjectPolicy(objModel);
            }
            // Network query
            final String strNetwork = "network";
            if (model.getClass() == Network.class) {
                BaseModel objModel = (BaseModel) model;
                setFourObjectPolicy(objModel);
                // IpVersion
            }
            // entity query and search
            final String strEntity = "entity";
            if (model.getClass() == Entity.class) {
                Entity objModel = (Entity) model;
                setEntityPolicy(objModel);
            }
        }
        return;
    }

    @Override
    public void setPolicy(final Object model, String strObj) {
        if (model != null && strObj != null) {
            HiddenColumnUtil.setListHiddenColumnNull(model, strObj);

            // notice
            BaseModel objBase = (BaseModel) model;
            final String strNotice = "notice";
            List<Notice> listNotice = objBase.getNotices();
            setListPolicy(listNotice, strNotice);

            final String strDomain = "domain";
            final String strNameserver = "nameServer";

            // nameserver search
            if (model.getClass() == NameserverSearch.class) {
                NameserverSearch obj = (NameserverSearch) model;
                List<Nameserver> listNS = obj.getNameserverSearchResults();
                for (int iObj = 0; iObj < listNS.size(); ++iObj) {
                    setPolicy(listNS.get(iObj), strNameserver);
                }
            }
            // nameserver query
            if (model.getClass() == Nameserver.class) {
                Nameserver objModel = (Nameserver) model;
                setNameserverPolicy(objModel, strNameserver);
            }
            // domain and search
            if (model.getClass() == Domain.class) {
                Domain objModel = (Domain) model;
                setDomainPolicy(objModel, strDomain);
            }
            if (model.getClass() == DomainSearch.class) {
                DomainSearch obj = (DomainSearch) model;
                List<Domain> listDomain = obj.getDomainSearchResults();
                setListDomainPolicy(listDomain, strDomain);
            }
            // autnum query
            final String strAutnum = "autnum";
            if (model.getClass() == Autnum.class) {
                BaseModel objModel = (BaseModel) model;
                setFourObjectPolicy(objModel);
            }
            // Network query
            final String strNetwork = "network";
            if (model.getClass() == Network.class) {
                BaseModel objModel = (BaseModel) model;
                setFourObjectPolicy(objModel);
                // IpVersion
            }
            // entity query and search
            final String strEntity = "entity";
            if (model.getClass() == Entity.class) {
                Entity objModel = (Entity) model;
                setEntityPolicy(objModel);
            }
            // NSIpAdrress query            
            if (model.getClass() == IPAddress.class) {
                final String strObject = "NameserverIp";
                IPAddress objModel = (IPAddress) model;
                //finish
            }
        }
        return;
    }

    public void setEntityPolicy(final Entity objModel) {
        final String strObj = "entity";
        HiddenColumnUtil.setListHiddenColumnNull(objModel, strObj);

        List<EntityAddress> listEntityAddr = objModel.getAddresses();
        setListPolicy(listEntityAddr,"entityAddress");

        List<Event> listEvent = objModel.getAsEventActor();
        setListPolicy(listEvent,"event");
        
        List<Autnum> listAutnum = objModel.getAutnums();
        setListPolicy(listAutnum,"autnums");
        
        List<Network> listNetwork = objModel.getNetworks();
        setListPolicy(listNetwork,"networks");
        
        List<PublicId> listPublicId = objModel.getPublicIds();
        setListPolicy(listPublicId,"publicIds");
        
        List<EntityTel> listEntityTel = objModel.getTelephones();
        setListPolicy(listEntityTel,"telephones");

        setFourObjectPolicy(objModel);
    }

    /**
     * 
     * @param objModel
     */
    public void setNetworkPolicy(final Domain objModel) {
        final String strNetwork = "network";
        Network obj = objModel.getNetwork();
        if (obj != null) {
            HiddenColumnUtil.setListHiddenColumnNull(obj, strNetwork);
        }
    }

    /**
     * 
     * @param objModel
     * @param strObj
     */
    public void setDomainPolicy(final Domain objModel, final String strObj) {
        HiddenColumnUtil.setListHiddenColumnNull(objModel, strObj);
        // domain publicIds
        final String strPublicIds = "publicIds";
        List<PublicId> listPubIds = objModel.getPublicIds();
        setListPolicy(listPubIds, strPublicIds);
        // domain secDns
        setSecureDnsPolicy(objModel);
        // domain variant
        final String strVariants = "variants";
        List<Variants> listVars = objModel.getVariants();
        if (listVars != null) {
            setListPolicy(listVars, strVariants);
            for (int iVar = 0; iVar < listVars.size(); ++iVar) {
                Variants vars = listVars.get(iVar);
                List<Variant> listVar = vars.getVariantNames();
                final String strVariant = "variant";
                setListPolicy(listVar, strVariant);
            }
        }
        // domain ns
        final String strNameservers = "nameServers";
        List<Nameserver> listNs = objModel.getnameServers();
        setListNameserverPolicy(listNs, strNameservers);
        // domain four other
        setFourObjectPolicy(objModel);
    }

    /**
     * 
     * @param objModel
     */
    public void setSecureDnsPolicy(final Domain objModel) {
        final String strSecureDns = "secureDns";
        SecureDns obj = objModel.getSecureDns();
        if (obj != null) {
            HiddenColumnUtil.setListHiddenColumnNull(obj, strSecureDns);
            List<DsData> listDs = obj.getDsData();
            final String strDsData = "dsData";
            setListPolicy(listDs,strDsData);
            List<KeyData> listKey = obj.getKeyData();
            final String strKeyData = "KeyData";
            setListPolicy(listKey,strKeyData);
        }
        
    }

    public void setNameserverPolicy(final Nameserver objModel,
            final String strObj) {
        setFourObjectPolicy(objModel);
    }

    public void setListDomainPolicy(final List<Domain> listDomain,
            final String strObj) {
        if (listDomain != null) {
            for (int iNS = 0; iNS < listDomain.size(); ++iNS) {
                Domain objModel = listDomain.get(iNS);
                setDomainPolicy(objModel, strObj);
            }
        }
    }

    //no use
    public void setListNameserverPolicy(final List<Nameserver> listNS,
            final String strObj) {
        if (listNS != null) {
            for (int iNS = 0; iNS < listNS.size(); ++iNS) {
                Nameserver objModel = listNS.get(iNS);
                setNameserverPolicy(objModel, strObj);
            }
        }
    }

    public void setFourObjectPolicy(final BaseModel objModel) {
        final String strEntity = "entities";
        final String strRemark = "remarks";
        final String strLink = "links";
        final String strEvent = "events";
        List<Event> listEvent = null;
        List<Link> listLink = null;
        List<Entity> listEntity = null;
        List<Remark> listRemark = null;
        if (objModel.getClass() == Nameserver.class) {
            Nameserver obj = (Nameserver) objModel;
            listEntity = obj.getEntities();
            listRemark = obj.getRemarks();
            listLink = obj.getLinks();
            listEvent = obj.getEvents();
        }
        if (objModel.getClass() == Domain.class) {
            Domain obj = (Domain) objModel;
            listEntity = obj.getEntities();
            listRemark = obj.getRemarks();
            listLink = obj.getLinks();
            listEvent = obj.getEvents();
        }
        if (objModel.getClass() == Network.class) {
            Network obj = (Network) objModel;
            listEntity = obj.getEntities();
            listRemark = obj.getRemarks();
            listLink = obj.getLinks();
            listEvent = obj.getEvents();
        }
        if (objModel.getClass() == Autnum.class) {
            Autnum obj = (Autnum) objModel;
            listEntity = obj.getEntities();
            listRemark = obj.getRemarks();
            listLink = obj.getLinks();
            listEvent = obj.getEvents();
        }

        setListPolicy(listEntity, strEntity);
        setListPolicy(listRemark, strRemark);
        setListPolicy(listLink, strLink);
        setListPolicy(listEvent, strEvent);
    }

    /**
     * 
     */
    public void setListPolicy(final List<?> listObjs, final String strObj) {
        if (listObjs != null) {
            for (int iObj = 0; iObj < listObjs.size(); ++iObj) {
                setPolicy(listObjs.get(iObj),
                        strObj);
            }
        }
    }
}
