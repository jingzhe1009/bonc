package com.ljz.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 * 调用存储过程返回结果集-执行器
 * 
 * @author dongtangqiang
 */
@Component
public class ProcedureReturnListExecutor {
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private TransactionTemplate template;

  public List<Order> getAll() {
    ProcedureReturnListTransactionCallback callback = new ProcedureReturnListTransactionCallback();
    return template.execute(callback);
  }

  class ProcedureReturnListTransactionCallback implements TransactionCallback<List<Order>> {

    @Override
    public List<Order> doInTransaction(TransactionStatus transactionStatus) {
      return jdbcTemplate.execute(new CallableStatementCreator() {

        @Override
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
          String procedure = "{call Get_ALL_Order()}";
          System.out.println(procedure);
          CallableStatement cs = con.prepareCall(procedure);
          return cs;
        }

      }, new CallableStatementCallback<List<Order>>() {
        @Override
        public List<Order> doInCallableStatement(CallableStatement cs)
            throws SQLException, DataAccessException {
          ResultSet rs = cs.executeQuery();

          List<Order> list = new ArrayList<>();
          while (rs.next()) {
            String number = rs.getString(1);
            String name = rs.getString(2);

            Order order = new Order();
            order.setNumber(number);
            order.setName(name);
            list.add(order);
          }

          return list;
        }
      });
    }
  }
}


