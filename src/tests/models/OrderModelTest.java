package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import application.models.OrderItemModel;
import application.models.OrderModel;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the MenuItemModel.
 */
public class OrderModelTest {
  private int orderId;
  private int tableNum;
  private String timeOrdered;
  private int waiterId;
  private String status;
  private boolean paid;
  private ArrayList<OrderItemModel> items;
  private OrderModel orderModel;

  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    orderId = 5;
    tableNum = 10;
    timeOrdered = "15:00";
    waiterId = 1;
    status = "Ready";
    paid = false;
    OrderItemModel orderItemModel = new OrderItemModel(5, orderId, 3,
        "no sauce", status, "beef");
    items = new ArrayList<>();
    items.add(orderItemModel);
    orderModel = new OrderModel(orderId, tableNum, timeOrdered, waiterId, status, paid, items);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testOrderConstructor() {
    orderModel = new OrderModel(orderId, tableNum, timeOrdered, waiterId, status, paid, items);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testGetOrderId() {
    assertEquals("Correct order ID not obtained", orderId, orderModel.getOrder_id());
  }

  @Test
  public void testGetTableNum() {
    assertEquals("Correct table not obtained", tableNum, orderModel.getTable_num());
  }

  @Test
  public void testGetTimeOrdered() {
    assertEquals("Correct time not obtained", timeOrdered, orderModel.getTime_ordered());

  }

  @Test
  public void testGetWaiterID() {
    assertEquals("Correct waiter ID not obtained", waiterId, orderModel.getStaffId());
  }

  @Test
  public void testGetStatus() {
    assertEquals("Correct status not obtained", status, orderModel.getStatus());
  }

  @Test
  public void testGetItems() {
    assertSame("Correct items not obtained", items, orderModel.getItems());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    OrderModel orderModel2 = new OrderModel(orderId, 27, timeOrdered,
        waiterId, "Cooking", false, items);
    OrderModel orderModel3 = new OrderModel(orderId, tableNum, timeOrdered,
        waiterId, status, false, items);
    assertNotEquals("Models are equal", orderModel, orderModel2);
    assertEquals("Models are unequal", orderModel, orderModel3);
  }
}