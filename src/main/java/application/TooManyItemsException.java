package application;

public class TooManyItemsException extends Exception {
  private static final long serialVersionUID = 1L; //default serialVersionUID

  public TooManyItemsException(String errorMessage) {
    super(errorMessage);
  }
}
