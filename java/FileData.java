import Math.Interpolator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

class FileData
  implements surfaceEquation, statisticalExposition, TimeScrollingListener
{
  private String file;
  private int[] LineQant = new int[2];
  private double[][][] value;
  private double maxValue;
  private int hoursQant;
  private float[][] currentValuesMatrix;
  private double firstMeasureHour;

  FileData(String filename)
  {
    this.file = filename;
    loadDataFromFile();
    this.currentValuesMatrix = new float[this.LineQant[0] + 1][this.LineQant[1] + 1];
    setCurrentValuesMatrix(0.0D);
  }

  private void loadDataFromFile() {
    try {
      Scanner in = new Scanner(new FileInputStream(this.file));
      ArrayList data = new ArrayList();
      while (in.hasNextDouble()) {
        data.add(Double.valueOf(in.nextDouble()));
      }
      ParseData(data);
    }
    catch (FileNotFoundException e) {
      System.out.println("Can't open file");
      e.printStackTrace();
    }
  }

  public float Eq(int xCross, int yCross)
  {
    return (float)(this.currentValuesMatrix[xCross][yCross] / this.maxValue);
  }
  public double getData(int hour, int xCross, int yCross) {
    return this.value[xCross][yCross][hour] / this.maxValue;
  }
  public int[] dimensions() {
    int[] i = new int[3];

    i[0] = this.LineQant[0];
    i[1] = this.LineQant[1];
    i[2] = this.hoursQant;
    return i;
  }
  public int[] getLineQant() {
    int[] i = new int[2];

    i[0] = this.LineQant[0];
    i[1] = this.LineQant[1];

    return i;
  }
  public double[] getRoomSize() {
    double[] d = new double[2];

    d[0] = this.LineQant[0];
    d[1] = this.LineQant[1];

    return d;
  }
  public void changeTimeMoment(TimeScrollBox time) {
    setCurrentValuesMatrix(time.getTimePointer());
  }
  public int getMeasuresAmount() {
    return this.hoursQant + 1;
  }
  public double getFirstMeasureHour() {
    return this.firstMeasureHour;
  }

  private void setCurrentValuesMatrix(double timePosition) {
    Interpolator.interpolateMatrix_TimeDependence(this.value, this.currentValuesMatrix, timePosition, this.hoursQant);
  }

  private void ParseData(ArrayList<Double> data)
  {
    int reservedDataAmount = 3;

    this.LineQant[0] = ((int)((Double)data.get(0)).doubleValue());
    this.LineQant[1] = ((int)((Double)data.get(1)).doubleValue());

    this.firstMeasureHour = ((Double)data.get(2)).doubleValue();

    int size = data.size() - 3;
    this.hoursQant = (size / ((this.LineQant[0] + 1) * (this.LineQant[1] + 1)) - 1);
    this.value = new double[this.LineQant[0] + 1][this.LineQant[1] + 1][this.hoursQant + 1];

    int counter = 3;
    this.maxValue = ((Double)data.get(counter)).doubleValue();
    for (int j = 0; j <= this.LineQant[1]; j++)
      for (int i = 0; i <= this.LineQant[0]; i++)
        for (int k = 0; k <= this.hoursQant; k++) {
          if ((this.value[i][j][k] = ((Double)data.get(counter)).doubleValue()) > this.maxValue) {
            this.maxValue = ((Double)data.get(counter)).doubleValue();
          }
          counter++;
        }
  }
}
