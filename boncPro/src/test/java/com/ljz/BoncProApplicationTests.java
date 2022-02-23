/*package com.ljz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ljz.model.Order;
import com.ljz.util.ProcedureReturnListExecutor;
import com.ljz.util.ProcedureReturnParametersExecutor;
import com.ljz.util.TestProduceReturn;
@SpringBootTest
class BoncProApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
    private ProcedureReturnParametersExecutor procedure1;
    @Autowired
    private ProcedureReturnListExecutor procedure2;
    
    @Autowired
    private TestProduceReturn test;

	  @Test
	  public void test() {
	    procedure1.save();
	  }

	  @Test
	  public void testGetByUuid() {
	    Order order = procedure1.getByUuid("d14f8eec-ec1a-11ea-98de-1234567");
	    assertNotNull(order);
	    assertEquals(order.getNumber(), "20200901000003");
	  }
	  
	  @Test
	  public void testGetSql() {
	    Order order = test.getSql("ECT");
	    System.out.println(order.getName());
	  }

	  @Test
	  public void testGetAll() {
	    List<Order> orderList = procedure2.getAll();
	    assertEquals(3, orderList.size());
	  }

}
*/