import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


class coordinateSystem
  implements openGLdraw
{
  public void GLdraw(GL2 gl)
  {
	
	     //GL2 gl = gl1.getGL2();
    gl.glBegin(1);
    gl.glColor3f(1.0F, 0.0F, 0.0F);

    gl.glVertex3f(0.0F, 0.0F, 0.0F);
    gl.glVertex3f(1.0F, 0.0F, 0.0F);

    gl.glVertex3f(0.0F, 0.0F, 0.0F);
    gl.glVertex3f(0.0F, 1.0F, 0.0F);
    gl.glVertex3f(0.0F, 0.0F, 0.0F);
    gl.glVertex3f(0.0F, 0.0F, 1.0F);

    gl.glEnd();
  }
}

