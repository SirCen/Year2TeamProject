package application.models;

import java.util.Objects;

public class OrderItemModel {
  private int orderItemsId;
  private int orderId;
  private int menuId;
  private String adjustments;
  private String status;
  private String name;

  /**
   * The model for an order item.
   * @param orderItemsId The id of the order item
   * @param orderId      The order id
   * @param menuId       The menu id
   * @param adjustments  The adjustments of the order item
   * @param status       The status of the order item
   * @param name         The name of an order item
   */
  public OrderItemModel(int orderItemsId, int orderId, int menuId, String adjustments,
                        String status, String name) {
    this.orderItemsId = orderItemsId;
    this.orderId = orderId;
    this.menuId = menuId;
    this.adjustments = adjustments;
    this.status = status;
    this.name = name;
  }

  public int getOrder_items_id() {
    return orderItemsId;
  }

  public int getOrder_id() {
    return orderId;
  }

  public int getMenu_id() {
    return menuId;
  }

  public String getAdjustments() {
    return adjustments;
  }

  public String getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemModel that = (OrderItemModel) o;
    return orderItemsId == that.orderItemsId
        && orderId == that.orderId
        && menuId == that.menuId
        && Objects.equals(adjustments, that.adjustments)
        && Objects.equals(status, that.status)
        && Objects.equals(name, that.name);
  }


}
