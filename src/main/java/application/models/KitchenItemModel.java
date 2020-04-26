package application.models;

import java.util.Objects;

public class KitchenItemModel {
  private int tableNum;
  private int orderId;
  private String timeOrdered;
  private OrderItemModel item;

  /**
   * The model for a kitchen item.
   * @param tableNum    The table number
   * @param orderId     The order ID
   * @param timeOrdered The time ordered
   * @param item        An order item
   */
  public KitchenItemModel(int tableNum, int orderId, String timeOrdered, OrderItemModel item) {
    this.tableNum = tableNum;
    this.orderId = orderId;
    this.timeOrdered = timeOrdered;
    this.item = item;
  }

  public int getTable_num() {
    return tableNum;
  }

  public int getOrder_id() {
    return orderId;
  }

  public String getTime_ordered() {
    return timeOrdered;
  }

  public OrderItemModel getItem() {
    return item;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KitchenItemModel that = (KitchenItemModel) o;
    return tableNum == that.tableNum
        && orderId == that.orderId
        && Objects.equals(timeOrdered, that.timeOrdered)
        && Objects.equals(item, that.item);
  }
}
