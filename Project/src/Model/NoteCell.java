package Model;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/*
 * Note cells move around the grid in paths or loops. Loops will automatically restart, paths will move backwards
 * once the note cell reaches the end. Each note cell has a musical note attributed to it and a random color.
 * It will play it's note when it collides with another note cell.
 */
public class NoteCell{
	protected Coordinates curPos;
	private String note; //Holds Pitch and Octave
	private Boolean loop; //Whether or not the path is a loop
	private Boolean reverse; //Whether the note is currently moving backwards along it's path.
	private ArrayList<Coordinates> path;
	private int pathPos;
	private Random rand = new Random();
	protected Color color;

	//Constructor for Generated Cells
	public NoteCell(int x, int y, String newNote) {
		curPos = new Coordinates(x, y);
		note = newNote;
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = -1;
		setRandomColor();
	}

	public NoteCell(int x, int y, String newNote, Color color) {
		curPos = new Coordinates(x, y);
		note = newNote;
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = -1;
		setColor(color);
	}

	/*
	 * Constructor for Drawn Cell Paths
	 */
	public NoteCell(String newNote, Color newColor, ArrayList<Coordinates> newPath){
		path = new ArrayList<Coordinates>(newPath);
		curPos = path.get(0);
		pathPos = -1;
		note = newNote;
		loop = false;
		reverse = false;
		if (path.get(0).equals(path.get(path.size() - 1))){
			path.remove(path.size()-1);
			loop = true;
		}
		setColor(newColor);
	}

	/*
	 * Creates a random path of random length.
	 */
	public void generateRandomPath() {
		//System.out.println(curPos.getX() + " " + curPos.getY());
		int pathLength = randInt(4, 20);	
		int newx = randInt(curPos.getX()-1, curPos.getX()+1);
		int newy = randInt(curPos.getY()-1, curPos.getY()+1);
		for(int i = 0; i < pathLength; i++) {
			Coordinates newCoor = new Coordinates(newx, newy);
			while (!path.get(i).isNeighbor(newCoor) || pathContains(newCoor)) {
				newx = randInt(path.get(i).getX()-1, path.get(i).getX()+1);
				newy = randInt(path.get(i).getY()-1, path.get(i).getY()+1);
				newCoor.set(newx, newy);
			}
			addToPath(newCoor);
			if (loop || noOpenNeighbors(path.get(i+1).getX(), path.get(i+1).getY())){
				break;
			}
		}
	}
	private boolean noOpenNeighbors(int x, int y) {
		int lboundx = x;
		int uboundx = x;
		int lboundy = y;
		int uboundy = y;
		if (x != 0) lboundx = x-1;
		if (x != 8) uboundx = x+1;
		if (y != 0) lboundy = y-1;
		if (y != 8) uboundy = y+1;
		for(int i = lboundx; i <= uboundx; i++) {
			for(int j = lboundy; j <= uboundy; j++) {
				if (!pathContains(i, j)) return false;
			}
		}
		return true;
	}

	private boolean pathContains(int x, int y) {
		for(int i = 0; i < path.size(); i++) {
			if (path.get(i).getX() == x && path.get(i).getY() == y)	return true;
		}
		return false;
	}
	/*
	 * Returns true if the path already contains the coordinate cell. But DOES NOT count
	 * the starting cell to allow for loop creation
	 */
	public boolean pathContains(Coordinates testCoor) {
		for(int i = 1; i < path.size(); i++) {
			if (path.get(i).equals(testCoor)) return true;
		}
		return false;
	}

	public void printPath() {
		String pathstring = "";
		for(int i = 0; i < path.size(); i++) {
			pathstring = pathstring + "(" + path.get(i).getX() + "," + path.get(i).getY() + ")  ";
		}
		System.out.println(pathstring);
	}

	public void addToPath(int x, int y){
		Coordinates coor = new Coordinates(x, y);
		if (path.get(0).equals(coor)) {
			loop = true;
			reverse = false;
		}
		else {
			path.add(coor);
			loop = false;
			reverse = true;
		}
	}

	private void addToPath(Coordinates newCoor) {
		if (path.get(0).equals(newCoor)) {
			loop = true;
		}
		else {
			path.add(newCoor);
			loop = false;
		}

	}

	public void setColor(Color newColor) {
		color = newColor;
		switch(getOctave()){
		case 4: color = color.darker();
		break;
		case 6: color = color.brighter();
		break;
		default: break;
		}
	}

	public void setRandomColor() {
		float hue = rand.nextFloat();
		float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		color = Color.getHSBColor(hue, saturation, luminance);
		switch(getOctave()){
		case 4: color = color.darker();
		break;
		case 6: color = color.brighter();
		break;
		default: break;
		}
	}

	/*
	 * Sets the position of the cell to the next coordinate in the path or loop. Reverses if necessary.
	 */
	public void advance(){
		if (pathPos == -1) {
			pathPos++;
		}
		else if (path.size() != 1) {
			if (loop && reverse) {
				if (pathPos > 0) {
					pathPos--;
				}
				else {
					pathPos = path.size()-1;
				}
			}

			if (loop && !reverse) {
				if (pathPos < path.size()-1) {
					pathPos++;
				}
				else {
					pathPos = 0;
				}
			}

			if (!loop) {
				if (reverse == false && pathPos < path.size()-1) {
					pathPos++;
				}
				else if (reverse == false && pathPos == path.size()-1) {
					pathPos--;
					reverse = true;
				}
				else if (reverse == true && pathPos > 0) {
					pathPos--;
				}
				else if (reverse == true && pathPos == 0) {
					pathPos++;
					reverse = false;
				}
			}
			curPos = path.get(pathPos);			
		}
	}

	public Coordinates getPos(){
		return curPos;
	}

	public void setPos(int index) {
		curPos = path.get(index);
		pathPos = index-1;
	}

	/**
	 * Returns the entire note: "C 5", "AS4", ect
	 * @return
	 */
	public String getNote() {
		return note;
	}

	/**
	 * returns the pitch. C, CS, ect.
	 * @return
	 */
	public String getPitch(){
		return note.substring(0, 2);
	}

	/*
	 * returns only the octave: 4,5, or 6.
	 */
	public int getOctave() {
		return Integer.parseInt(note.substring(2, 3));
	}

	public ArrayList<Coordinates> getPath() {
		return path;
	}

	public Color getColor() {
		return color;
	}

	public Boolean isLoop() {
		return loop;
	}

	public void reverse() {
		reverse = !reverse;
	}

	public int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
