package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import application.models.OrderItemModel;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the MenuItemModel.
 */
public class OrderItemModelTest {
  private int orderItemsId;
  private int orderId;
  private int menuId;
  private String adjustments;
  private String status;
  private String name;
  private OrderItemModel orderItemModel;


  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    orderItemsId = 10;
    orderId = 5;
    menuId = 10;
    adjustments = "No sauce";
    status = "Ready";
    name = "Beef";
    orderItemModel = new OrderItemModel(orderItemsId, orderId, menuId, adjustments, status, name);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testOrderItemConstructor() {
    new OrderItemModel(orderItemsId, orderId, menuId, adjustments, status, name);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testGetOrderItemsId() {
    assertEquals("Correct table not obtained", orderItemsId,
        orderItemModel.getOrder_items_id());
  }

  @Test
  public void testGetOrderID() {
    assertEquals("Correct order ID not obtained", orderId, orderItemModel.getOrder_id());
  }

  @Test
  public void testGetMenuId() {
    assertEquals("Correct menu ID not obtained", menuId, orderItemModel.getMenu_id());
  }

  @Test
  public void testGetAdjustments() {
    assertSame("Correct adjustments not obtained", adjustments,
        orderItemModel.getAdjustments());
  }

  @Test
  public void testGetStatus() {
    assertSame("Correct status not obtained", status, orderItemModel.getStatus());
  }

  @Test
  public void testGetName() {
    assertSame("Correct name not obtained", name, orderItemModel.getName());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    OrderItemModel orderItemModel2 = new OrderItemModel(orderItemsId, 25, menuId,
        adjustments, "preparation", name);
    OrderItemModel orderItemModel3 = new OrderItemModel(orderItemsId, orderId, menuId,
        adjustments, status, name);
    assertNotEquals("Models are equal", orderItemModel, orderItemModel2);
    assertEquals("Models are unequal", orderItemModel, orderItemModel3);
  }
}