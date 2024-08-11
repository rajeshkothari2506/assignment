package starwag.assignment.exception;

import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SectionExceptionHandlerTest {

  @InjectMocks
  SectionExceptionHandler underTest;
  @Mock
  RuntimeException runtimeException;
  @Mock
  NoSuchSectionPresentException noSuchSectionPresentException;
  @Mock
  SQLException sqlException;

  @Test
  public void handleNoSuchSectionPresentExceptionTest() {
    when(noSuchSectionPresentException.getMessage()).thenReturn("noSuchSectionPresentException");
    ErrorMessage errorMessage = underTest.handleNoSuchSectionPresentException(noSuchSectionPresentException);
    Assertions.assertEquals(404, errorMessage.getStatusCode());
  }

  @Test
  public void handleSQLExceptionTest() {
    when(noSuchSectionPresentException.getMessage()).thenReturn("noSuchSectionPresentException");
    ErrorMessage errorMessage = underTest.handleSQLException(noSuchSectionPresentException);
    Assertions.assertEquals(500, errorMessage.getStatusCode());
  }

}
