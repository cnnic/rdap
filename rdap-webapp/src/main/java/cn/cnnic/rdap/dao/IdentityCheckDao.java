/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.dao;

import cn.cnnic.rdap.bean.User;

/**
 *
 * @author wang
 */
public interface IdentityCheckDao {
    /**
     * check user id and get use password
     * @param userId
     *         userId String
     */    
    User checkUserId(String userId);
    
}
