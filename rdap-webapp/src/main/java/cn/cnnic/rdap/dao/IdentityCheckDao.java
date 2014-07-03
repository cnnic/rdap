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
     * check the user id.
     * 
     * @param userId
     *            user id.
     * @return user object.
     */
    User checkUserId(String userId);

}
