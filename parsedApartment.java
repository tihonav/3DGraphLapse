import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

class parsedApartment extends apartment
{
  private Document doc;

  parsedApartment(Document doc)
  {
    this.doc = doc;
    parseData();
  }
  private void parseData() {
    XPathFactory xpfactory = XPathFactory.newInstance();
    XPath path = xpfactory.newXPath();
    try
    {
      this.data.size[0] = Double.parseDouble(path.evaluate("/apartment/size/width", this.doc));
      this.data.size[1] = Double.parseDouble(path.evaluate("/apartment/size/heigth", this.doc));
      this.data.offsetY[0] = Double.parseDouble(path.evaluate("/apartment/offset/top", this.doc));
      this.data.offsetX[0] = Double.parseDouble(path.evaluate("/apartment/offset/left", this.doc));
      this.data.offsetY[1] = Double.parseDouble(path.evaluate("/apartment/offset/bottom", this.doc));
      this.data.offsetX[1] = Double.parseDouble(path.evaluate("/apartment/offset/right", this.doc));
      createWalls(path);
    }
    catch (XPathExpressionException e)
    {
      e.printStackTrace();
    }
  }

  private void createWalls(XPath path)
    throws XPathExpressionException
  {
    int wallsAmount = Integer.parseInt(path.evaluate("count(/apartment/wall)", this.doc));
    this.wallsData.walls = new double[wallsAmount][][];
    this.wallsData.wallParametrs = new double[wallsAmount][2];

    double heigth = 0.0D;
    if (Integer.parseInt(path.evaluate("count(/apartment/heigth)", this.doc)) > 0) {
      heigth = Double.parseDouble(path.evaluate("/apartment/heigth", this.doc));
    }

    for (Integer wall = Integer.valueOf(1); wall.intValue() <= wallsAmount; wall = Integer.valueOf(wall.intValue() + 1)) {
      String WALL = "/apartment/wall[" + wall.toString() + "]";

      int pointsAmount = Integer.parseInt(path.evaluate("count(" + WALL + "/x)", this.doc));
      this.wallsData.wallParametrs[(wall.intValue() - 1)][0] = Double.parseDouble(path.evaluate(WALL + "/width", this.doc));

      if (heigth == 0.0D) {
        this.wallsData.wallParametrs[(wall.intValue() - 1)][1] = Double.parseDouble(path.evaluate(WALL + "/heigth", this.doc));
      }
      else {
        this.wallsData.wallParametrs[(wall.intValue() - 1)][1] = heigth;
      }

      this.wallsData.walls[(wall.intValue() - 1)] = new double[pointsAmount][2];
      for (Integer point = Integer.valueOf(1); point.intValue() <= pointsAmount; point = Integer.valueOf(point.intValue() + 1)) {
        this.wallsData.walls[(wall.intValue() - 1)][(point.intValue() - 1)][0] = Double.parseDouble(path.evaluate(WALL + "/x[" + point.toString() + "]", this.doc));

        this.wallsData.walls[(wall.intValue() - 1)][(point.intValue() - 1)][1] = Double.parseDouble(path.evaluate(WALL + "/y[" + point.toString() + "]", this.doc));
      }
    }
  }
}

