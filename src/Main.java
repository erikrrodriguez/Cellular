import Model.Audio;
import Model.Grid;

public class Main {

	public static void main(String[] args) {
		
		Audio sound = new Audio();
		Grid grid = new Grid(9, sound);		
		Frame frame = new Frame(9);
		Controller controller = new Controller(frame, grid);
		
		controller.start();

		sound.shutdown();
	}

}
