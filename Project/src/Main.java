import java.net.SocketException;
import java.net.UnknownHostException;

import Model.Audio;
import Model.Grid;
import Model.OSCSend;
import View.View;

/*
 * Initiate and run the program.
 * Creates an audio class that holds the sound files
 * Creates a new grid of given size, passes the audio class.
 * Creates a new frame of the same size of the grid.
 * Creates a new controller to interface with the frame and model.
 */
public class Main {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		int gridSize = 9;
		
		Audio sound = new Audio();
		OSCSend oscSend = new OSCSend();
		Grid grid = new Grid(gridSize, sound, oscSend);		
		View newFrame = new View(gridSize);
		
		Controller controller = new Controller(newFrame, grid);
		
		controller.start(); //Run the program

		sound.shutdown(); //After program is exited
	}

}
