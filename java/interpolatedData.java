import Math.Interpolator;

class interpolatedData
  implements surfaceEquation, TimeScrollingListener
{
  private int[] LineQant = new int[2];
  private double[][][] value;
  private int timeMoments;
  private float[][] currentValuesMatrix;
  statisticalExposition statisticalexposition;

  interpolatedData(statisticalExposition S, int xLines, int yLines)
  {
    this.statisticalexposition = S;
    this.LineQant[0] = xLines;
    this.LineQant[1] = yLines;
    this.currentValuesMatrix = new float[this.LineQant[0] + 1][this.LineQant[1] + 1];
    init();
  }

  private void init() {
    interpolate();
    setCurrentValuesMatrix(0.0D);
  }

  public float Eq(int xCross, int yCross)
  {
    float result = this.currentValuesMatrix[xCross][yCross]; 
    if (result <0){
        result  = 0;
    }
    return result; 
	      //return this.currentValuesMatrix[xCross][yCross];
  }

  public int[] getLineQant()
  {
    int[] i = new int[2];

    i[0] = this.LineQant[0];
    i[1] = this.LineQant[1];
    return i;
  }
  public double[] getRoomSize() {
    return this.statisticalexposition.getRoomSize();
  }

  private void interpolate()
  {
    int[] dimensions = this.statisticalexposition.dimensions();

    this.timeMoments = dimensions[2];
    this.value = new double[this.LineQant[0] + 1][this.LineQant[1] + 1][dimensions[2] + 1];

    for (int moment = 0; moment <= dimensions[2]; moment++) {
      double[][] SAMPLE = new double[dimensions[0] + 1][dimensions[1] + 1];
      for (int i = 0; i <= dimensions[0]; i++) {
        for (int j = 0; j <= dimensions[1]; j++) {
          SAMPLE[i][j] = this.statisticalexposition.getData(moment, i, j);
        }
      }
      double[][] TEMPORARYSAMPLE = SAMPLE;
      SAMPLE = new double[this.LineQant[1] + 1][dimensions[0] + 1];

      for (int i = 0; i <= dimensions[0]; i++) {
        for (int j = 0; j <= this.LineQant[1]; j++) {
          SAMPLE[j][i] = Interpolator.interpolateLineByParts(TEMPORARYSAMPLE[i], (float)j / this.LineQant[1]);
        }
      }

      for (int i = 0; i <= this.LineQant[0]; i++)
        for (int j = 0; j <= this.LineQant[1]; j++){
          this.value[i][j][moment] = Interpolator.interpolateLineByParts(SAMPLE[j], (float)i / this.LineQant[0]);

		  }
    }
  }

  public void changeTimeMoment(TimeScrollBox time)
  {
    setCurrentValuesMatrix(time.getTimePointer());
		//System.out.format("time.getTimePointer() = %f\n",time.getTimePointer());
  }

  private void setCurrentValuesMatrix(double timePosition) {
    Interpolator.interpolateMatrix_TimeDependence(this.value, this.currentValuesMatrix, timePosition, this.timeMoments);
  }
}

