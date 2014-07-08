/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        LOGGER.info("IdentityCheckService userId:" + userId 
                                + ", userPwd:" + userPwd);  
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPwd)) {
            return null;
        }
        User user = idcDao.checkUserId(userId);
        LOGGER.info("IdentityCheckService user:" + user);  
        if (null == user) {
            return null;
        }
        if (MD5Encryption.encryption(userPwd).equalsIgnoreCase(
                user.getUserPwd())) {
            LOGGER.info("IdentityCheckService user " + userId 
                                                + " authorized");  
            user.setUserType(User.UserType.Cerfications);
            return user;
        }
        LOGGER.info("IdentityCheckService user " + userId 
                + " unauthorized");  
        return null;
    }
}
