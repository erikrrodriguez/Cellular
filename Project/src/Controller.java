import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Model.Coordinates;
import Model.Grid;
import Model.GridCell;
import Model.NoteCell;

public class Controller {

	private Frame frame;	
	private Grid grid;
	private boolean running;
	private boolean pause;
	private int clickedCellX; //(x,y) position of the cell the user clicks
	private int clickedCellY;
	private ArrayList<Coordinates> drawnPath; //To hold the path that the user draws.

	public Controller(Frame frame, Grid grid) {
		drawnPath = new ArrayList<Coordinates>();
		this.frame = frame;
		this.grid = grid;
		
		frame.addListener(new Listener()); //Listener for all buttons
		frame.getPanel().addMouse(new Mouse());
		
		running = true;
		pause = true;
		
		frame.changeVisible(true);
		grid.update();
		
		sendCells(); //Transfers note cells from the grid to the frame
	}
	
	/*
	 * Main Loop
	 */
	public void start() {
		while (running) {
			if (!pause) {
				grid.update();
				sendCells(); //Update panel with new cell info
			}
			
			try { //Delay before next grid update
				TimeUnit.MILLISECONDS.sleep(250);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/*
	 * Get the note cells from the grid
	 */
	public ArrayList<NoteCell> getCells() {
		return grid.getCells();
	}
	
	/*
	 * Get the occupied grid cells
	 */
	public ArrayList<GridCell> getOccupiedCells() {
		return grid.getOccupiedCells();
	}
	
	/*
	 * Send the note cells, occupied grid cells, and the drawn path arraylist to the frame
	 */
	private void sendCells() {
		frame.getPanel().setCells(getCells(), getOccupiedCells(), drawnPath);
	}

	public boolean isPause() {
		return pause;
	}
	
	/*
	 * Remove last clicked cell from the drawn path
	 */
	public void removeCell(int x, int y) {
		if (drawnPath.get(drawnPath.size()-1).getX() == x && drawnPath.get(drawnPath.size()-1).getY() == y) {
			drawnPath.remove(drawnPath.size()-1);				
		}
	}
	
	/*
	 * Print drawn path.
	 */
	public void printPath() {
		String path = "";
		for(Coordinates coor : drawnPath) {
			path += "(" + coor.getX()+","+coor.getY()+") ";
		}
		System.out.println(path);
	}
	
	/*
	 * Listener for all buttons on the frame.
	 */
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent button) {
			String b = button.getActionCommand(); //Determine which button was pressed
			if (b == "startStop" && drawnPath.size() == 0) {
				pause = !pause;
				frame.changeVisible(pause);
				if (!pause){
					drawnPath.clear();
				}
			}
			else if (b == "clear" && drawnPath.size() == 0) {
				grid.clearGrid();
				pause = true;
				frame.changeVisible(pause);
				sendCells();
			}
			else if (b == "generate" && drawnPath.size() == 0) {
				grid.clearGrid();
				grid.generate(); //Generate some random cells
				pause = false;
				frame.changeVisible(pause);
				drawnPath.clear();
			}
			else if (b == "delete") {
				if (drawnPath.size() > 0) {
					drawnPath.clear();
				}
				else {
					grid.clearCell(clickedCellX, clickedCellY);
				}
			}
			else if (b == "birth") {
				grid.changeBirth();
			}
			else if (b == "insert" && pause) {
				if (frame.getNote() != "-" && frame.getOctave() != "-" && drawnPath.size() > 0) {
					String note = frame.getNote();
					String octave = frame.getOctave();
					Color color = frame.getColor();
					NoteCell noteCell = new NoteCell(note+octave, color, drawnPath);
					grid.addNoteCell(noteCell);
					drawnPath.clear();
				}
			}
		}
	}
	
	/*
	 * Mouse class for drawing a path
	 */
	private class Mouse implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent click) {
			clickedCellX = (int)(click.getX()/50); //Determine which grid cell is clicked
			clickedCellY = (int)(click.getY()/50);
			if (pause) {
				pathContains(clickedCellX, clickedCellY);
			}
		}
		
		private void pathContains(int x, int y) {
			boolean found = false;
			for (int i = 1; i < drawnPath.size(); i++){
				if (drawnPath.get(i).getX() == x && drawnPath.get(i).getY() == y) {
					drawnPath.remove(i);
					found = true;
					break;
				}
			}
			if (!found && drawnPath.size() == 0) {
				drawnPath.add(new Coordinates(x, y));
			}
			else if (!found && isNeighbor(drawnPath.get(drawnPath.size()-1).getX(), drawnPath.get(drawnPath.size()-1).getY(), x, y)){
				drawnPath.add(new Coordinates(x, y));
			}
		}
		
		private Boolean isNeighbor(int x, int y, int nx, int ny) {
			if ((nx==x && (y==ny-1 || y==ny+1)) || (ny==y && (x==nx-1 || x==nx+1))) {
				return true;
			}
			return false;
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
}