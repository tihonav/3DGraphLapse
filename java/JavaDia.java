import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class JavaDia
  implements Runnable
{
  static Thread displayT = new Thread(new JavaDia());
  static boolean bQuit = false;

  public static void main(String[] args) {
    displayT.start();
  }

  public void run()
  {
    JFrame frame = new JFrame("Jogl 3D Shape/Rotation");

    frame.setLayout(new GridBagLayout());
    frame.setSize(1024, 768);
    int size = frame.getExtendedState();
    size |= 6;
    frame.setExtendedState(size);
    frame.setUndecorated(true);

    GLCanvas canvas = new GLCanvas();
    JavaRenderer renderer = new JavaRenderer();
    canvas.addGLEventListener(renderer);
             
		
	     canvas.addMouseListener(renderer);
	     canvas.addKeyListener(renderer);
	     canvas.addMouseMotionListener(renderer);
	     canvas.addMouseWheelListener(renderer);


    controlPanel Panel = new controlPanel(renderer);

    frame.add(Panel, new GBC(0, 0, 1, 1, 100, 0));
    frame.add(canvas, new GBC(0, 1, 1, 1, 100, 100));

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        JavaDia.bQuit = true;
        System.exit(0);
      }
    });
    frame.setVisible(true);

    canvas.requestFocus();
    while (!bQuit)
      canvas.display();
  }
}

