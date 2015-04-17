package Model;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class NoteCell{
	private Coordinates curPos;
	private String note;
	private String notes = "C C#D D#E F F#G G#A A#B ";
	private int noteNum;
	private Boolean loop;
	private Boolean reverse;
	private ArrayList<Coordinates> path;
	private int pathPos;
	private Random rand = new Random();
	private Color color;

	//Constructor
	public NoteCell(int x, int y, String newNote) {
		curPos = new Coordinates(x, y);
		note = newNote.substring(0, 2);
		noteNum = setNoteNum(note);
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = 0;

		float hue = rand.nextFloat();
		float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		color = Color.getHSBColor(hue, saturation, luminance);
	}

	//Birth Constructor
	public NoteCell(int x, int y, int parnote1, int parnote2) {
		curPos = new Coordinates(x, y);
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = 0;

		noteNum = (parnote1 + parnote2) % 12;
		note = noteNumToNote(noteNum);

		generateRandomPath();

		float hue = rand.nextFloat();
		float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		color = Color.getHSBColor(hue, saturation, luminance);
	}
	
	public NoteCell(String note, Color newColor, ArrayList<Coordinates> newPath){
		this.path = new ArrayList<Coordinates>(newPath);
		curPos = path.get(0);
		this.note = note;
		noteNum = setNoteNum(this.note);
		loop = false;
		reverse = false;
		if (path.get(0).equals(path.get(path.size() - 1))){
			path.remove(path.size()-1);
			loop = true;
		}
		pathPos = 0;
		this.color = newColor;
	}
	
	public void generateRandomPath() {
		int pathLength = randInt(2, 15);	
		int newx = -1;
		int newy = -1;
		for(int i = 0; i < pathLength; i++) {
			Coordinates newCoor = new Coordinates(-1, -1);
			while (!path.get(i).isNeighbor(newCoor) || pathContains(newCoor)) {
				newx = randInt(path.get(i).getX()-1, path.get(i).getX()+1);
				newy = randInt(path.get(i).getY()-1, path.get(i).getY()+1);
				newCoor.set(newx, newy);
			}
			addToPath(newCoor);
			if (loop){
				break;
			}
		}
	}
	
	public boolean pathContains(Coordinates testCoor) {
		for(int i = 1; i < path.size(); i++) {
			if (path.get(i).equals(testCoor)) {
				return true;
			}
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


	private String noteNumToNote(int noteNum) {
		return notes.substring(2*noteNum, 2*noteNum+2);
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
			reverse = true;
		}

	}

	public void advance(){
//		System.out.println(pathPos);
		if (path.size() != 1) {
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

	public String getNote(){
		return note;
	}
	
	public String getOctave() {
		return note.substring(1, 2);
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

	public int setNoteNum(String note) {
		return (notes.indexOf(note.substring(0, 2)) / 2);
	}

	public int getNoteNum() {
		return noteNum;
	}

	public int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
