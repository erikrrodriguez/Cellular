package Model;
import java.util.ArrayList;
import java.util.Random;

public class Grid {
	private ArrayList<NoteCell> noteCells;
	private ArrayList<NoteCell> delayedNoteCells; //Birthed cells are added to the grid 1 iteration after collision 
	private ArrayList<Coordinates> collisions;
	private ArrayList<GridCell> occupiedCells;
	private GridCell[][] grid;
	private int numCells;
	private Random rand;
	private Audio sound;
	private boolean clear;
	private boolean birth;

	//constructor
	public Grid(int newNumCells, Audio sound) {
		noteCells = new ArrayList<NoteCell>();
		delayedNoteCells = new ArrayList<NoteCell>();
		collisions = new ArrayList<Coordinates>();
		occupiedCells = new ArrayList<GridCell>();

		numCells = newNumCells;
		rand = new Random();
		grid = new GridCell[numCells][numCells];
		this.sound = sound;
		clear = true;
		birth = false;
		setGrid();
	}
	
	public void setGrid() {
		for (int i = 0; i < numCells; i++) {
			for (int j = 0; j < numCells; j++) {
				grid[i][j] = new GridCell(i, j, sound);
			}
		}
	}

	public void update() {
		resetGrid();

		//move cells to new positions
		for(NoteCell noteCell : noteCells) {
			noteCell.advance();
			int x = noteCell.getPos().getX();
			int y = noteCell.getPos().getY();

			grid[x][y].addNoteCell(noteCell);
			addOccupied(grid[x][y]);

			if (grid[x][y].getNumNoteCells() > 1 && !collisions.contains(noteCell.getPos())) {
				collisions.add(noteCell.getPos());
			}
		}

		addDelayedCells(); //From births from previous collision round

		//Check Collisions
		if (collisions.size() > 0) {
			for(int i = 0; i < collisions.size(); i++) {
				int x = collisions.get(i).getX();
				int y = collisions.get(i).getY();
				grid[x][y].playNotes();

				if (birth) { //The birth option can be toggled
					if (grid[x][y].getNumNoteCells() == 2 && !containsBirthCell(x, y)) {
						int rnum = randInt(0, 3); //25% chance of birth
						if (rnum == 0) {
							NoteCell parent1 = grid[x][y].getNoteCell(0);
							//NoteCell parent2 = grid[x][y].getNoteCell(1);
							BirthCell birthCell = new BirthCell(x, y, parent1.getNote());
							delayedNoteCells.add(birthCell);
						}
					}
					if (grid[x][y].getNumNoteCells() >= 4) {
						clearCell(x, y);
					}
				}
			}
			collisions.clear();

		}
	}

	private boolean containsBirthCell(int x, int y) {
		if (grid[x][y].getNoteCell(0).getClass().equals(BirthCell.class) || 
				grid[x][y].getNoteCell(1).getClass().equals(BirthCell.class))
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
		NoteCell cell1 = new NoteCell(4, 3, "C 4");
		cell1.generateRandomPath();
		addNoteCell(cell1);

		NoteCell cell2 = new NoteCell(4, 5, "E 4");
		cell2.generateRandomPath();
		addNoteCell(cell2);

		NoteCell cell3 = new NoteCell(3,4, "G 4");
		cell3.generateRandomPath();
		addNoteCell(cell3);

		NoteCell cell4 = new NoteCell(5,4, "B 4");
		cell4.generateRandomPath();
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

}
