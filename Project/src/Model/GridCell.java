package Model;
import java.net.SocketException;
import java.net.UnknownHostException;
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
	private OSCSend oscSend;
	
	public GridCell(int newx, int newy, Audio sound, OSCSend oscSend) {
		occupyingCells = new ArrayList<NoteCell>();
		numNoteCells = 0;
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
	
	public void removeCell(NoteCell cell) {
		occupyingCells.remove(cell);
	}
	
	public ArrayList<NoteCell> getOccupyingCells() {
		return occupyingCells;
	}
	
	/*
	 * Play the notes of each note cell in the grid cell.
	 */
	public void playNotes(boolean osc) {
		if (osc) {
			sendOSC();
		}
		else{
			playNotes();
		}
		
	}
	
	private void playNotes() {
		double volume;
		//Adjust volume if multiple notes.
		switch(numNoteCells) {
			case 2: volume = 1.0; break;
			case 3: volume = 0.6; break;
			case 4: volume = 0.5; break;
			default: volume = 0.4; break;
		}
		int index;
		for(NoteCell cell : occupyingCells) {
			if (cell.getOctave() < 4) {
				index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch())/2;
			}
			else if (cell.getOctave() > 6) {
				index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch())/2 + 24;
			}
			else {
				index = "A A#B C C#D D#E F F#G G#".indexOf(cell.getPitch())/2 + 12*(cell.getOctave()-4);
			}
			sound.playSound(index, volume);
		}
	}
	
	private void sendOSC() {
		int[] array = new int[occupyingCells.size()+2];
		array[0] = getX();
		array[1] = getY();
		int count = 2;
		int midiNote;
		for(NoteCell cell : occupyingCells) {
			midiNote = "C C#D D#E F F#G G#A A#B ".indexOf(cell.getPitch())/2 + 12*cell.getOctave();
			System.out.println(midiNote);
			array[count] = midiNote;
			count++;
		}
//		try {
//			oscSend.sendMsg(array);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
