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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.cnnic.rdap.bean.User;
import cn.cnnic.rdap.common.util.MD5Encryption;
import cn.cnnic.rdap.dao.IdentityCheckDao;
import cn.cnnic.rdap.service.IdentityCheckService;

/**
 * 
 * @author wang
 */
@Service("identityCheckService")
public class IdentityCheckServiceImpl implements IdentityCheckService {
        /**
	 * rdap IdentityCheckService service
	 */
    @Autowired
    private IdentityCheckDao idcDao;

    @Override
    public User IdentityCheckService(String userId, String userPwd) {
         /**
	 * get user indentity by user id and user password
	 */       
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPwd)) {
            return null;
        }
        User user = idcDao.checkUserId(userId);
        if(null == user){
            return null;
        }
        if (MD5Encryption.Encryption(userPwd).equalsIgnoreCase(
                user.getUserPwd())) {
            user.setUserType(User.UserType.Cerfications);
            return user;
        }
        return null;
    }
}
