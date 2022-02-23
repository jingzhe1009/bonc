package com.ljz.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.ljz.model.Order;

/**
 * 调用存储过程返回参数-执行器
 * 
 * @author dongtangqiang
 */
@Component
public class InsertDbProduceReturn {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private TransactionTemplate template;

  

  public Order getSql(String tableName,String dataSrcAbbr) {
    ProcedureReturnParametersTransactionCallback callback = new ProcedureReturnParametersTransactionCallback(
    		tableName,dataSrcAbbr);
    return template.execute(callback);
  }

  class ProcedureReturnParametersTransactionCallback implements TransactionCallback<Order> {
    private String tableName;
    
    private String dataSrcAbbr;

    public ProcedureReturnParametersTransactionCallback(String tableName,String dataSrcAbbr) {
      super();
      this.tableName = tableName;
      this.dataSrcAbbr = dataSrcAbbr;
    }

    @Override
    public Order doInTransaction(TransactionStatus transactionStatus){
      return jdbcTemplate.execute(new CallableStatementCreator() {

        @Override
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
          //测试用
        /*	String procedure = "{call Produce_test(?,?,?,?)}";
          CallableStatement cs = con.prepareCall(procedure);
          System.out.println(tableName+dataSrcAbbr);
          cs.setString(1, tableName);
          cs.setString(2, dataSrcAbbr);
          cs.registerOutParameter(3, java.sql.Types.VARCHAR);
          cs.registerOutParameter(4, java.sql.Types.VARCHAR);
          return cs;*/
        	//生产
        	System.out.println("dataSrcAbbr=="+dataSrcAbbr+",tableName=="+tableName);
	    	String procedure = "{call pkg_ruku_ddl_util_exe.pro_sp_ddl_exe(?,?,?,?)}";
	        CallableStatement cs = con.prepareCall(procedure);
	        cs.setString(1, dataSrcAbbr);
	        cs.setString(2, tableName);
	        cs.registerOutParameter(3, java.sql.Types.VARCHAR);
	        cs.registerOutParameter(4, java.sql.Types.VARCHAR);
	        return cs;
        }

      }, new CallableStatementCallback<Order>() {
        @Override
        public Order doInCallableStatement(CallableStatement cs)
            throws SQLException, DataAccessException {
          cs.execute();
          String sql1 = cs.getString(3);
          String sql2 = cs.getString(4);
          System.out.println("sql1=="+sql1+",sql2=="+sql2);
          Order order = new Order();
          order.setSql1(sql1);
          order.setSql2(sql2);
          
          return order;
        }
      });
    }
  }

}

