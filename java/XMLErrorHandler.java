import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

class XMLErrorHandler
  implements ErrorHandler
{
  public void warning(SAXParseException e)
  {
    e.printStackTrace();
    System.exit(1);
  }
  public void fatalError(SAXParseException e) {
    e.printStackTrace();
    System.exit(1);
  }
  public void error(SAXParseException e) {
    e.printStackTrace();
    System.exit(1);
  }
}

