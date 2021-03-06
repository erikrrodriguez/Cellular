package Model;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * The grid is made up of grid cells, each at position (x,y). Each grid cell can hold multiple note cells.
 * Grid cells do not move, note cells move from grid cell to grid cell.
 */
public class GridCell {
	private ArrayList<NoteCell> occupyingCells;
	private ArrayList<String> containedPaths;
	private int numNoteCells;
	private int numUnplacedBirthCells;
	private int x;
	private int y;
	private int upCount, downCount, leftCount, rightCount;
	private Audio sound;
	private OSCSend oscSend;

	public GridCell(int newx, int newy, Audio sound, OSCSend oscSend) {
		containedPaths = new ArrayList<String>();
		occupyingCells = new ArrayList<NoteCell>();
		numNoteCells = 0;
		numUnplacedBirthCells = 0;
		x = newx;
		y = newy;
		this.sound = sound;
		this.oscSend = oscSend;
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
		if (cell instanceof BirthCell && !((BirthCell) cell).isPlaced())
			numUnplacedBirthCells++;
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

	public int getNumUnplacedBirthCells() {
		return numUnplacedBirthCells;
	}

	public void setNumUnplacedBirthCells(int num) {
		numUnplacedBirthCells = num;
	}

	public void removeNoteCells() {
		occupyingCells.clear();
		numNoteCells = 0;
		numUnplacedBirthCells = 0;
	}

	public void removeCell(NoteCell cell) {
		occupyingCells.remove(cell);
		numNoteCells--;
		if (cell instanceof BirthCell && !((BirthCell) cell).isPlaced())
			numUnplacedBirthCells--;
	}

	public ArrayList<NoteCell> getOccupyingCells() {
		return occupyingCells;
	}

	public void setOccupyingCells(ArrayList<NoteCell> array) {
		occupyingCells = array;
	}

	public String getOccupyingCellNotes() {
		String notes = "";
		for (NoteCell cell : occupyingCells) {
			notes = notes + cell.getScoreNote() + "_";
		}
		return notes;
	}

	/*
	 * Play the notes of each note cell in the grid cell.
	 */
	public void playNotes(boolean osc, int totalCells) {
		if (osc) {
			sendOSC(totalCells);
		} else {
			playNotes();
		}

	}

	private void playNotes() {
		double volume;
		// Adjust volume if multiple notes.
		switch (numNoteCells) {
		case 2:
			volume = 1.0;
			break;
		case 3:
			volume = 0.6;
			break;
		case 4:
			volume = 0.5;
			break;
		default:
			volume = 0.4;
			break;
		}
		int index;
		for (NoteCell cell : occupyingCells) {
			if (!cell.getPitch().equals("- ")) {
				if (cell.getOctave() < 4) {
					index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch()) / 2;
				} else if (cell.getOctave() > 6) {
					index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch()) / 2 + 24;
				} else {
					index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch())
							/ 2 + 12 * (cell.getOctave() - 4);
				}
				sound.playSound(index, volume);
			}
		}
	}

	private void sendOSC(int totalCells) {
		int[] array = new int[occupyingCells.size() + 3];
		array[0] = totalCells;
		array[1] = getX();
		array[2] = getY();
		int count = 3;
		int midiNote;
		for (NoteCell cell : occupyingCells) {
			if (!cell.getPitch().equals("- ")) {
				midiNote = "C C#D D#E F F#G G#A A#B ".indexOf(cell.getPitch())
						/ 2 + 12 * cell.getOctave();
				array[count] = midiNote;
				count++;
			}
		}
		try {
			oscSend.sendMsg(array);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addContainedPath(String dirColor) {
		containedPaths.add(dirColor);
	}

	public ArrayList<String> getContainedPaths() {
		return containedPaths;
	}

	public String getContainedPath(int index) {
		return containedPaths.get(index);
	}

	public void clearContainedPaths() {
		containedPaths.clear();
		setUpCount(0);
		setDownCount(0);
		setLeftCount(0);
		setRightCount(0);
	}

	public void setUpCount(int c) {
		upCount = c;
	}

	public void setDownCount(int c) {
		downCount = c;
	}

	public void setLeftCount(int c) {
		leftCount = c;
	}

	public void setRightCount(int c) {
		rightCount = c;
	}

	public int getUpCount() {
		return upCount;
	}

	public int getDownCount() {
		return downCount;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void incrementUpCount() {
		upCount += 1;
	}

	public void incrementDownCount() {
		downCount += 1;
	}

	public void incrementLeftCount() {
		leftCount += 1;
	}

	public void incrementRightCount() {
		rightCount += 1;
	}

}
