import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


class openGLStatutusBar
  implements openGLdraw2dElement, TimeScrollingListener
{
  private final float LENGTH = 0.3F;
  private final float HEIGTH = 0.05F;
  private boolean ENABLED = true;
  private float STATUS;

  public void GLdraw(GL gl1, float zOffset)
  {
	      GL2 gl = gl1.getGL2(); 
    gl.glColor3f(1.0F, 1.0F, 1.0F);
    drawRectangle(gl, zOffset, 1.0F);
    gl.glColor3f(0.5F, 0.0F, 0.0F);
    drawRectangle(gl, zOffset, this.STATUS);
  }

  public boolean isEnabled()
  {
    return this.ENABLED;
  }
  public void setEnabled(boolean enabled) {
    this.ENABLED = enabled;
  }
  public void changeTimeMoment(TimeScrollBox time) {
    this.STATUS = ((float)time.getTimePointer());
  }
  private void drawRectangle(GL gl1, float zOffset, float qotient) {
    float POS = 0.9F;

	      GL2 gl = gl1.getGL2(); 

    gl.glBegin(7);
    gl.glVertex3d(-0.8999999761581421D, 0.8999999761581421D, zOffset);
    gl.glVertex3d(-0.9F + 0.3F * qotient, 0.8999999761581421D, zOffset);
    gl.glVertex3d(-0.9F + 0.3F * qotient, 0.8499999642372131D, zOffset);
    gl.glVertex3d(-0.8999999761581421D, 0.8499999642372131D, zOffset);
    gl.glEnd();
  }
}

