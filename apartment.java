import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class apartment
{
  protected apartment.dataContainer data = new apartment.dataContainer();
  protected apartment.wallContainer wallsData = new apartment.wallContainer();

  public static apartment readApartmentFromFile(String docFile)
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      factory.setValidating(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);
      DocumentBuilder builder = factory.newDocumentBuilder();

      builder.setErrorHandler(new XMLErrorHandler());
      FileInputStream DOC = new FileInputStream(docFile);
      return newDocument(builder, DOC);
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
      return null;
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }return null;
  }

  public apartment roundWalls()
  {
    return new roundedWallsApartment(this);
  }
  private static apartment newDocument(DocumentBuilder builder, InputStream DOC) {
    try {
      Document doc = builder.parse(DOC);
      Element root = doc.getDocumentElement();

      return new parsedApartment(doc);
    }
    catch (SAXException e) {
      e.printStackTrace();
      return null;
    }
    catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public apartment.dataContainer getData()
  {
    return this.data;
  }
  public apartment.wallContainer getWalls() {
    return this.wallsData;
  }

  public class dataContainer
  {
    public double[] size = new double[2];
    public double[] offsetX = new double[2];
    public double[] offsetY = new double[2];
    public double[][] indent;

    public dataContainer()
    {
    }
  }

  public class wallContainer
  {
    public double[][][] walls;
    public double[][] wallParametrs;
    public double[][][] reverseWalls;

    public wallContainer()
    {
    }
  }
}

