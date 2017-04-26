import Math.Geometry;
//import com.sun.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.Texture;
//import com.sun.opengl.util.texture.TextureIO;
import  com.jogamp.opengl.util.texture.TextureIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class JavaRenderer
  implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
  private static final GLU glu = new GLU();
  private static final float GRAPH3DTRANSPARENCY = 0.95F;
  private static final float GRAPH2DTRANSPARENCY = 0.55F;
  private Texture texture;
  private static String imageFile = "Rekonstrukciq-Model1.jpg";
  private static apartment APARTMENT = apartment.readApartmentFromFile("apartment/apartment.xml");

  private static float[] _COLOR1 = { 0.0F, 1.0F, 1.0F };
  private static float[] _COLOR2 = { 1.0F, 1.0F, 0.0F };

  private static FileData FILEDATA = new FileData("data.txt");
  private static interpolatedData INTERPOLATEDDATA = new interpolatedData(FILEDATA, 100, 100);

  private static final roomDrawing room = new roomDrawing(FILEDATA, 2.0F, false, false, false, _COLOR2, 0.95F);
  private static final roomDrawing roomINTERPOLIZED = new roomDrawing(INTERPOLATEDDATA, 2.0F, false, false, true, _COLOR1, 0.95F);
  private static final openGLdraw axises = new coordinateSystem();

  private static TimeScrollBox TIME = new TimeScrollBox((int)FILEDATA.getFirstMeasureHour(), FILEDATA.getMeasuresAmount());
  private static PlayStop PLAY = new PlayStop(TIME);
  public static final int KEYPLAY = 90;
  public static final int KEYSTOP = 67;
  public static final int KEYPAUSE = 88;
  public static final int KEY_REGIME2D_3D = 86;
  private static final double Scroll_Amplitude = 0.1D;
  private double axisScale = 1.0D;

  private boolean mouseDown = false;
  private int[] tempdPointer = new int[2];
  private int[] Pointer = new int[2];
  private int axisRotated = 0;
  private boolean rotated = false;
  private boolean scaled = false;
  private boolean rotationFocusDropped = false;

  //private boolean LIGHTING = true;
  private boolean ENABLE_3DGRAPH_CURRENT = true;
  private boolean ENABLE_3DGRAPH_NEW     = ENABLE_3DGRAPH_CURRENT;
  
  private float[] saved3dposition = new float[16];
  private boolean position3dsaved = false;
  private static double MIN_PICTURE_SIZE_CUTOFF = 0.1D;
  private static double MAX_PICTURE_SIZE_CUTOFF = 10.0D;

  JavaRenderer()
  {
    TIME.addTimeScrollingListener(FILEDATA);
    TIME.addTimeScrollingListener(INTERPOLATEDDATA);
  }

  //@Override
  public void dispose(GLAutoDrawable gLDrawable){ 
  }

  public void display(GLAutoDrawable gLDrawable)
  {
    GL2 gl = gLDrawable.getGL().getGL2();

    //if (gl.glIsEnabled(2896) != this.LIGHTING)
    if(this.ENABLE_3DGRAPH_CURRENT!=this.ENABLE_3DGRAPH_NEW)
    {
      this.ENABLE_3DGRAPH_CURRENT = this.ENABLE_3DGRAPH_NEW;
      float graphTransparency;
      if (this.ENABLE_3DGRAPH_CURRENT) {
        //lightingEnable(gl);
        graphTransparency = 0.95F;
	if(position3dsaved)
		gl.glLoadMatrixf(saved3dposition, 0);

	//gl.glPopMatrix();
      }
      else {
        //lightingDisable(gl);
	
	//gl.glPushMatrix();
        gl.glGetFloatv(2982, saved3dposition, 0);
	position3dsaved = true;

	graphTransparency = 0.55F;
	gl.glLoadIdentity();
        gl.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
	/*
        gl.glLoadIdentity();
        gl.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        gl.glPushMatrix();
	*/
      }
      //gl.glPushMatrix();

      roomINTERPOLIZED.setGraphTransparency(graphTransparency);
      room.setGraphTransparency(graphTransparency);
    }

    gl.glClear(16384);
    gl.glClear(256);
    //gl.glLoadIdentity();




    // Before performing rotation\scaling, let's backup our current matrix 
    // In case something goes wrong, we will restore it 
    float[] goodmodel = new float[16];   
    gl.glGetFloatv(2982, goodmodel, 0);


    // Scale
    //gl.glPopMatrix();
    if (this.scaled) {
      gl.glScaled(this.axisScale, this.axisScale, this.axisScale);
      this.scaled = false;
    }

    // Rotate
    //if (this.LIGHTING) {
    if (this.ENABLE_3DGRAPH_CURRENT){
      rotated3d(gLDrawable);
    }
    else {
      rotated2d(gLDrawable);
    }


    // Check if the matrix is okay, if not -> load the previous good one
    double[] currentmodel = new double[16];
    gl.glGetDoublev(2982, currentmodel, 0);    
    boolean currentgood = true;
    double maxvalue = 0;
    for(int i=0; i<16; i++){
	    //System.out.format("currentmodel[i] = %f\n", currentmodel[i]);
	    if(Double.isNaN(currentmodel[i]))  currentgood = false;
	    if(i==3 || i==7 || i==11 || i>= 12 ) continue;  // We are not interested in the 4th coordinate for evaluating the scale of our picture!
	    if(maxvalue< Math.abs(currentmodel[i])) maxvalue = Math.abs(currentmodel[i]);
    }
    //System.out.format("maxvalue = %f\n", maxvalue);
    if(! currentgood || maxvalue < MIN_PICTURE_SIZE_CUTOFF || maxvalue>MAX_PICTURE_SIZE_CUTOFF){
	    gl.glLoadMatrixf(goodmodel, 0);
	    
	    /*
	    float[] model = new float[16];
	    gl.glGetFloatv(2982, model, 0);
	    System.out.format("\n");
	            for(int i=0; i<16; i++) System.out.format("model[i] = %f\n", model[i]);
	    */
	    //System.out.format("aha!\n");
    }

    //gl.glPushMatrix();

    room.GLdraw(gl);
    roomINTERPOLIZED.GLdraw(gl);
  }

  private void rotated3d(GLAutoDrawable gLDrawable)
  {
    GL2 gl = gLDrawable.getGL().getGL2();
    if (this.axisRotated != 0) {
      double[] vector = new double[3];
      if (this.mouseDown) {
        double x = this.Pointer[0];
        double y = gLDrawable.getSurfaceHeight() - this.Pointer[1];

        Geometry.FindVector(gLDrawable, glu, x, y, 0.0D, vector);
      }
      else if (Math.abs(this.axisRotated) == 2) {
        Geometry.FindScreenVector(gLDrawable, glu, 0.0D, 1.0D, 0.0D, vector);
      }
      else {
        Geometry.FindScreenVector(gLDrawable, glu, 1.0D, 0.0D, 0.0D, vector);
      }
      gl.glRotated(Math.signum(this.axisRotated) * 5.0F, vector[0], vector[1], vector[2]);
      this.axisRotated = 0;
    }
    if (this.rotated) {

      //System.out.println("test");

      double[] param = new double[4];
      double x1 = this.tempdPointer[0];
      double y1 = gLDrawable.getSurfaceHeight() - this.tempdPointer[1];
      //System.out.printf("gLDrawable.getSurfaceHeight() = %d \n", gLDrawable.getSurfaceHeight());
      double x2 = this.Pointer[0];
      double y2 = gLDrawable.getSurfaceHeight() - this.Pointer[1];

      if (Geometry.findRotateParametrsAlgorythm1(gLDrawable, glu, x1, y1, x2, y2, param)) {
        gl.glRotated(param[0], param[1], param[2], param[3]);
		  
        //System.out.println("  --->  test");
        
	this.tempdPointer[0] = this.Pointer[0];
        this.tempdPointer[1] = this.Pointer[1];
      }
      else {
        this.rotationFocusDropped = true;
      }
      this.rotated = false;
    }
  }

  private void rotated2d(GLAutoDrawable gLDrawable) {
    GL2 gl = gLDrawable.getGL().getGL2();
    if (this.axisRotated != 0) {
      double[] vector = new double[3];
      if (this.axisRotated > 0) {
        gl.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
      }
      else {
        gl.glRotated(-90.0D, 0.0D, 1.0D, 0.0D);
      }
      this.axisRotated = 0;
    }
    if (this.rotated)
      this.rotated = false;
  }

  public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged)
  {
  }

  public void init(GLAutoDrawable gLDrawable)
  {
    GL2 gl = gLDrawable.getGL().getGL2();

    gl.glShadeModel(7425);
    gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    gl.glClearDepth(1.0D);
    gl.glEnable(2929);
    gl.glDepthFunc(515);
    gl.glHint(3152, 4354);
    gl.glEnable(2903);
    try
    {
      this.texture = TextureIO.newTexture(new File(imageFile), true);
      gl.glTexEnvf(8960, 8704, 8449.0F);
      roomINTERPOLIZED.setRoomDraftTexture(this.texture.getTextureObject());
      roomINTERPOLIZED.setDraftPositionAndQuotient(APARTMENT.getData());
      roomINTERPOLIZED.switchDraftDrawing();

      roomINTERPOLIZED.setWalls(APARTMENT.roundWalls().getWalls());
      roomINTERPOLIZED.switchWallsDrawing();
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    //if (this.LIGHTING) lightingEnable(gl);
    if (this.ENABLE_3DGRAPH_CURRENT) lightingEnable(gl);

     
    //((Component) gLDrawable).addKeyListener(this);
    //((Component) gLDrawable).addKeyListener(this);
    //((Component) gLDrawable).addMouseMotionListener(this);
    //((Component) gLDrawable).addMouseListener(this);
    //((Component) gLDrawable).addMouseWheelListener(this);
              
  }

  private void lightingEnable(GL2 gl)
  {
    gl.glLightModelf(2898, 1.0F);
    gl.glEnable(2977);
    gl.glEnable(2896);
    gl.glEnable(16384);
    gl.glEnable(16385);

    gl.glLoadIdentity();
    float[] pos = { 3.0F, 3.0F, 3.0F, 1.0F };
    float[] dir = { -1.0F, -1.0F, -1.0F };

    float[] pos1 = { -3.0F, 3.0F, 3.0F, 1.0F };
    float[] dir1 = { 1.0F, -1.0F, -1.0F };

    gl.glLightfv(16385, 4611, pos1, 3);
    gl.glLightfv(16385, 4612, dir1, 2);

    float[] mat_specular = { 1.0F, 1.0F, 1.0F, 1.0F };
    gl.glMaterialfv(1032, 4610, mat_specular, 3);
    gl.glMaterialf(1032, 5633, 128.0F);
  }

  private void lightingDisable(GL2 gl)
  {
    gl.glDisable(16385);
    gl.glDisable(16384);
    gl.glDisable(2896);
  }

  private void switchLighting() {
    //this.LIGHTING = (!this.LIGHTING);
    this.ENABLE_3DGRAPH_NEW = ! this.ENABLE_3DGRAPH_CURRENT;
  }

  public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height)
  {
    GL2 gl = gLDrawable.getGL().getGL2();

    if (height <= 0) {
      height = 1;
    }

    float h = width / height;
    gl.glMatrixMode(5889);
    gl.glLoadIdentity();
    glu.gluPerspective(50.0D, h, 1.0D, 1000.0D);
    gl.glTranslatef(0.0F, 0.0F, -3.0F);
    gl.glMatrixMode(5888);
  }

  public void keyPressed(KeyEvent e)
  {
    action(e.getKeyCode());
  }
  public void action(int ACTION) {
    switch (ACTION) {
    case 27:
      JavaDia.bQuit = true;
      JavaDia.displayT = null;
      System.exit(0);
      break;
    case 33:
      TIME.largeIncrement();
      break;
    case 34:
      TIME.largeDecrement();

      break;
    case 90:
      Thread T = new Thread(PLAY);
      T.setDaemon(true);
      T.start();
      break;
    case 88:
      PLAY.pause();
      break;
    case 67:
      PLAY.pause();
      TIME.reset();
      break;
    case KeyEvent.VK_A:
      room.switchGraphDrawing();
      break;
    case KeyEvent.VK_S:
      roomINTERPOLIZED.switchGraphDrawing();
      break;
    case 86:
      roomINTERPOLIZED.switchWallsDrawing();
      roomINTERPOLIZED.switchGraphFalling();
      room.switchGraphFalling();
      switchLighting();
      break;
    case 66:
      room.switchGridNBorderDrawing();
      break;
    case 37:
      this.axisRotated = 2;
      break;
    case 39:
      this.axisRotated = -2;
      break;
    case 38:
      this.axisRotated = 1;
      break;
    case 40:
      this.axisRotated = -1;
    }
  }

  public TimeScrollBox getSlider()
  {
    return TIME;
  }

  public void keyReleased(KeyEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void mousePressed(MouseEvent e) {
	      //System.out.println("test2");
    if ((e.getModifiersEx() & 0x400) != 0)
    {
      int tmp25_22 = e.getX(); this.Pointer[0] = tmp25_22; this.tempdPointer[0] = tmp25_22;
      int tmp42_39 = e.getY(); this.Pointer[1] = tmp42_39; this.tempdPointer[1] = tmp42_39;
      this.rotationFocusDropped = false;
      this.mouseDown = true;
    }
  }

  public void mouseReleased(MouseEvent e)
  {
    this.mouseDown = false;
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseDragged(MouseEvent e) {
	      //System.out.println("test3");
    if (!this.rotationFocusDropped) {
                 
	        //System.out.println("test4");
      this.Pointer[0] = e.getX();
      this.Pointer[1] = e.getY();
      this.rotated = true;
    }
  }

  public void mouseMoved(MouseEvent e) {
  }

  public void mouseWheelMoved(MouseWheelEvent e) { this.axisScale = (1.0D + 0.1D * e.getWheelRotation());
    this.scaled = true; }

  private int genTexture(GL2 gl) {
    int[] tmp = new int[1];
    gl.glGenTextures(1, tmp, 0);
    return tmp[0];
  }
}

