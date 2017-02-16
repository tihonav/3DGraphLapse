package Math;

public class COLORFUNCTIONS
{
  private static final float[] RED = { 1.0F, 0.0F, 0.0F };
  private static final float[] ORANGE = { 1.0F, 0.5F, 0.0F };
  private static final float[] YELLOW = { 1.0F, 1.0F, 0.0F };
  private static final float[] GREEN = { 0.0F, 1.0F, 0.0F };
  private static final float[] CYAN = { 0.0F, 1.0F, 1.0F };
  private static final float[] BLUE = { 0.0F, 0.0F, 1.0F };
  private static final float[] MAGENTA = { 1.0F, 0.0F, 1.0F };

  private static int INTERVALS = 6;
  private static boolean INITIALIZED = false;
  private static final int SPECTRUMCOLORGRADATIONS = 100000;
  private static float[][] SPECTRUMCOLOR = new float[100001][3];

  public COLORFUNCTIONS()
  {
    if (!INITIALIZED) {
      calculateSpectrumColors();
      INITIALIZED = true;
    }
  }

  public static float[] setSpectrumColor(double value, double maxValue)
  {
    if (value <= 0.0D) {
      return MAGENTA;
    }
    if (value >= maxValue) {
      return RED;
    }

    return SPECTRUMCOLOR[((int)(value * 100000.0D / maxValue))];
  }

  private static void calculateSpectrumColors()
  {
    for (int i = 0; i <= 100000; i++)
      SPECTRUMCOLOR[i] = SpectrumColor(i, 100000.0D);
  }

  public static float[] SpectrumColor(double value, double maxValue)
  {
    float[] COLOR = new float[3];
    double HEIGTH = maxValue - value;

    if (HEIGTH < 0.0D) {
      for (int i = 0; i < 3; i++) COLOR[i] = RED[i];
    }
    else if (HEIGTH >= maxValue) {
      for (int i = 0; i < 3; i++) COLOR[i] = MAGENTA[i]; 
    }
    else
    {
      double VALUE = HEIGTH * INTERVALS / maxValue;
      double FLOOR = Math.floor(VALUE);
      double DELTA = VALUE - FLOOR;

      switch ((int)FLOOR) {
      case 0:
        for (int i = 0; i < 3; i++) COLOR[i] = RED[i];
        COLOR[1] += 0.5F * (float)DELTA;
        break;
      case 1:
        for (int i = 0; i < 3; i++) COLOR[i] = ORANGE[i];
        COLOR[1] += 0.5F * (float)DELTA;
        break;
      case 2:
        for (int i = 0; i < 3; i++) COLOR[i] = YELLOW[i];
        COLOR[0] -= (float)DELTA;
        break;
      case 3:
        for (int i = 0; i < 3; i++) COLOR[i] = GREEN[i];
        COLOR[2] += (float)DELTA;
        break;
      case 4:
        for (int i = 0; i < 3; i++) COLOR[i] = CYAN[i];
        COLOR[1] -= (float)DELTA;
        break;
      case 5:
        for (int i = 0; i < 3; i++) COLOR[i] = BLUE[i];
        COLOR[0] += (float)DELTA;
      }
    }

    return COLOR;
  }
}

