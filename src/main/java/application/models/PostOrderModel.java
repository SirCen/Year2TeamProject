package application.models;

import java.util.Objects;

public class PostOrderModel {
  private int orderId;
  private String status;
  private int tableNum;
  private boolean paid;

  /**
   * The model for a post-order display.
   * @param orderId  The order id
   * @param status   The order status
   * @param tableNum The table number
   * @param paid     Whether the order has been paid for or not
   */
  public PostOrderModel(int orderId, String status, int tableNum, boolean paid) {
    this.orderId = orderId;
    this.status = status;
    this.tableNum = tableNum;
    this.paid = paid;
  }

  public int getOrder_id() {
    return orderId;
  }

  public String getStatus() {
    return status;
  }

  public int getTable_num() {
    return tableNum;
  }

  public boolean getPaid() {
    return paid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostOrderModel that = (PostOrderModel) o;
    return orderId == that.orderId
        && tableNum == that.tableNum
        && Objects.equals(status, that.status);
  }
}
