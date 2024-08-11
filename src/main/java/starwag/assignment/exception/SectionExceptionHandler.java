package starwag.assignment.exception;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import starwag.assignment.controller.SectionController;

@ControllerAdvice
public class SectionExceptionHandler {

  private static final Log LOG = LogFactory.getLog(SectionController.class);

  @ExceptionHandler(value = NoSuchSectionPresentException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ErrorMessage handleNoSuchSectionPresentException(NoSuchSectionPresentException ex) {
    LOG.error("NoSuchSectionPresentException:", ex);
    return new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(value = RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorMessage handleRuntimeException(RuntimeException ex) {
    LOG.error("RuntimeException: ", ex);
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error While Request Processing ");
  }

  @ExceptionHandler(value = SQLException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ErrorMessage handleSQLException(RuntimeException ex) {
    LOG.error("SQLException: ", ex);
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error While Request Processing ");
  }
}
