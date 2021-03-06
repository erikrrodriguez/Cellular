package Model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/*
 * Note cells move around the grid in paths or loops. Loops will automatically restart, paths will move backwards
 * once the note cell reaches the end. Each note cell has a musical note attributed to it and a random color.
 * It will play it's note when it collides with another note cell.
 */
public class NoteCell {
	protected Coordinates curPos;
	protected int gridSize;
	private String note; // Holds Pitch and Octave
	private String pitch;
	private int octave;
	private Boolean loop; // Whether or not the path is a loop
	private Boolean reverse; // Whether the note is currently moving backwards
								// along it's path.
	private ArrayList<Coordinates> path;
	private int pathPos;
	private int pathLength;
	private Random rand = new Random();
	protected Color color;
	private int furthestX;
	private int furthestY;

	// Constructor for Generated Cells
	public NoteCell(int x, int y, String newNote, int newGridSize) {
		gridSize = newGridSize;
		curPos = new Coordinates(x, y, gridSize);
		furthestX = x;
		furthestY = y;
		note = newNote;
		pitch = note.substring(0, 2);
		octave = Integer.parseInt(note.substring(2));

		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = -1;
		pathLength = 1;
		setRandomColor();
	}

	public NoteCell(int x, int y, String newNote, Color color, int newGridSize) {
		gridSize = newGridSize;
		curPos = new Coordinates(x, y, gridSize);
		furthestX = x;
		furthestY = y;
		note = newNote;
		pitch = note.substring(0, 2);
		octave = Integer.parseInt(note.substring(2));
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = -1;
		pathLength = 1;
		setColor(color);
	}

	// Constructor for importing notecells. So that colors are not darkened or
	// lightened repeatedly
	public NoteCell(int x, int y, String newNote, Color color, int newGridSize,
			boolean importing) {
		gridSize = newGridSize;
		curPos = new Coordinates(x, y, gridSize);
		furthestX = x;
		furthestY = y;
		note = newNote;
		pitch = note.substring(0, 2);
		octave = Integer.parseInt(note.substring(2));
		loop = false;
		reverse = false;
		path = new ArrayList<Coordinates>();
		path.add(curPos);
		pathPos = -1;
		pathLength = 1;
		if (importing) {
			this.color = color;
		} else {
			setColor(color);
		}
	}

	/*
	 * Constructor for Drawn Cell Paths
	 */
	public NoteCell(String newNote, Color newColor,
			ArrayList<Coordinates> newPath, int newGridSize) {
		gridSize = newGridSize;
		path = new ArrayList<Coordinates>(newPath);
		setFurthestXY();
		curPos = path.get(0);
		pathPos = -1;
		note = newNote; // pitch+octave
		pitch = note.substring(0, 2);
		octave = Integer.parseInt(note.substring(2));
		loop = false;
		reverse = false;
		if (path.size() > 1 && path.get(0).equals(path.get(path.size() - 1))) {
			path.remove(path.size() - 1);
			loop = true;
		}
		if (loop) {
			pathLength = path.size();
		} else {
			pathLength = 2 * path.size() - 2;
		}
		setColor(newColor);
	}

	public void setFurthestXY() {
		for (int i = 0; i < path.size(); i++) {
			furthestX = Math.max(furthestX, path.get(i).getX());
			furthestY = Math.max(furthestY, path.get(i).getY());
		}
	}

	public void changeGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	/*
	 * Creates a random path of random length.
	 */
	public void generateRandomPath() {
		int pathLength = randInt((int) Math.sqrt(gridSize), gridSize * 3);
		int newX, newY;
		outerloop: for (int i = 0; i < pathLength; i++) {
			if (openNeighbor(path.get(i).getX(), path.get(i).getY())) {
				do {
					newX = randInt(path.get(i).getX() - 1,
							path.get(i).getX() + 1);
					newY = randInt(path.get(i).getY() - 1,
							path.get(i).getY() + 1);
				} while (!path.get(i).isNeighbor(newX, newY, gridSize)
						|| pathContains(newX, newY));
				addToPath(newX, newY);
				if (loop || !openNeighbor(newX, newY))
					break outerloop;
			}
		}
	}

	public void setPathLength() {
		if (loop) {
			pathLength = path.size();
		} else {
			pathLength = 2 * path.size() - 2;
		}
	}

	private boolean openNeighbor(int x, int y) {
		int openCount = 4;
		if (pathContains(x - 1, y) || x - 1 < 0) {
			openCount--;
		}
		if (pathContains(x + 1, y) || x + 1 > gridSize - 1) {
			openCount--;
		}
		if (pathContains(x, y + 1) || y + 1 > gridSize - 1) {
			openCount--;
		}
		if (pathContains(x, y - 1) || y - 1 < 0) {
			openCount--;
		}
		if (openCount > 0) {
			return true;
		}
		return false;
	}

	private boolean pathContains(int x, int y) {
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getX() == x && path.get(i).getY() == y)
				return true;
		}
		return false;
	}

	/*
	 * Returns true if the path already contains the coordinate cell. But DOES
	 * NOT count the starting cell to allow for loop creation
	 */
	public boolean pathContains(Coordinates testCoor) {
		for (int i = 1; i < path.size(); i++) {
			if (path.get(i).equals(testCoor))
				return true;
		}
		return false;
	}

	public String getPathColorString() {
		String pathstring = "";
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		for (int i = 0; i < path.size(); i++) {
			pathstring = pathstring + path.get(i).getX() + ":"
					+ path.get(i).getY() + ":" + r + ":" + g + ":" + b + " ";
		}
		return pathstring;
	}

	public String getPathString() {
		String pathstring = "";
		for (int i = 0; i < path.size(); i++) {
			pathstring = pathstring + path.get(i).getX() + ","
					+ path.get(i).getY() + "_";
		}
		return pathstring;
	}

	public void addToPath(int x, int y) {
		Coordinates coor = new Coordinates(x, y, gridSize);
		if (path.get(0).equals(coor)) {
			loop = true;
			reverse = false;
		} else {
			path.add(coor);
			loop = false;
			reverse = true;
		}
		setPathLength();
		furthestX = Math.max(furthestX, x);
		furthestY = Math.max(furthestX, y);
	}

	public void setColor(Color newColor) {
		color = newColor;
		int diff = getOctave() - 5;
		if (diff < 3) {
			color = color.darker();
		}
		if (diff > 3) {
			color = color.brighter();
		}
	}

	public void setRandomColor() {
		float hue = rand.nextFloat();
		float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
		float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
		color = Color.getHSBColor(hue, saturation, luminance);
		switch (getOctave()) {
		case 4:
			color = color.darker();
			break;
		case 6:
			color = color.brighter();
			break;
		default:
			break;
		}
	}

	/*
	 * Sets the position of the cell to the next coordinate in the path or loop.
	 * Reverses if necessary.
	 */
	public void advance() {
		if (path.size() > 1 && pathPos == -1) {
			pathPos++;
		} else if (path.size() != 1) {
			if (loop && reverse) {
				if (pathPos > 0) {
					pathPos--;
				} else {
					pathPos = path.size() - 1;
				}
			}

			if (loop && !reverse) {
				if (pathPos < path.size() - 1) {
					pathPos++;
				} else {
					pathPos = 0;
				}
			}

			if (!loop) {
				if (reverse == false && pathPos < path.size() - 1) {
					pathPos++;
				} else if (reverse == false && pathPos == path.size() - 1) {
					pathPos--;
					reverse = true;
				} else if (reverse == true && pathPos > 0) {
					pathPos--;
				} else if (reverse == true && pathPos == 0) {
					pathPos++;
					reverse = false;
				}
			}
			curPos = path.get(pathPos);
		}
	}

	public Coordinates getPos() {
		return curPos;
	}

	public void setPos(int index) {
		curPos = path.get(index);
		pathPos = index - 1;
	}

	/**
	 * Returns the entire note: "C 5", "AS4", ect
	 * 
	 * @return
	 */
	public String getNote() {
		return note;
	}

	public String getScoreNote() {
		return note;
	}

	/**
	 * returns the pitch. C, CS, ect.
	 * 
	 * @return
	 */
	public String getPitch() {
		return pitch;
	}

	/*
	 * returns only the octave: 0 through 9
	 */
	public int getOctave() {
		return octave;
	}

	public ArrayList<Coordinates> getPath() {
		return path;
	}

	public Coordinates getPath(int index) {
		return path.get(index);
	}

	public int getPathLength() {
		return pathLength;
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

	public int getFurthestX() {
		return furthestX;
	}

	public int getFurthestY() {
		return furthestY;
	}

	public int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

}
