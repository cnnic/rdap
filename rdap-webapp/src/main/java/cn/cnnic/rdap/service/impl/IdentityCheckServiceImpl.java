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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.cnnic.rdap.bean.User;
import cn.cnnic.rdap.common.util.MD5Encryption;
import cn.cnnic.rdap.dao.IdentityCheckDao;
import cn.cnnic.rdap.service.IdentityCheckService;

/**
 * check user Authentication.
 * 
 * Requirement from  
 * http://tools.ietf.org/html/draft-ietf-weirds-rdap-sec-06#section-3.1 .
 *
 * Provide basic authentication to user and its password.
 * 
 * @author wang
 */
@Service("identityCheckService")
public class IdentityCheckServiceImpl implements IdentityCheckService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(IdentityCheckServiceImpl.class);
    /**
     * User Identity Dao.
     */
    @Autowired
    private IdentityCheckDao idcDao;
    /**
     * check user authorization and return user.
     * 
     * @param userId
     *          user id. 
     * @param userPwd
     *          user password.
     * 
     * @return user object.
     */   
    @Override
    public User identityCheckService(String userId, String userPwd) {
        LOGGER.debug("IdentityCheckService userId:" + userId 
                                + ", userPwd:" + userPwd);  
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPwd)) {
            return null;
        }
        User user = idcDao.checkUserId(userId);
        LOGGER.debug("IdentityCheckService user:" + user);  
        if (null == user) {
            return null;
        }
        if (MD5Encryption.encryption(userPwd).equalsIgnoreCase(
                user.getUserPwd())) {
            LOGGER.debug("IdentityCheckService user " + userId 
                                                + " authorized");  
            user.setUserType(User.UserType.Cerfications);
            return user;
        }
        LOGGER.debug("IdentityCheckService user " + userId 
                + " unauthorized");  
        return null;
    }
}

