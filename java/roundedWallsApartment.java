import Math.Geometry;
import Math.Interpolator;
import java.io.PrintStream;

class roundedWallsApartment extends apartment
{
  private static final int POINTS_AMOUNT_ON_ROUND_WALL = 10;

  roundedWallsApartment(apartment APARTMENT)
  {
    createRoundedWalls(APARTMENT);
    createReverseWalls(this.wallsData);
  }
  private void createRoundedWalls(apartment APARTMENT) {
    int wallsAmount = APARTMENT.wallsData.walls.length;

    this.data.size = APARTMENT.data.size;
    this.wallsData.wallParametrs = APARTMENT.wallsData.wallParametrs;
    this.wallsData.walls = new double[wallsAmount][][];

    for (int wallNm = 0; wallNm < wallsAmount; wallNm++)
      if (APARTMENT.wallsData.walls[wallNm].length > 2) {
        this.wallsData.walls[wallNm] = new double[10][2];

        double xFirst = APARTMENT.wallsData.walls[wallNm][0][0];
        double xLength = APARTMENT.wallsData.walls[wallNm][(APARTMENT.wallsData.walls[wallNm].length - 1)][0] - xFirst;

        double yFirst = APARTMENT.wallsData.walls[wallNm][0][1];
        double yLength = APARTMENT.wallsData.walls[wallNm][(APARTMENT.wallsData.walls[wallNm].length - 1)][1] - yFirst;

        if (Math.abs(xLength) > Math.abs(yLength)) {
          interpolateWall(APARTMENT, wallNm, 0);
        }
        else
          interpolateWall(APARTMENT, wallNm, 1);
      }
      else
      {
        this.wallsData.walls[wallNm] = APARTMENT.wallsData.walls[wallNm];
      }
  }

  private void interpolateWall(apartment APARTMENT, int wallNm, int argIndex)
  {
    for (int point = 0; point < 10; point++) {
      double First = APARTMENT.wallsData.walls[wallNm][0][argIndex];
      double Length = APARTMENT.wallsData.walls[wallNm][(APARTMENT.wallsData.walls[wallNm].length - 1)][argIndex] - First;

      double current = this.wallsData.walls[wallNm][point][argIndex] = First + Length * point / 9.0D;
      if (argIndex == 0) {
        this.wallsData.walls[wallNm][point][1] = Interpolator.interpolateLine(APARTMENT.wallsData.walls[wallNm], current);
      }
      else
        this.wallsData.walls[wallNm][point][0] = Interpolator.interpolateLineInverted(APARTMENT.wallsData.walls[wallNm], current);
    }
  }

  private static void createReverseWalls(apartment.wallContainer wallsData)
  {
    int wallsAmount = wallsData.walls.length;

    wallsData.reverseWalls = new double[wallsAmount][][];
    for (int wallNm = 0; wallNm < wallsAmount; wallNm++) {
      int pointsAmount = wallsData.walls[wallNm].length;

      wallsData.reverseWalls[wallNm] = new double[pointsAmount][2];
      if (pointsAmount > 1) {
        for (int point = 1; point < pointsAmount - 1; point++) {
          double[] a = new double[2];
          double[] b = new double[2];
          for (int i = 0; i < 2; i++) {
            a[i] = (wallsData.walls[wallNm][point][i] - wallsData.walls[wallNm][(point - 1)][i]);
            b[i] = (wallsData.walls[wallNm][(point + 1)][i] - wallsData.walls[wallNm][point][i]);
          }
          double[] vector = Geometry.findBorderVectorOnPlane(a, b, wallsData.wallParametrs[wallNm][0]);
          for (int i = 0; i < 2; i++) {
            wallsData.reverseWalls[wallNm][point][i] = (wallsData.walls[wallNm][point][i] + vector[i]);
          }
        }
        double[] firstVector = new double[2];
        double[] lastVector = new double[2];
        for (int i = 0; i < 2; i++) {
          firstVector[i] = (wallsData.walls[wallNm][1][i] - wallsData.walls[wallNm][0][i]);
          lastVector[i] = (wallsData.walls[wallNm][(pointsAmount - 1)][i] - wallsData.walls[wallNm][(pointsAmount - 2)][i]);
        }
        firstVector = Geometry.findBorderVectorOnPlane(firstVector, firstVector, wallsData.wallParametrs[wallNm][0]);
        lastVector = Geometry.findBorderVectorOnPlane(lastVector, lastVector, wallsData.wallParametrs[wallNm][0]);
        for (int i = 0; i < 2; i++) {
          wallsData.reverseWalls[wallNm][0][i] = (wallsData.walls[wallNm][0][i] + firstVector[i]);
          wallsData.reverseWalls[wallNm][(pointsAmount - 1)][i] = (wallsData.walls[wallNm][(pointsAmount - 1)][i] + lastVector[i]);
        }

      }
      else
      {
        System.out.println("Wall drawing failure: not enaugh points!");
      }
    }
  }
}

