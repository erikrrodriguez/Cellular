import java.net.SocketException;
import java.net.UnknownHostException;

import Model.Audio;
import Model.Grid;
import Model.OSCSend;
import NewView.View;
import View.FirstTabPanel;
import View.MainFrame;
import View.OSCPanel;

/*
 * Initiate and run the program.
 * Creates an audio class that holds the sound files
 * Creates a new grid of given size, passes the audio class.
 * Creates a new frame of the same size of the grid.
 * Creates a new controller to interface with the frame and model.
 */
public class Main {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		Audio sound = new Audio();
		OSCSend oscSend = new OSCSend();
		Grid grid = new Grid(9, sound, oscSend);		
		//FirstTabPanel frame = new FirstTabPanel(9);
		
		//OSCPanel oscPanel = new OSCPanel();
		//MainFrame mainScreen = new MainFrame(frame, oscPanel);
		//Controller controller = new Controller(mainScreen, grid);
		
		View newFrame = new View(9);
		
		Controller controller = new Controller(newFrame, grid);
		
		controller.start(); //Run the program

		sound.shutdown(); //After program is exited
	}

}
