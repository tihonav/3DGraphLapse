import com.jogamp.opengl.GL2;
import java.util.*;

class geometryDraw implements openGLdraw
{

	private static String geometryExt = ".geometry";  

	private Vector<String> filenames = new Vector<String>();

	private void geometryDraw(String[] args)
	{
		for(int i=0; i<args.length; i++)
		{
			if(!args[i].toLowerCase().contains(geometryExt.toLowerCase())) continue;
			filenames.add(args[i]);
		}

			



	}
	
	public void GLdraw(GL2 paramGL)
	{
		
	}


	/*
	void getInputFiles(){
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + dataFolder+fileExt);
        }
	*/


}
