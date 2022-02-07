package tester;

import java.io.IOException;
/**
 * Hello world!
 *
 */
public class RunPython 
{	
	public static void runP() 
	{ //need to call myscript.py and also pass arg1 as its arguments.
	  //and also myscript.py path is in C:\Demo\myscript.py
	

        String[] cmd = new String[2];

        // first argument is the program we want to open
        cmd[0] = "python";

        // second argument is a txt file we want to open with notepad
        cmd[1] = "C:/Users/Monica/Dropbox/workspaceMonica/TrainPlanning/src/main/java/tester/trainGraph.py";

 
	    try {
	    	Process process = Runtime.getRuntime().exec(cmd,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}