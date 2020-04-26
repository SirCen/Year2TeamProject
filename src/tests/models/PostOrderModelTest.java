package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import application.models.PostOrderModel;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the MenuItemModel.
 */
public class PostOrderModelTest {
  private int orderId;
  private String status;
  private int tableNum;
  private boolean paid;
  private PostOrderModel postOrderModel;

  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    orderId = 2;
    status = "Ready";
    tableNum = 5;
    postOrderModel = new PostOrderModel(orderId, status, tableNum, paid);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testPostOrderConstructor() {
    new PostOrderModel(orderId, status, tableNum, paid);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testGetOrderId() {
    assertEquals("Correct order ID not obtained", orderId, postOrderModel.getOrder_id());
  }

  @Test
  public void testGetStatus() {
    assertEquals("Correct status not obtained", status, postOrderModel.getStatus());
  }

  @Test
  public void testGetTableNum() {
    assertEquals("Correct table not obtained", tableNum, postOrderModel.getTable_num());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    PostOrderModel postOrderModel2 = new PostOrderModel(18, status, tableNum, paid);
    PostOrderModel postOrderModel3 = new PostOrderModel(orderId, status, tableNum, paid);
    assertNotEquals("Models are equal", postOrderModel, postOrderModel2);
    assertEquals("Models are unequal", postOrderModel, postOrderModel3);
  }
}