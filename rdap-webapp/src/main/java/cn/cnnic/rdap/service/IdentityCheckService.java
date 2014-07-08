/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.service;

import cn.cnnic.rdap.bean.User;


/**
 * check user authorization.
 * 
 * @author wang
 */
public interface IdentityCheckService {
    /**
     * check user authorization by user Id and password.
     * 
     * @param userId
     *            user identity.
     * @param userPwd
     *            user password.
     * @return user or null.
     */
    User identityCheckService(final String userId,final String userPwd);
    
}
