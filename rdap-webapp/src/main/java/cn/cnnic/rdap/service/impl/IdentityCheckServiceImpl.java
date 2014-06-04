/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.service.impl;

import cn.cnnic.rdap.service.IdentityCheckService;
import cn.cnnic.rdap.bean.User;
import cn.cnnic.rdap.dao.IdentityCheckDao;
import cn.cnnic.rdap.dao.impl.IdentityCheckDaoImpl;
import cn.cnnic.rdap.common.util.MD5Encryption;

/**
 *
 * @author wang
 */
public class IdentityCheckServiceImpl implements IdentityCheckService{
    private IdentityCheckDao idcDao;
    private User user;
    @Override
    public  User IdentityCheckService(String userId,String userPwd){
        user = idcDao.checkUserId(userId);
        if(userPwd.isEmpty())
        {
            user.setUserType(User.UserType.Anonymous);
            return user;
        }
        if(MD5Encryption.encryption(userPwd).equalsIgnoreCase(user.getUserPwd())){
            user.setUserType(User.UserType.Cerfications);   
        }else{
            user.setUserType(User.UserType.Anonymous);
        }
        return user;
    }
    public void IdentityCheckServiceImpl(){
        idcDao = new IdentityCheckDaoImpl();
    } 
}
