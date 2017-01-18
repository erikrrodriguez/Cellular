package Model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
	private ArrayList<NoteCell> noteCells;
	private ArrayList<NoteCell> delayedNoteCells; //Birthed cells are added to the grid 1 iteration after collision 
	private Set<GridCell> collisions;
	private Set<GridCell> gridCellsWithPaths;
	private Set<GridCell> occupiedCells;
	private GridCell[][] grid;
	private int numCells; //number of cells in the grid
	private Audio sound;
	private OSCSend oscSend;
	private boolean clear;
	private boolean birth;
	private boolean death;
	private boolean osc;

	//constructor
	public Grid(int newNumCells, Audio sound, OSCSend oscSend) {
		noteCells = new ArrayList<NoteCell>();
		delayedNoteCells = new ArrayList<NoteCell>();
		collisions = new HashSet<GridCell>();
		gridCellsWithPaths = new HashSet<GridCell>();
		occupiedCells = new HashSet<GridCell>();

		numCells = newNumCells;
		grid = new GridCell[numCells][numCells];

		this.sound = sound;
		this.oscSend = oscSend;
		clear = true;
		birth = false;
		death = false;
		osc = false;
		setGrid();
	}

	public void setGrid() {
		for (int i = 0; i < numCells; i++) {
			for (int j = 0; j < numCells; j++) {
				grid[i][j] = new GridCell(i, j, sound, oscSend);
			}
		}
	}

	public void update() {
		resetGrid();
		advanceCells();
		checkCollisions();
		addDelayedCells(); //Births from previous collision round
	}

	private void advanceCells() {
		for(NoteCell noteCell : noteCells) {
			noteCell.advance();
			int x = noteCell.getPos().getX();
			int y = noteCell.getPos().getY();

			grid[x][y].addNoteCell(noteCell);
			addOccupied(grid[x][y]);

			if (grid[x][y].getNumNoteCells() > 1) {
				collisions.add(grid[x][y]);
			}
		}
	}
	private void checkCollisions() {
		if (collisions.size() > 0) {
			int totalCells = 0;
			//get total number of cells across all collisions for OSC output.
			for(GridCell gridCell : collisions) {
				totalCells += gridCell.getNumNoteCells();
			}
			for(GridCell gridCell : collisions) {
				gridCell.playNotes(osc, totalCells);

				if (birth) { //The birth option can be toggled
					if (gridCell.getNumNoteCells() == 2 && !containsBirthCell(gridCell.getX(),gridCell.getY()) 
							&& !containsPlayerCell(gridCell.getX(), gridCell.getY())) birthNewCell(gridCell.getX(), gridCell.getY());
				}
				if (death) {
					if (gridCell.getNumNoteCells() >= 4) clearCell(gridCell.getX(), gridCell.getY());
				}
			}
			collisions.clear();
		}
	}

	private void birthNewCell(int x, int y) {
		int rnum = randInt(0, 15);
		if (rnum <= 3) {	//25% chance of birth
			NoteCell parNote1 = grid[x][y].getNoteCell(0);
			NoteCell parNote2 = grid[x][y].getNoteCell(1);
			String parPitch1 = parNote1.getPitch();
			String parPitch2 = parNote2.getPitch();
			int parOctave1 = parNote1.getOctave();
			int parOctave2 = parNote2.getOctave();
			String chldNote = "";
			if (rnum == 0) 	chldNote = parPitch1 + parOctave2;
			if (rnum == 1) 	chldNote = parPitch2 + parOctave1;
			if (rnum == 2) {
				int index1 = "A A#B C C#D D#E F F#G G#".indexOf(parPitch1);
				int index2 = "A A#B C C#D D#E F F#G G#".indexOf(parPitch2);
				int index = 2*((index1 + index2) % 11);
				chldNote = "A A#B C C#D D#E F F#G G#".substring(index, index+2);
				chldNote += (parOctave1 + parOctave2)/2;
			}
			if (rnum == 3) {
				int index1 = "A A#B C C#D D#E F F#G G#".indexOf(parPitch1);
				int index2 = "A A#B C C#D D#E F F#G G#".indexOf(parPitch2);
				int index = 2*(Math.abs((index1 - index2)) % 11);
				chldNote = "A A#B C C#D D#E F F#G G#".substring(index, index+2);
				chldNote += (parOctave1 + parOctave2)/2;
			}
			BirthCell birthCell = new BirthCell(x, y, chldNote, parNote1.getColor(), parNote2.getColor(), false, numCells);
			delayedNoteCells.add(birthCell);
		}
	}

	private boolean containsBirthCell(int x, int y) {
		for(NoteCell cell : grid[x][y].getOccupyingCells()) {
			if (cell instanceof BirthCell) return true;
		}
		return false;
	}

	private boolean containsPlayerCell(int x, int y) {
		for(NoteCell cell : grid[x][y].getOccupyingCells()) {
			if (cell.getPitch().equals("- ")) return true;
		}
		return false;
	}

	private void addOccupied(GridCell gridCell) {
		occupiedCells.add(gridCell);
	}

	private void addDelayedCells() {
		for (NoteCell cell : delayedNoteCells) {
			addNoteCell(cell);
			addOccupied(grid[cell.getPos().getX()][cell.getPos().getY()]);
		}
		delayedNoteCells.clear();
	}

	public void addNoteCell(NoteCell newCell){
		noteCells.add(newCell);
		int x = newCell.getPos().getX();
		int y = newCell.getPos().getY();
		grid[x][y].addNoteCell(newCell);
		addOccupied(grid[x][y]);
		fillPathInfo(newCell);
		if (grid[x][y].getNumNoteCells() > 1) {
			collisions.add(grid[x][y]);
		}
	}

	public void fillPathInfo(NoteCell cell) {
		//fill grid cells with this note cell's path
		int size = cell.getPath().size();
		if (size > 1) {
			String[] path = new String[size];
			String[] node1 = new String[5];
			String[] node2 = new String[5];
			path = cell.getPathColorString().split(" "); //array of ["x:y:r:g:b", "x:y:r:g:b"]
			int bound;
			if (path.length % 2 == 0) {
				bound = path.length;
			} else {
				bound = path.length - 1;
			}
			for(int i = 0; i < bound - 1; i++) {
				if (i==0) {
					node1 = path[i].split(":"); //array [x, y, r, g, b]
				} else {
					node1 = node2;
				}
				node2 = path[i+1].split(":");
				findDirection(node1, node2);
			}
			if (path.length % 2 != 0 && size >= 3) { //For odd length paths
				node1 = path[cell.getPath().size()-2].split(":"); //Penultimate node
				node2 = path[cell.getPath().size()-1].split(":"); //Last node
				findDirection(node1, node2);
			}
			if (cell.isLoop()) { //For loops
				node1 = path[cell.getPath().size()-1].split(":"); //last node
				node2 = path[0].split(":"); //first node
				findDirection(node1, node2);
			}
		}
	}

	public void refillPathInfo() {
		for(NoteCell cell : noteCells) {
			fillPathInfo(cell);
		}
	}

	public void findDirection(String[] node1, String[] node2) {
		String direction1;
		String direction2;
		int x1 = Integer.parseInt(node1[0]);
		int x2 = Integer.parseInt(node2[0]);
		int y1 = Integer.parseInt(node1[1]);
		int y2 = Integer.parseInt(node2[1]);
		//Find Direction
		if(x1 < x2) {
			direction1 = "right"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			direction2 = "left"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			grid[x1][y1].incrementRightCount();
			grid[x2][y2].incrementLeftCount();
		} else if(x1 > x2) {
			direction1 = "left"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			direction2 = "right"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			grid[x1][y1].incrementLeftCount();
			grid[x2][y2].incrementRightCount();
		} else if(y1 > y2) {
			direction1 = "up"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			direction2 = "down"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			grid[x1][y1].incrementUpCount();
			grid[x2][y2].incrementDownCount();
		} else {
			direction1 = "down"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			direction2 = "up"+":"+node1[2]+":"+node1[3]+":"+node1[4];
			grid[x1][y1].incrementDownCount();
			grid[x2][y2].incrementUpCount();
		}				
		grid[x1][y1].addContainedPath(direction1);
		grid[x2][y2].addContainedPath(direction2);
		gridCellsWithPaths.add(grid[x1][y1]);
		gridCellsWithPaths.add(grid[x2][y2]);
	}
	
	public void resetGrid() {
		for (GridCell cell : occupiedCells) {
			cell.removeNoteCells();
		}
		occupiedCells.clear();
	}

	@SuppressWarnings("unused")
	public void clearGrid() {
		for(GridCell cell : gridCellsWithPaths) {
			cell.clearContainedPaths();
		}
		for (GridCell cell : occupiedCells) {
			cell.removeNoteCells();
		}
		for (NoteCell notecell : noteCells) {
			notecell = null;
		}
		gridCellsWithPaths.clear();
		occupiedCells.clear();
		noteCells.clear();
		clear = true;
	}

	public void clearCell(int x, int y) {
		for(NoteCell cell : grid[x][y].getOccupyingCells()){
			noteCells.remove(cell);
			cell = null;
		}
		for(GridCell cell : gridCellsWithPaths) {
			cell.clearContainedPaths();
		}
		gridCellsWithPaths.clear();
		grid[x][y].removeNoteCells();
		occupiedCells.remove(grid[x][y]);
		refillPathInfo();
	}

	public ArrayList<NoteCell> getCells(){
		return noteCells;
	}

	public Set<GridCell> getOccupiedCells() {
		return occupiedCells;
	}

	public Set<GridCell> getGridCellsWithPaths() {
		return gridCellsWithPaths;
	}

	public int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public int getNumCells() {
		return numCells;
	}

	public void generate() {
		clear = false;
		NoteCell cell1 = new NoteCell(4, 3, "C 4", numCells);
		cell1.generateRandomPath();
		cell1.setPathLength();
		addNoteCell(cell1);

		NoteCell cell2 = new NoteCell(4, 5, "E 4", numCells);
		cell2.generateRandomPath();
		cell2.setPathLength();
		addNoteCell(cell2);

		NoteCell cell3 = new NoteCell(3,4, "G 4", numCells);
		cell3.generateRandomPath();
		cell3.setPathLength();
		addNoteCell(cell3);

		NoteCell cell4 = new NoteCell(5,4, "B 4", numCells);
		cell4.generateRandomPath();
		cell4.setPathLength();
		addNoteCell(cell4);

	}

	public GridCell getGridCell(int x, int y) {
		return grid[x][y];
	}

	public boolean isClear() {
		return clear;
	}

	public void changeBirth() {
		birth = !birth;
	}
	
	public void changeDeath() {
		death = !death;
	}

	public void changeOSC() {
		osc = !osc;
	}

	public boolean isOSC() {
		return osc;
	}

	public void resetCells() {
		deleteChildCells();
		resetGrid();
		for (NoteCell cell : noteCells) {
			cell.setPos(0);
		}
	}

	private void deleteChildCells() {
		Iterator<NoteCell> iter = noteCells.iterator();
		while (iter.hasNext()) {
			NoteCell cell = iter.next();
			if (cell instanceof BirthCell && !((BirthCell) cell).isPlaced()) {
				iter.remove();
				grid[cell.getPos().getX()][cell.getPos().getY()].removeCell(cell);
				if (grid[cell.getPos().getX()][cell.getPos().getY()].getNumNoteCells() == 0) {
					occupiedCells.remove(grid[cell.getPos().getX()][cell.getPos().getY()]);
				}
				cell = null;

			}
		}
	}

	public void setIpandPort(String Ip, String port) {
		try {
			oscSend.setIP(Ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oscSend.setPort(port);
	}

	public void exportScore() {
		if (noteCells.size() > 0) {
			//Find LCM for cycle length
			int index = 0;
			int[] LCMcalc = new int[noteCells.size()];
			for(NoteCell cell : noteCells) {
				LCMcalc[index] = cell.getPathLength();
				System.out.println("LCM " + index + ": " + LCMcalc[index]);
				index++;
			}
			int cycleLength = lcm(LCMcalc);
			System.out.println("Cycle Length: " + cycleLength);
			//Advance Cells and write to file
			//resetCells();
			try (BufferedWriter file = new BufferedWriter(new FileWriter("score.txt",false))) { //change to true to over write
				file.write("\\version \"2.18.2\"");
				file.newLine();
				file.write("\\language \"english\"");
				file.newLine();
				file.write("\\repeat volta 2");
				file.newLine();
				file.write("{");
				file.newLine();
				file.write("#(set-accidental-style 'forget)");
				file.newLine();
				for(int i = 0; i < cycleLength; i++) {
					resetGrid();
					advanceCells();
					String notes = checkCollisionsExportScore(); //This version does not play notes
					if (notes.equals("<>")) notes = "r";
					file.write(notes);
					file.newLine();
				}
				file.write("}");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resetCells();
		}
	}

	private String checkCollisionsExportScore() {
		String notes = "";
		if (collisions.size() > 0) {
			for(GridCell gridCell : collisions) {
				notes = notes + gridCell.getOccupyingCellNotes()+ "_";
			}
			collisions.clear();
			//notes is now the full string of notes played this step
		}
		notes = cleanFormat(notes);
		return notes;
	}

	/*
	 * Transform notes list into LilyPond syntax
	 */
	private String cleanFormat(String notes) {
		notes = notes.replaceAll(" ", "");
		notes = notes.toLowerCase();
		notes = notes.replaceAll("#", "s");
		notes = notes.replaceAll("0", ",,,");
		notes = notes.replaceAll("1", ",,");
		notes = notes.replaceAll("2", ",");
		notes = notes.replaceAll("3", "");
		notes = notes.replaceAll("4", "'");
		notes = notes.replaceAll("5", "''");
		notes = notes.replaceAll("6", "'''");
		notes = notes.replaceAll("7", "''''");
		notes = notes.replaceAll("8", "'''''");
		notes = notes.replaceAll("9", "''''''");
		notes = notes.replaceAll("_", " ");
		notes = "<" + notes + ">";
		return notes;
	}

	/*
	 * All to find the LCM of an array
	 */
	private static int gcd(int a, int b)
	{
		while (b > 0)
		{
			int temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}	
	private int lcm(int a, int b)
	{
		return a * (b / gcd(a, b));
	}
	private int lcm(int[] input)
	{
		int result = input[0];
		for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
		return result;
	}


}
