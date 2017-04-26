import Math.COLORFUNCTIONS;
import Math.Geometry;
import java.io.PrintStream;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL;


class roomDrawing
  implements openGLdraw
{
  private static final COLORFUNCTIONS CF = new COLORFUNCTIONS();
  private static final float GRAPH_SCALE_QUOTIENT = 0.5F;
  private surfaceEquation eq;
  private int[] LineQant;
  private double[] roomSize;
  private boolean addGrid;
  private boolean addBorder;
  private boolean addGraph;
  private boolean addDraft = false;
  private boolean addWalls = false;
  private float graphFall = 1.0F;
  private float graphTransparency;
  private float[] reducedRoomSize = new float[2];
  private float[] reducedCellSize = new float[2];
  private float[] _COLOR;
  private int roomDraftTexture;
  private double[] draftPositionX = new double[2];
  private double[] draftPositionY = new double[2];
  private double draftQuotientX;
  private double draftQuotientY;
  private apartment.wallContainer walls;

  roomDrawing(surfaceEquation EQ, float openGlmainPlaneSize, boolean addgrid, boolean addborder, boolean addgraph, float[] color, float graphTransparency)
  {
    this._COLOR = color;
    this.graphTransparency = graphTransparency;
    this.eq = EQ;
    this.LineQant = this.eq.getLineQant();
    this.roomSize = this.eq.getRoomSize();
    this.addGrid = addgrid;
    this.addBorder = addborder;
    this.addGraph = addgraph;
    float roomScaleQotient;
    //float roomScaleQotient;
    if (this.roomSize[0] > this.roomSize[1]) {
      roomScaleQotient = (float)(openGlmainPlaneSize / this.roomSize[0]);
    }
    else
    {
      roomScaleQotient = (float)(openGlmainPlaneSize / this.roomSize[1]);
    }

    for (int i = 0; i < 2; i++) {
      this.reducedRoomSize[i] = ((float)(roomScaleQotient * this.roomSize[i]));
      this.reducedCellSize[i] = (this.reducedRoomSize[i] / this.LineQant[i]);
    }
  }

  public void GLdraw(GL2 gl) {
    if (this.addBorder) borderDrawing(gl);
    if (this.addGrid) gridDrawing(gl);
    if (this.addDraft) draftDrawing(gl);
    if (this.addWalls) wallsDrawing(gl);
    if (this.addGraph) graphDrawing(gl); 
  }

  public void switchGraphFalling()
  {
    if (this.graphFall == 0.0F) this.graphFall = 1.0F; else
      this.graphFall = 0.0F;
  }

  public void switchGraphDrawing() {
    this.addGraph = (!this.addGraph);
  }
  public void switchGridNBorderDrawing() {
    this.addGrid = (!this.addGrid);
    this.addBorder = (!this.addBorder);
  }

  public void switchDraftDrawing()
  {
    this.addDraft = (!this.addDraft);
  }
  public void switchWallsDrawing() {
    this.addWalls = (!this.addWalls);
  }
  public void setRoomDraftTexture(int texture) {
    this.roomDraftTexture = texture;
  }

  public void setDraftPositionAndQuotient(apartment.dataContainer data)
  {
    double width = data.size[0] - data.offsetX[0] - data.offsetX[1];
    double heigth = data.size[1] - data.offsetY[0] - data.offsetY[1];

    this.draftPositionX[0] = (-this.reducedRoomSize[0] * (0.5D + data.offsetX[0] / width));
    this.draftPositionX[1] = (this.reducedRoomSize[0] * (0.5D + data.offsetX[1] / width));

    this.draftPositionY[0] = (-this.reducedRoomSize[1] * (0.5D + data.offsetY[0] / heigth));
    this.draftPositionY[1] = (this.reducedRoomSize[1] * (0.5D + data.offsetY[1] / heigth));

    this.draftQuotientX = ((this.draftPositionX[1] - this.draftPositionX[0]) / data.size[0]);
    this.draftQuotientY = ((this.draftPositionY[1] - this.draftPositionY[0]) / data.size[1]);
  }

  public void setWalls(apartment.wallContainer walls) {
    this.walls = walls;
  }

  public void setGraphTransparency(float f)
  {
    this.graphTransparency = f;
  }

  private void gridDrawing(GL2 gl) {
	      //GL2 gl = gl1.getGL2();
    gl.glColor3f(1.0F, 1.0F, 1.0F);
    gl.glBegin(1);

    for (int i = 1; i < this.LineQant[0]; i++) {
      float f = -0.5F * this.reducedRoomSize[0] + this.reducedCellSize[0] * i;
      gl.glVertex3f(f, 0.0F, this.reducedRoomSize[1] / 2.0F);
      gl.glVertex3f(f, 0.0F, -this.reducedRoomSize[1] / 2.0F);
    }
    for (int i = 1; i < this.LineQant[1]; i++) {
      float f = -0.5F * this.reducedRoomSize[1] + this.reducedCellSize[1] * i;
      gl.glVertex3f(this.reducedRoomSize[0] / 2.0F, 0.0F, f);
      gl.glVertex3f(-this.reducedRoomSize[0] / 2.0F, 0.0F, f);
    }

    gl.glEnd();
  }
  private void borderDrawing(GL2 gl) {

	      //GL2 gl = gl1.getGL2();


    gl.glColor3f(1.0F, 1.0F, 1.0F);
    gl.glBegin(2);

    gl.glVertex3f(-this.reducedRoomSize[0] / 2.0F, 
      0.0F, -this.reducedRoomSize[1] / 2.0F);
    gl.glVertex3f(-this.reducedRoomSize[0] / 2.0F, 
      0.0F, this.reducedRoomSize[1] / 2.0F);
    gl.glVertex3f(this.reducedRoomSize[0] / 2.0F, 
      0.0F, this.reducedRoomSize[1] / 2.0F);
    gl.glVertex3f(this.reducedRoomSize[0] / 2.0F, 
      0.0F, -this.reducedRoomSize[1] / 2.0F);

    gl.glEnd();
  }
  private void draftDrawing(GL2 gl) {

	      //GL2 gl = gl1.getGL2();

    double offset = -0.005D;

    gl.glEnable(3553);
    gl.glBindTexture(3553, this.roomDraftTexture);
    gl.glBegin(7);
    
    /*
    gl.glTexCoord2d(0.0D, 0.0D); gl.glVertex3d(this.draftPositionX[0], -0.005D, this.draftPositionY[0]);
    gl.glTexCoord2d(0.0D, 1.0D); gl.glVertex3d(this.draftPositionX[0], -0.005D, this.draftPositionY[1]);
    gl.glTexCoord2d(1.0D, 1.0D); gl.glVertex3d(this.draftPositionX[1], -0.005D, this.draftPositionY[1]);
    gl.glTexCoord2d(1.0D, 0.0D); gl.glVertex3d(this.draftPositionX[1], -0.005D, this.draftPositionY[0]);
    */
    gl.glTexCoord2d(0.0D, 1.0D); gl.glVertex3d(this.draftPositionX[0], -0.005D, this.draftPositionY[0]);
    gl.glTexCoord2d(0.0D, 0.0D); gl.glVertex3d(this.draftPositionX[0], -0.005D, this.draftPositionY[1]);
    gl.glTexCoord2d(1.0D, 0.0D); gl.glVertex3d(this.draftPositionX[1], -0.005D, this.draftPositionY[1]);
    gl.glTexCoord2d(1.0D, 1.0D); gl.glVertex3d(this.draftPositionX[1], -0.005D, this.draftPositionY[0]);
    

    gl.glEnd();
    gl.glDisable(3553);
  }

  private void wallsDrawing(GL2 gl)
  {

	      
	      //GL2 gl = gl1.getGL2();
    double[] a = new double[3];
    double[] b = new double[3];
    double[] c = new double[3];

    gl.glPushMatrix();
    gl.glTranslated(this.draftPositionX[0], 0.0D, this.draftPositionY[0]);
    gl.glScaled(this.draftQuotientX, 1.0D, this.draftQuotientY);
    gl.glColor4d(0.85D, 0.85D, 0.85D, 1.0D);
    for (int wallNm = 0; wallNm < this.walls.walls.length; wallNm++) {
      int pointsAmount = this.walls.walls[wallNm].length;
      if (pointsAmount > 1)
      {
        wallSideDraw(gl, pointsAmount, this.walls.walls[wallNm], this.walls.wallParametrs[wallNm]);

        gl.glBegin(8);
        a[0] = (this.walls.reverseWalls[wallNm][(pointsAmount - 1)][0] - this.walls.walls[wallNm][(pointsAmount - 1)][0]);
        a[1] = 0.0D;
        a[2] = (this.walls.reverseWalls[wallNm][(pointsAmount - 1)][1] - this.walls.walls[wallNm][(pointsAmount - 1)][1]);

        b[0] = 0.0D;
        b[1] = -1.0D;
        b[2] = 0.0D;

        c = Geometry.normVectorProduct(a, b);
        gl.glNormal3d(c[0], c[1], c[2]);
        gl.glVertex3d(this.walls.walls[wallNm][(pointsAmount - 1)][0], 0.0D, this.walls.walls[wallNm][(pointsAmount - 1)][1]);
        gl.glVertex3d(this.walls.walls[wallNm][(pointsAmount - 1)][0], this.walls.wallParametrs[wallNm][1], this.walls.walls[wallNm][(pointsAmount - 1)][1]);
        gl.glVertex3d(this.walls.reverseWalls[wallNm][(pointsAmount - 1)][0], 0.0D, this.walls.reverseWalls[wallNm][(pointsAmount - 1)][1]);
        gl.glVertex3d(this.walls.reverseWalls[wallNm][(pointsAmount - 1)][0], this.walls.wallParametrs[wallNm][1], this.walls.reverseWalls[wallNm][(pointsAmount - 1)][1]);
        gl.glEnd();

        wallSideDraw(gl, pointsAmount, this.walls.reverseWalls[wallNm], this.walls.wallParametrs[wallNm]);

        gl.glBegin(8);
        gl.glBegin(8);
        a[0] = (this.walls.reverseWalls[wallNm][0][0] - this.walls.walls[wallNm][0][0]);
        a[1] = 0.0D;
        a[2] = (this.walls.reverseWalls[wallNm][0][1] - this.walls.walls[wallNm][0][1]);

        b[0] = 0.0D;
        b[1] = -1.0D;
        b[2] = 0.0D;

        c = Geometry.normVectorProduct(a, b);
        gl.glNormal3d(c[0], c[1], c[2]);
        gl.glVertex3d(this.walls.walls[wallNm][0][0], 0.0D, this.walls.walls[wallNm][0][1]);
        gl.glVertex3d(this.walls.walls[wallNm][0][0], this.walls.wallParametrs[wallNm][1], this.walls.walls[wallNm][0][1]);
        gl.glVertex3d(this.walls.reverseWalls[wallNm][0][0], 0.0D, this.walls.reverseWalls[wallNm][0][1]);
        gl.glVertex3d(this.walls.reverseWalls[wallNm][0][0], this.walls.wallParametrs[wallNm][1], this.walls.reverseWalls[wallNm][0][1]);
        gl.glEnd();

        gl.glEnd();

        wallTopDraw(gl, pointsAmount, this.walls.walls[wallNm], this.walls.reverseWalls[wallNm], this.walls.wallParametrs[wallNm]);
      }
      else
      {
        System.out.println("Wall drawing failure: not enaugh points!");
      }
    }
    gl.glPopMatrix();
  }
  private void wallSideDraw(GL2 gl, int pointsAmount, double[][] wall, double[] wallParametrs) {

	      //GL2 gl = gl1.getGL2();

    double[] a = new double[3];
    double[] b = new double[3];
    double[] c = new double[3];

    gl.glBegin(8);
    for (int point = 0; point < pointsAmount - 1; point++) {
      a[0] = (wall[(point + 1)][0] - wall[point][0]);
      a[1] = 0.0D;
      a[2] = (wall[(point + 1)][1] - wall[point][1]);

      b[0] = 0.0D;
      b[1] = -1.0D;
      b[2] = 0.0D;

      c = Geometry.normVectorProduct(a, b);
      gl.glNormal3d(c[0], c[1], c[2]);
      gl.glVertex3d(wall[point][0], 0.0D, wall[point][1]);
      gl.glVertex3d(wall[point][0], wallParametrs[1], wall[point][1]);
    }
    gl.glVertex3d(wall[(pointsAmount - 1)][0], 0.0D, wall[(pointsAmount - 1)][1]);
    gl.glVertex3d(wall[(pointsAmount - 1)][0], wallParametrs[1], wall[(pointsAmount - 1)][1]);
    gl.glEnd();
  }
  private void wallTopDraw(GL2 gl, int pointsAmount, double[][] wall1, double[][] wall2, double[] wallParametrs) {
	      //GL2 gl = gl1.getGL2();
    gl.glBegin(8);
    gl.glNormal3d(0.0D, -1.0D, 0.0D);
    for (int point = 0; point < pointsAmount; point++)
    {
      gl.glVertex3d(wall1[point][0], wallParametrs[1], wall1[point][1]);
      gl.glVertex3d(wall2[point][0], wallParametrs[1], wall2[point][1]);
    }
    gl.glEnd();
  }

  private void graphDrawing(GL2 gl)
  {


	      //GL2 gl = gl1.getGL2();
    double[] a = new double[3];
    double[] b = new double[3];
    double[][] c = new double[this.LineQant[1]][3];

    gl.glEnable(3008);
    gl.glEnable(3042);
    gl.glBlendFunc(770, 771);

    for (int i = 0; i < this.LineQant[0]; i++) {
      gl.glBegin(8);
		int j;
      for (j = 0; j < this.LineQant[1]; j++)
      {
        a[0] = 0.0D;
        a[1] = (this.eq.Eq(i, j + 1) - this.eq.Eq(i, j));
        a[2] = this.reducedCellSize[1];
        b[0] = this.reducedCellSize[0];
        b[1] = (this.eq.Eq(i + 1, j) - this.eq.Eq(i, j));
        b[2] = 0.0D;

        gl.glNormal3d(-c[j][0], -c[j][1], -c[j][2]);

        float f = -0.5F * this.reducedRoomSize[1] + j * this.reducedCellSize[1];
        float[] COLOR = COLORFUNCTIONS.setSpectrumColor(this.eq.Eq(i, j), 1.0D);
        gl.glColor4f(COLOR[0], COLOR[1], COLOR[2], this.graphTransparency);
        gl.glVertex3f(-0.5F * this.reducedRoomSize[0] + i * this.reducedCellSize[0], 0.5F * this.graphFall * this.eq.Eq(i, j), f);

        c[j] = Geometry.normVectorProduct(a, b);
        gl.glNormal3d(-c[j][0], -c[j][1], -c[j][2]);

        COLOR = COLORFUNCTIONS.setSpectrumColor(this.eq.Eq(i + 1, j), 1.0D);
        gl.glColor4f(COLOR[0], COLOR[1], COLOR[2], this.graphTransparency);
        gl.glVertex3f(-0.5F * this.reducedRoomSize[0] + (i + 1) * this.reducedCellSize[0], 0.5F * this.graphFall * this.eq.Eq(i + 1, j), f);

		
		  //System.out.format("i,j, value  = %d, %d  %f \n",i,j, this.eq.Eq(i, j));

      }

      float f = -0.5F * this.reducedRoomSize[1] + j * this.reducedCellSize[1];
      gl.glVertex3f(-0.5F * this.reducedRoomSize[0] + i * this.reducedCellSize[0], 0.5F * this.graphFall * this.eq.Eq(i, j), f);
      gl.glVertex3f(-0.5F * this.reducedRoomSize[0] + (i + 1) * this.reducedCellSize[0], 0.5F * this.graphFall * this.eq.Eq(i + 1, j), f);
      gl.glEnd();


    }

    gl.glDisable(3042);
    gl.glDisable(3008);
  }
}

