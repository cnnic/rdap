/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.service;

import cn.cnnic.rdap.bean.User;


/**
 *
 * @author wang
 */
public interface IdentityCheckService {
    
    User IdentityCheckService(final String userId,final String userPwd);
    
}
