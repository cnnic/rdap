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
package cn.cnnic.rdap.bean;

/**
 * rdap user object.
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
        /**
         * get user type.
         * 
         * @return String
         *           userType.
         */
        public String getUserType() {
            return this.userType;
        }
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
