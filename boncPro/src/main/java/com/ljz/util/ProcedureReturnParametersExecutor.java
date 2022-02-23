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
public class ProcedureReturnParametersExecutor {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private TransactionTemplate template;

  public boolean save() {
    String insertSql = "INSERT INTO `order`(`uuid`, `number`, `name`, `type`) VALUES ('d14f8eec-ec1a-11ea-98de-1234567', '20200901000003', '单据3', NULL)";
    return jdbcTemplate.update(insertSql) > 0;
  }

  public Order getByUuid(String uuid) {
    ProcedureReturnParametersTransactionCallback callback = new ProcedureReturnParametersTransactionCallback(
        uuid);
    return template.execute(callback);
  }

  class ProcedureReturnParametersTransactionCallback implements TransactionCallback<Order> {
    private String uuid;

    public ProcedureReturnParametersTransactionCallback(String uuid) {
      super();
      this.uuid = uuid;
    }

    @Override
    public Order doInTransaction(TransactionStatus transactionStatus) {
      return jdbcTemplate.execute(new CallableStatementCreator() {

        @Override
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
          String procedure = "{call Get_Order_By_Uuid(?,?,?,?)}";
          CallableStatement cs = con.prepareCall(procedure);
          cs.setString(1, uuid);
          cs.registerOutParameter(2, java.sql.Types.VARCHAR);
          cs.registerOutParameter(3, java.sql.Types.VARCHAR);
          cs.registerOutParameter(4, java.sql.Types.VARCHAR);
          return cs;
        }

      }, new CallableStatementCallback<Order>() {
        @Override
        public Order doInCallableStatement(CallableStatement cs)
            throws SQLException, DataAccessException {
          cs.execute();
          String number = cs.getString(2);
          String name = cs.getString(3);
          String msg = cs.getString(4);
          if (msg != null) {
            throw new RuntimeException(msg);
          }

          Order order = new Order();
          order.setNumber(number);
          order.setName(name);

          return order;
        }
      });
    }
  }

}

