package Math;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class Geometry
{
  public static double[] normVectorProduct(double[] a, double[] b)
  {
    double[] c = new double[3];

    c[0] = (a[1] * b[2] - a[2] * b[1]);
    c[1] = (a[2] * b[0] - a[0] * b[2]);
    c[2] = (a[0] * b[1] - a[1] * b[0]);

    return c;
  }
  public static double scalarVectorProduct(double[] a, double[] b) {
    double result = 0.0D;

    for (int i = 0; i < 3; i++) {
      result += a[i] * b[i];
    }
    return result;
  }

  public static boolean lineNsphereCross(double[] point1, double[] point2, double[] center, double radius, double[] vectorResult) {
    double a = 0.0D;
    double b = 0.0D;
    double c = 0.0D;

    for (int i = 0; i < 3; i++) {
      double d = point2[i] - point1[i];

      a += d * d;

      b += d * point1[i];

      c += point1[i] * point1[i];
    }

    c -= radius * radius;

    double d = b * b - a * c;
    double k1 = (-b + Math.sqrt(d)) / a;
    double k2 = (-b - Math.sqrt(d)) / a;
    double k;
    
    if (Math.abs(k1) < Math.abs(k2)) k = k1; else {
      k = k2;
    }
    double[] result = new double[3];
    for (int i = 0; i < 3; i++) {
      result[i] = (k * (point2[i] - point1[i]) + point1[i]);
    }

    for (int i = 0; i < 3; i++) vectorResult[i] = result[i];

    if (d < 0.0D) return false;
    return true;
  }

  public static boolean findRotateParametrsAlgorythm1(GLAutoDrawable gLDrawable, GLU glu, double x1, double y1, double x2, double y2, double[] param) {
    GL2 gl = gLDrawable.getGL().getGL2();

    double radius = 1.2D;

    double[] vector1 = new double[3];
    double[] vector2 = new double[3];
    if ((FindVectorForAlgorythm1(gLDrawable, glu, x1, y1, radius, vector1)) && (FindVectorForAlgorythm1(gLDrawable, glu, x2, y2, radius, vector2))) {
      double[] vectorProduct = normVectorProduct(vector1, vector2);
      for (int i = 0; i < 3; i++) param[(i + 1)] = vectorProduct[i];
      param[0] = 
        (57.295779513082323D * 
        Math.acos(scalarVectorProduct(vector1, vector2) / (
        Math.sqrt(scalarVectorProduct(vector1, vector1)) * Math.sqrt(scalarVectorProduct(vector2, vector2)))));
      return true;
    }

    return false;
  }

  private static boolean FindVectorForAlgorythm1(GLAutoDrawable gLDrawable, GLU glu, double x, double y, double radius, double[] result)
  {
    GL2 gl = gLDrawable.getGL().getGL2();

    double[] point1 = new double[3];
    double[] point2 = new double[3];
    double[] center = { 0.0D, 0.0D, 0.0D };

    double[] model = new double[16];
    double[] projection = new double[16];
    int[] viewport = new int[4];
    gl.glGetDoublev(2982, model, 0);
    gl.glGetDoublev(2983, projection, 0);
    gl.glGetIntegerv(2978, viewport, 0);
	
    /*   	
        System.out.format("\n");
	for(int i=0; i<16; i++) System.out.format("model[i] = %f\n", model[i]);
	for(int i=0; i<16; i++) System.out.format("projection[i] = %f\n", projection[i]);
	for(int i=0; i<4; i++) System.out.format("viewport[i] = %d\n", viewport[i]);
	for(int i=0; i<16; i++)
	       if(Double.isNaN(model[i])) System.out.format(" --> nan");
	
    */

    glu.gluUnProject(x, y, 0.0D, model, 0, projection, 0, viewport, 0, point1, 0);
    glu.gluUnProject(x, y, 2.0D, model, 0, projection, 0, viewport, 0, point2, 0);

    return lineNsphereCross(point1, point2, center, 1.0D, result);
  }

  public static void FindScreenVector(GLAutoDrawable gLDrawable, GLU glu, double x, double y, double z, double[] result)
  {
    GL2 gl = gLDrawable.getGL().getGL2();

    double[] point1 = new double[3];
    double[] point2 = new double[3];

    double[] model = new double[16];
    double[] projection = new double[16];
    int[] viewport = new int[4];
    gl.glGetDoublev(2982, model, 0);
    gl.glGetDoublev(2983, projection, 0);
    gl.glGetIntegerv(2978, viewport, 0);

    glu.gluUnProject(0.0D, 0.0D, 0.0D, model, 0, projection, 0, viewport, 0, point1, 0);
    glu.gluUnProject(x, y, z, model, 0, projection, 0, viewport, 0, point2, 0);

    for (int i = 0; i < 3; i++) point2[i] -= point1[i]; 
  }

  public static void FindVector(GLAutoDrawable gLDrawable, GLU glu, double x, double y, double z, double[] result) { GL2 gl = gLDrawable.getGL().getGL2();

    double[] point = new double[3];

    double[] model = new double[16];
    double[] projection = new double[16];
    int[] viewport = new int[4];
    gl.glGetDoublev(2982, model, 0);
    gl.glGetDoublev(2983, projection, 0);
    gl.glGetIntegerv(2978, viewport, 0);

    glu.gluUnProject(x, y, z, model, 0, projection, 0, viewport, 0, point, 0);

    for (int i = 0; i < 3; i++) result[i] = point[i];  } 
  public static double[] findBorderVectorOnPlane(double[] a, double[] b, double modulus)
  {
    double[] result = new double[2];

    double X = a[0] + b[0];
    double Y = a[1] + b[1];

    if (X < 0.0D) {
      X = -X;
      Y = -Y;
    }
    double sin = X / Math.sqrt(X * X + Y * Y);
    double alpha = Math.asin(sin);

    if (X * Y < 0.0D) {
      alpha = 3.141592653589793D - alpha;
    }

    result[0] = (modulus * Math.cos(-alpha));
    result[1] = (modulus * Math.sin(-alpha));

    if (a[0] * result[1] - a[1] * result[0] > 0.0D) {
      result[0] = (-result[0]);
      result[1] = (-result[1]);
    }

    return result;
  }
}

