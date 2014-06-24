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

    /**
     * type of user.
     * 
     * @author jiashuo
     * 
     */
    public enum UserType {

        /**
         * @param anonymous
         *            defautl anonymous.
         * @param cerfications
         *            default cerfications.
         */
        Anonymous("anonymous"), Cerfications("cerfications");
        /**
         * type of user.
         */
        private final String userType;

        /**
         * constructor.
         * 
         * @param userType
         *            parameter to construct.
         */
        private UserType(String userType) {

            this.userType = userType;

        };

    };

    /**
     * type of user.
     */
    private UserType userType;

    /**
     * get type of user.
     * 
     * @return userType object.
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * set userType object.
     * 
     * @param userType
     *            userType to set.
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * long number of userId.
     */
    private long userId;

    /**
     * get id of user.
     * 
     * @return long number of userId.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set user id.
     * 
     * @param userId
     *            long number to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * password of user.
     */
    private String userPwd;

    /**
     * set user password.
     * 
     * @param userPwd
     *            user password.
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * get user password.
     * 
     * @return user password string.
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * constructor.
     */
    public User() {
        userType = UserType.Anonymous;
        userId = 0;
        userPwd = "";
    }
}