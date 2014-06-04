/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.bean;

/**
 *
 * @author wang
 */
public class User extends BaseModel {
    

    public enum UserType{
        
    Anonymous("anonymous"),Cerfications("cerfications");
    private final String userType;
    
    private UserType(String userType ){
        
        this.userType = userType;
        
    };
    
    };
    
    private UserType userType;
    
    public UserType getUserType(){
        
           return userType;
           
    }
    
    public void setUserType(UserType userType){
        
           this.userType = userType;
           
    }
    
    private long userId;
    
    public long getUserId(){
        
           return userId;
           
    }
    
    public void setUserId(long userId){
        
           this.userId = userId;
           
    }
    
    private String userPwd;
    
    public void setUserPwd(String userPwd){
        
        this.userPwd = userPwd;
        
    }
    
    public String getUserPwd(){
        return userPwd;
    }
    
    public User(){
        
           userType = UserType.Anonymous;
           userId = 0;
           userPwd = "";
    }  
}