import com.jogamp.opengl.GL;

abstract interface openGLdraw2dElement
{
  public abstract void GLdraw(GL paramGL, float paramFloat);

  public abstract boolean isEnabled();
}

