package Math;

public class Interpolator
{
  public static double interpolateLine(double[] value, int sizeX, float arg)
  {
    int size = sizeX - 1;

    double interpolValue = 0.0D;
    for (int i = 0; i <= size; i++) {
      double contrib = value[i];
      for (int j = 0; j < i; j++) {
        contrib = contrib * (arg * size - j) / (i - j);
      }
      for (int j = i + 1; j <= size; j++) {
        contrib = contrib * (arg * size - j) / (i - j);
      }
      interpolValue += contrib;
    }
    return interpolValue;
  }
  public static double interpolateLine(double[] value, float arg) {
    int size = value.length - 1;

    double interpolValue = 0.0D;
    for (int i = 0; i <= size; i++) {
      double contrib = value[i];
      for (int j = 0; j < i; j++) {
        contrib = contrib * (arg * size - j) / (i - j);
      }
      for (int j = i + 1; j <= size; j++) {
        contrib = contrib * (arg * size - j) / (i - j);
      }
      interpolValue += contrib;
    }
    return interpolValue;
  }

  public static double interpolateLine(double[] value, double[] arg, double curArg) {
    int size = value.length - 1;

    double interpolValue = 0.0D;
    for (int i = 0; i <= size; i++) {
      double contrib = value[i];
      for (int j = 0; j < i; j++) {
        contrib = contrib * (curArg - arg[j]) / (arg[i] - arg[j]);
      }
      for (int j = i + 1; j <= size; j++) {
        contrib = contrib * (curArg - arg[j]) / (arg[i] - arg[j]);
      }
      interpolValue += contrib;
    }
    return interpolValue;
  }

  public static double interpolateLine(double[][] value, double arg) {
    int size = value.length - 1;

    double interpolValue = 0.0D;
    for (int i = 0; i <= size; i++) {
      double contrib = value[i][1];

      for (int j = 0; j < i; j++) {
        contrib = contrib * (arg - value[j][0]) / (value[i][0] - value[j][0]);
      }
      for (int j = i + 1; j <= size; j++) {
        contrib = contrib * (arg - value[j][0]) / (value[i][0] - value[j][0]);
      }
      interpolValue += contrib;
    }
    return interpolValue;
  }
  public static double interpolateLineInverted(double[][] value, double arg) {
    int size = value.length - 1;

    double interpolValue = 0.0D;
    for (int i = 0; i <= size; i++) {
      double contrib = value[i][0];

      for (int j = 0; j < i; j++) {
        contrib = contrib * (arg - value[j][1]) / (value[i][1] - value[j][1]);
      }
      for (int j = i + 1; j <= size; j++) {
        contrib = contrib * (arg - value[j][1]) / (value[i][1] - value[j][1]);
      }
      interpolValue += contrib;
    }
    return interpolValue;
  }

  public static double interpolatePlane(double[][] value, int sizeX, int sizeY, float argX, float argY)
  {
    double[] d = new double[sizeX];

    for (int i = 0; i < sizeX; i++) {
      d[i] = interpolateLine(value[i], sizeY, argY);
    }
    return interpolateLine(d, sizeX, argX);
  }

  public static double interpolatePlane(double[][] value, float argX, float argY) {
    double[] d = new double[value.length];

    for (int i = 0; i < value.length; i++) {
      d[i] = interpolateLine(value[i], argY);
    }
    return interpolateLine(d, argX);
  }

  public static double interpolatePlaneByParts6on6(double[][] value, float argX, float argY)
  {
    int matrixLength = 6;
    int matrixHeigth = 6;

    double pointXdouble = argX * (value.length - 1);
    int pointX = (int)Math.floor(pointXdouble);
    double pointYdouble = argY * (value[pointX].length - 1);
    int pointY = (int)Math.floor(pointYdouble);

    int left = pointX + 1 - 3;
    int right = pointX + 3;
    int top = pointY + 1 - 3;
    int bottom = pointY + 3;

    if (left < 0) left = 0;
    if (right >= value.length) right = value.length - 1;
    if (top < 0) top = 0;
    if (bottom >= value[pointX].length) bottom = value[pointX].length - 1;

    int realMatrixLength = right - left + 1;
    int realMatrixHeigth = bottom - top + 1;

    double[][] m = new double[realMatrixLength][realMatrixHeigth];
    for (int i = left; i <= right; i++) {
      for (int j = top; j <= bottom; j++) {
        m[(i - left)][(j - top)] = value[i][j];
      }
    }
    return 
      interpolatePlane(m, 
      ((float)pointXdouble - left) / (realMatrixLength - 1.0F), 
      ((float)pointYdouble - top) / (realMatrixHeigth - 1.0F));
  }

  public static double interpolateLineByParts(double[] value, float arg)
  {

		//System.out.format("arg = %f", arg);

    int Length = 6;

    double pointDouble = arg * (value.length - 1);
    int point = (int)Math.floor(pointDouble);

    int left = point + 1 - 3;
    int right = point + 3;

    if (left < 0) left = 0;
    if (right >= value.length) right = value.length - 1;

    int realLength = right - left + 1;

    double[] d = new double[realLength];
    for (int i = left; i <= right; i++) {
      d[(i - left)] = value[i];
    }

    return interpolateLine(d, ((float)pointDouble - left) / (realLength - 1.0F));
  }

  public static void interpolateMatrix_TimeDependence(double[][][] value, float[][] currentValuesMatrix, double timePosition, int timeMoments) {
    int[] LineQant = new int[2];

    LineQant[0] = (value.length - 1);

    double PRECIZEMOMENT = timePosition * timeMoments;
    int MOMENT = (int)Math.floor(PRECIZEMOMENT);
    double FRACTIONAL = PRECIZEMOMENT - MOMENT;

    if (MOMENT < timeMoments) {
      for (int i = 0; i <= LineQant[0]; i++) {
        LineQant[1] = (value[i].length - 1);
        for (int j = 0; j <= LineQant[1]; j++) {
          currentValuesMatrix[i][j] = ((float)(value[i][j][MOMENT] + (value[i][j][(MOMENT + 1)] - value[i][j][MOMENT]) * FRACTIONAL));
        }
      }
    }
    else
    {
      for (int i = 0; i <= LineQant[0]; i++)
        for (int j = 0; j <= LineQant[1]; j++)
          currentValuesMatrix[i][j] = ((float)value[i][j][MOMENT]);
    }
  }
}

