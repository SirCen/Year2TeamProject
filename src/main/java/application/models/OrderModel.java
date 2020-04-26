package application.models;

import java.util.ArrayList;
import java.util.Objects;

public class OrderModel {
  private int orderId;
  private int tableNum;
  private String timeOrdered;
  private int staffId;
  private String status;
  private boolean paid;
  private ArrayList<OrderItemModel> items;

  /**
   * The model for an order.
   * @param orderId     The order id
   * @param tableNum    The table number
   * @param timeOrdered The time ordered
   * @param staffId     The waiter id
   * @param status      The status of the order
   * @param paid        Whether or not the order has been paid for
   * @param items       The items in the order
   */
  public OrderModel(int orderId, int tableNum, String timeOrdered, int staffId, String status, boolean paid,
                    ArrayList<OrderItemModel> items) {
    this.orderId = orderId;
    this.tableNum = tableNum;
    this.timeOrdered = timeOrdered;
    this.staffId = staffId;
    this.status = status;
    this.paid = paid;
    this.items = items;
  }

  public int getOrder_id() {
    return orderId;
  }

  public int getTable_num() {
    return tableNum;
  }

  public String getTime_ordered() {
    return timeOrdered;
  }

  public int getStaffId() {
    return staffId;
  }

  public String getStatus() {
    return status;
  }

  public boolean getPaid() {
    return paid;
  }

  public ArrayList<OrderItemModel> getItems() {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderModel that = (OrderModel) o;
    return orderId == that.orderId
        && tableNum == that.tableNum
        && staffId == that.staffId
        && Objects.equals(timeOrdered, that.timeOrdered)
        && Objects.equals(status, that.status)
        && paid == that.paid
        && Objects.equals(items, that.items);
  }
}

