package starwag.assignment.exception;

public class NoSuchSectionPresentException extends RuntimeException {
  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;

  public NoSuchSectionPresentException(String message) {
    this.message = message;
  }

  public NoSuchSectionPresentException() {

  }
}
