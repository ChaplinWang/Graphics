/**  This exception is thrown when a string describing an DSShape
cannot be parsed..
@author Tim Lambert
  */

public class MalformedShapeException extends Exception{
  
  public MalformedShapeException() {
  }
  
  public MalformedShapeException(String s) {
    super(s);
  }

}
