package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import application.models.KitchenItemModel;
import application.models.OrderItemModel;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the KitchenModel.
 */
public class KitchenModelTest {
  private int tableNum;
  private int orderId;
  private String timeOrdered;
  private OrderItemModel item;
  private KitchenItemModel kitchenItemModel;


  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    tableNum = 2;
    orderId = 5;
    timeOrdered = "10:00";
    item = new OrderItemModel(5, 5, 2, "no sauce",
        "ready", "chicken");
    kitchenItemModel = new KitchenItemModel(tableNum, orderId, timeOrdered, item);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testKitchenConstructor() {
    new KitchenItemModel(tableNum, orderId, timeOrdered, item);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testTableNum() {
    assertEquals("Correct table not obtained", tableNum, kitchenItemModel.getTable_num());
  }

  @Test
  public void testOrderID() {
    assertEquals("Correct order ID not obtained", orderId, kitchenItemModel.getOrder_id());
  }

  @Test
  public void testGetTimeOrdered() {
    assertSame("Correct time not obtained", timeOrdered,
        kitchenItemModel.getTime_ordered());
  }

  @Test
  public void testGetOrderItem() {
    assertEquals("Correct OrderItem not obtained", item, kitchenItemModel.getItem());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    KitchenItemModel kitchenItemModel2 =
        new KitchenItemModel(tableNum, 21, "11:00", item);
    KitchenItemModel kitchenItemModel3 =
        new KitchenItemModel(tableNum, orderId, timeOrdered, item);
    assertNotEquals("Models are equal", kitchenItemModel, kitchenItemModel2);
    assertEquals("Models are unequal", kitchenItemModel, kitchenItemModel3);
  }
}