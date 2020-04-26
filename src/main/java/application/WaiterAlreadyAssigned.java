package application;

/**
 * This exception is called when a waiter tries to assign themselves to an order which has
 * already got a waiter assigned to it.
 * In the future, may need to allow multiple waiters to handle one order.
 */

public class WaiterAlreadyAssigned extends Exception {
  private static final long serialVersionUID = 1L; //default serialVersionUID

  public WaiterAlreadyAssigned(String errorMessage) {
    super(errorMessage);
  }
}
