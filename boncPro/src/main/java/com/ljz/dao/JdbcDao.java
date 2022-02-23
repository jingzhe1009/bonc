package com.ljz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcDao {
	
	private Connection getConnection(String driver, String url, String username, String password) {
        
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
	
	
	public void insertDb(String sql,String driver, String url, String username, String password) {
		
	   Connection conn = null;
	   Statement stmt = null;
	   try{
		  conn =getConnection(driver, url, username, password);
	      stmt = conn.createStatement();
	      stmt.executeUpdate(sql);
	   }catch(SQLException se){
	      se.printStackTrace();
	   }catch(Exception e){
	      e.printStackTrace();
	   }finally{
	      try{
	         if(stmt!=null) {
	            conn.close();
	         }
	      }catch(SQLException se){
	      }
	      try{
	         if(conn!=null) {
	            conn.close();
	         }
	      }catch(SQLException se){
	      }
	   }
	}

}
