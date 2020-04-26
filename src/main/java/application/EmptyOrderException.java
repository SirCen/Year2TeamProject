package application;

public class EmptyOrderException extends Exception {
  private static final long serialVersionUID = 1L; //default serialVersionUID

  public EmptyOrderException(String errorMessage) {
  }
}
