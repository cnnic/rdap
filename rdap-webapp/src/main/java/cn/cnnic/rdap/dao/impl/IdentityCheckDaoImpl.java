/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.dao.impl;

import cn.cnnic.rdap.dao.IdentityCheckDao;
import cn.cnnic.rdap.bean.User;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 *
 * @author wang
 */
public class IdentityCheckDaoImpl implements IdentityCheckDao{
    
    private JdbcTemplate jdbcTemplate;
    private final String sql = "select USER_ID,USER_PWD from RDAP_IDENTITY_USER where USER_ID=?"; 
    private User user;
    @Override
    public User checkUserId(final String userId){			
                List<User> result =  jdbcTemplate.query(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(
							Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql);
                                                ps.setLong(1, Long.parseLong(userId));
                                                return ps;
                                        }
                                },new RowMapper<User>(){
                                    public User mapRow(ResultSet rs, int rowNum)
                                            throws SQLException {
                                        user.setUserId(rs.getLong("USER_ID"));
                                        user.setUserPwd(rs.getString("USER_PWD"));
                                            return user;
                                    }
                                }); 
                return user;
    }
    
    public IdentityCheckDaoImpl(){
        user = new User();
    }
  
}
