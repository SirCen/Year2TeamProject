package application;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * This is the class for the OrderList from the menu.
 */
public class OrderList {
  private HashMap<String, Integer> orderList = new HashMap<>(); //Integer ID and Integer Quantity
  private int itemLimit = 25; //The limit of each individual item

  /**
   * Default constructor for the class.
   */
  public OrderList() {
  }


  /**
   * A method to get the size of the hashmap; which shows how many different menu items are
   * present within the hashmap.
   *
   * @return Numbers of unique items selected
   */
  public int getSize() {
    return orderList.size();
  }

  /**
   * A method to get the quantity of a particular menu item by getting the value
   * associated with the id in the hashmap.
   *
   * @param id Id of the order item
   * @return Number of that particular item selected
   */
  public String getQuantity(String id) {
    if (orderList.get(id) != null) {
      return orderList.get(id).toString();
    } else {
      return "0";
    }
  }

  /**
   * This method adds the selected items to a hashmap; keeping track of which menu items
   * are added and their quantity.
   *
   * @param id Id of the order item
   * @throws SQLException if there is an error in a SQL statement
   * @throws TooManyItemsException if there are too many items overall or of one particular item
   *                               within the order
   */
  public void addItem(String id) throws TooManyItemsException, SQLException {
    //String id = Application.Main.select("SELECT menu_id FROM menu WHERE name = " +
    // name).getString("menu_id");
    orderList.putIfAbsent(id, 0);
    if (orderList.get(id) < itemLimit && checkTotalQuantity() < itemLimit * 2) {
      orderList.put(id, orderList.get(id) + 1);
    } else {
      throw new TooManyItemsException("Overall limit: 50    Individual limit: 25");
    }
  }

  /**
   * This method ensures the total number of items selected does not exceed the limit.
   *
   * @return the number of items selected
   */
  public int checkTotalQuantity() {
    int count = 0;
    for (int i : orderList.values()) {
      count += i;
    }
    return count;
  }

  /**
   * Removes an item from the order by decrementing it's quantity.
   * If the item reaches 0 quantity then it is removed from the order entirely
   *
   * @param id the ID of the menu item
   * @throws EmptyOrderException Thrown when an item cannot be removed from the order
   */
  public void removeItem(String id) throws EmptyOrderException {
    if (orderList.containsKey(id)) {
      orderList.put(id, orderList.get(id) - 1);

      if (orderList.get(id) <= 0) {
        orderList.remove(id);
      }
    } else {
      throw new EmptyOrderException("Item cannot be removed");
    }
  }

  /**
   * Submits an order to the waiters.
   *
   * @throws EmptyOrderException if the order is empty upon submission.
   */
  public void submitItems() throws EmptyOrderException {
    if (getSize() > 0) {
      //code to pass the order to the waiters database
      return;
    } else {
      throw new EmptyOrderException("No items to submit.");
    }
  }
}