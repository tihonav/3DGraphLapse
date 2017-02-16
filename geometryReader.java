import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import java.util.*;

class geometryReader
{

	private final static  String POSITION_TAG   = "position";
	private final static  String TRIANGULAR_TAG = "triangular";
	private final static  String NAME_ATTR      = "name";
	private final static  String X_ATTR         = "x";
	private final static  String Y_ATTR         = "y";
	private final static  String Z_ATTR         = "z";
	private final static  String VERTEX_ATTR    = "vertex";
	private final static  int NVERTEX_POLYGON   = 3;

	//private final static  String VERTEX2_ATTR = "vertex2";
	//private final static  String VERTEX3_ATTR = "vertex3";
	
	public HashMap<String,Geometry3Vertex > all_vertex = new HashMap<String,Geometry3Vertex >();
	public Vector<GeometryPolygon> all_polygons = new Vector<GeometryPolygon>();

	

	public boolean Parse(String fname)
	{
		try {
			File f = new File(fname);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
		

			//* Get vertices
			NodeList nList = doc.getElementsByTagName(POSITION_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				/*
				Node nNode = nList.item(temp);
				NamedNodeMap attr = nNode.getAttributes();
				all_vertex.put(
					attr.getNamedItem(NAME_ATTR).getNodeValue(), new Geometry3Vertex(
						Double.parseDouble(attr.getNamedItem(X_ATTR).getNodeValue()),
						Double.parseDouble(attr.getNamedItem(Y_ATTR).getNodeValue()),
						Double.parseDouble(attr.getNamedItem(Z_ATTR).getNodeValue())
					)
				);
				*/
				System.out.format("\rProcessing file %s   vertex: %8d/%8d  %4.1f %%",fname,temp,nList.getLength(), temp/nList.getLength()*100.);
				System.out.flush();
			}

			//* Get triangles
			nList = doc.getElementsByTagName(TRIANGULAR_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				Node nNode = nList.item(temp);
				NamedNodeMap attr = nNode.getAttributes();
				GeometryPolygon pol = new GeometryPolygon();
				for(int i=1; i<=NVERTEX_POLYGON;i++){
					pol.vertex.add(all_vertex.get(
						attr.getNamedItem(VERTEX_ATTR + Integer.toString(i)).getNodeValue()
					));
					
				}
				all_polygons.add(pol);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args){
		geometryReader a = new geometryReader();
		a.Parse("input.gdml");
	}
}

class Geometry3Vertex
{
	public Geometry3Vertex(Double x, Double y, Double z)
	{	
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Double x;
	public Double y;
	public Double z;
}

class GeometryPolygon
{
	public Vector<Geometry3Vertex> vertex = new Vector<Geometry3Vertex>();
}
