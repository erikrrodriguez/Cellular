package Model;
import java.util.ArrayList;

/*
 * The grid is made up of grid cells, each at position (x,y). Each grid cell can hold multiple note cells.
 * Grid cells do not move, note cells move from grid cell to grid cell.
 */
public class GridCell{
	private ArrayList<NoteCell> occupyingCells;
	private int numNoteCells;
	private int x;
	private int y;
	private Audio sound;
	
	public GridCell(int newx, int newy, Audio sound) {
		occupyingCells = new ArrayList<NoteCell>();
		numNoteCells = 0;
		x = newx;
		y = newy;
		this.sound = sound;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void addNoteCell(NoteCell cell) {
		occupyingCells.add(cell);
		numNoteCells++;
	}
	
	public NoteCell getNoteCell() {
		return occupyingCells.get(0);
	}
	
	public NoteCell getNoteCell(int index) {
		return occupyingCells.get(index);
	}
	
	public int getNumNoteCells() {
		return numNoteCells;
	}

	public void removeNoteCells() {
			occupyingCells.clear();
			numNoteCells = 0;
	}
	
	public ArrayList<NoteCell> getOccupyingCells() {
		return occupyingCells;
	}
	
	/*
	 * Play the notes of each note cell in the grid cell.
	 */
	public void playNotes() {
		double volume = 0;
		
		//Adjust volume if multiple notes. This is currently not working I'm not sure why.
		switch(numNoteCells) {
			case 1: volume = 1;
			case 2: volume = .6;
			case 3: volume = .5;
		}
		for(NoteCell cell : occupyingCells) {
			int index = "A ASB C CSD DSE F FSG GS".indexOf(cell.getPitch())/2 + 12*(cell.getOctave()-4);
			System.out.println(index);
			sound.playSound(index, volume);
		}
		
	}

}
