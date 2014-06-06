/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    @Autowired
    private IdentityCheckDao idcDao;

    @Override
    public User IdentityCheckService(String userId, String userPwd) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPwd)) {
            return null;
        }
        User user = idcDao.checkUserId(userId);
        if(null == user){
            return null;
        }
        if (MD5Encryption.encryption(userPwd).equalsIgnoreCase(
                user.getUserPwd())) {
            user.setUserType(User.UserType.Cerfications);
            return user;
        }
        return null;
    }
}
