package controller;

import static org.junit.Assert.assertEquals;

import application.controllers.MenuController;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;


public class MenuControllerTest {
  private String order;
  private int tableNum;
  private MenuController menuController;

  /**
   * Setup method using a mock order for testing purposes.
   */
  @Before
  public void setup() {
    order = "{\"tableNum\":5,\"order\":\"{\"1\":{\"name\":\"Food item 1\",\"{}\":2},"
        + "\"2\":{\"name\":\"Food item 2\",\"{}\":2,"
        + "\"{No vegetables}\":5},\"3\":{\"name\":\"Food item 3\",\"{}\":1}}\"}";
    tableNum = 23;
    menuController = new MenuController();
  }

  /*
  @Test
  public void submitOrder() throws SQLException {
    assertEquals("redirect:/", menuController.submitOrder(order));
  }
   */
}
