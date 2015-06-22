import Model.Audio;
import Model.Grid;

/*
 * Initiate and run the program.
 * Creates an audio class that holds the sound files
 * Creates a new grid of given size, passes the audio class.
 * Creates a new frame of the same size of the grid.
 * Creates a new controller to interface with the frame and model.
 */
public class Main {

	public static void main(String[] args) {
		
		Audio sound = new Audio();
		Grid grid = new Grid(9, sound);		
		Frame frame = new Frame(9);
		Controller controller = new Controller(frame, grid);
		
		controller.start(); //Run the program

		sound.shutdown(); //After program is exited
	}

}
