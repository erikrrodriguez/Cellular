package Model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Grid {
	private ArrayList<NoteCell> noteCells;
	private ArrayList<NoteCell> delayedNoteCells; //Birthed cells are added to the grid 1 iteration after collision 
	private Set<GridCell> collisions;
	//private ArrayList<Coordinates> collisions;
	private ArrayList<GridCell> occupiedCells;
	private GridCell[][] grid;
	private int numCells; //number of cells in the grid
	private Random rand;
	private Audio sound;
	private OSCSend oscSend;
	private boolean clear;
	private boolean birth;
	private boolean osc;

	//constructor
	public Grid(int newNumCells, Audio sound, OSCSend oscSend) {
		noteCells = new ArrayList<NoteCell>();
		delayedNoteCells = new ArrayList<NoteCell>();
		collisions = new HashSet<GridCell>();
		//collisions = new ArrayList<Coordinates>();
		occupiedCells = new ArrayList<GridCell>();

		numCells = newNumCells;
		rand = new Random();
		grid = new GridCell[numCells][numCells];
		
		this.sound = sound;
		this.oscSend = oscSend;
		clear = true;
		birth = false;
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
		if (grid[x][y].getNoteCell(0) instanceof BirthCell || 
				grid[x][y].getNoteCell(1) instanceof BirthCell)
			return true;
		return false;
	}
	
	private boolean containsPlayerCell(int x, int y) {
		if (grid[x][y].getNoteCell(0).getPitch().equals("- ") || 
				grid[x][y].getNoteCell(1).getPitch().equals("- "))
			return true;
		return false;
	}

	private void addOccupied(GridCell gridCell) {
		if (!occupiedCells.contains(gridCell)) {
			occupiedCells.add(gridCell);
		}
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
		grid[newCell.getPos().getX()][newCell.getPos().getY()].addNoteCell(newCell);
		addOccupied(grid[newCell.getPos().getX()][newCell.getPos().getY()]);
		if (grid[newCell.getPos().getX()][newCell.getPos().getY()].getNumNoteCells() > 1 
				&& !collisions.contains(newCell.getPos())) {
			collisions.add(grid[newCell.getPos().getX()][newCell.getPos().getY()]);
		}

	}

	public void resetGrid() {
		for (NoteCell cell : noteCells) {
			grid[cell.getPos().getX()][cell.getPos().getY()].removeNoteCells();
		}
		occupiedCells.clear();
	}

	@SuppressWarnings("unused")
	public void clearGrid() {
		for (GridCell cell : occupiedCells) {
			cell.removeNoteCells();
		}
		for (NoteCell notecell : noteCells) {
			notecell = null;
		}
		occupiedCells.clear();
		noteCells.clear();
		clear = true;
	}

	public void clearCell(int x, int y) {
		for(NoteCell cell : grid[x][y].getOccupyingCells()){
			noteCells.remove(cell);
			cell = null;
		}
		grid[x][y].removeNoteCells();
		occupiedCells.remove(grid[x][y]);
	}

	public ArrayList<NoteCell> getCells(){
		return noteCells;
	}

	public ArrayList<GridCell> getOccupiedCells() {
		return occupiedCells;
	}

	public int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
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

	public void exportScore() {
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
		resetCells();
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
		notes = "<" + notes;
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
		notes = notes + ">";
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
