import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import Model.BirthCell;
import Model.Coordinates;
import Model.Grid;
import Model.GridCell;
import Model.NoteCell;
import NewView.View;
import View.MainFrame;

public class Controller {

	private View mainScreen; //MainFrame
	private Grid grid;
	private boolean running;
	private boolean pause;
	private int clickedCellX; //(x,y) position of the cell the user clicks
	private int clickedCellY;
	private ArrayList<Coordinates> drawnPath; //To hold the path that the user draws.

	public Controller(View frame, Grid grid) { //MainFrame
		drawnPath = new ArrayList<Coordinates>();
		this.mainScreen = frame;
		this.grid = grid;

		mainScreen.getFrame().addListener(new Listener()); //Listener for all buttons
		mainScreen.getFrame().getPanel().addMouse(new Mouse());

		running = true;
		pause = true;
		grid.update();

		updateView(); //Transfers note cells from the grid to the frame
	}

	/*
	 * Main Loop
	 */
	public void start() {
		while (running) {
			if (!pause) {
				grid.update();
				updateView(); //Update panel with new cell info
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
	 * Send the note cells, occupied grid cells, and the drawn path array list to the frame
	 */
	private void updateView() {
		mainScreen.getFrame().getPanel().setCells(getCells(), getOccupiedCells(), drawnPath);
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
			switch(b) {
			case "startStop": startStop(); break;
			case "clear": clear(); break;
			case "generate": generate(); break;
			case "delete": delete(); break;
			case "insert": insert(); break;
			case "birth": grid.changeBirth(); break;
			case "reset": reset(); break;
			case "OSC": setOSC(); break;
			default: break;
			}
			updateView();
		}
		public void setOSC() {
			grid.changeOSC();
		}
		public void reset() {
			grid.resetCells();
		}
		public void startStop() {
			if (drawnPath.size() == 0) {
				pause = !pause;
			}
		}
		public void clear() {
			drawnPath.clear();
			grid.clearGrid();
			pause = true;
		}
		public void generate() {
			drawnPath.clear();
			grid.clearGrid();
			grid.generate(); //Generate some random cells
			pause = false;
		}
		public void delete() {
			if (drawnPath.size() > 1) {
				drawnPath.clear();
			}
			else {
				grid.clearCell(clickedCellX, clickedCellY);
				drawnPath.clear();
			}
		}
		public void insert() {
			if (mainScreen.getFrame().getPitch() != "- "	&& 
					mainScreen.getFrame().getOctave() != "-" && 
							mainScreen.getFrame().getPath() == "Birth") {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				BirthCell birthCell = new BirthCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, color, true);
				grid.addNoteCell(birthCell);
				drawnPath.clear();
			}
			if (mainScreen.getFrame().getPitch() != "- "	&& 
					mainScreen.getFrame().getOctave() != "-" && 
							mainScreen.getFrame().getPath() == "Drawn" &&
								drawnPath.size() > 0) {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				NoteCell noteCell = new NoteCell(note+octave, color, drawnPath);
				grid.addNoteCell(noteCell);
				drawnPath.clear();
			}
			if (mainScreen.getFrame().getPitch() != "- "	&& 
					mainScreen.getFrame().getOctave() != "-" && 
							mainScreen.getFrame().getPath() == "Random") {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				NoteCell noteCell = new NoteCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color);
				noteCell.generateRandomPath();
				grid.addNoteCell(noteCell);
				drawnPath.clear();
			}
			if (mainScreen.getFrame().getPitch() == "- " && //Insertion of Player cells with various paths
					mainScreen.getFrame().getOctave() != "-") {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				if (mainScreen.getFrame().getPath() == "Random") {
					NoteCell noteCell = new NoteCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color);
					noteCell.generateRandomPath();
					grid.addNoteCell(noteCell);
					drawnPath.clear();
				}
				if (mainScreen.getFrame().getPath() == "Drawn") {
					NoteCell noteCell = new NoteCell(note+octave, color, drawnPath);
					System.out.println(note+octave);
					grid.addNoteCell(noteCell);
					drawnPath.clear();
				}
				if (mainScreen.getFrame().getPath() == "Birth") {
					BirthCell birthCell = new BirthCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, color, true);
					grid.addNoteCell(birthCell);
					drawnPath.clear();
				}
			}
		}
	}

	/*
	 * Mouse class for drawing a path
	 */
	private class Mouse implements MouseListener, MouseMotionListener {
		private boolean drag = false;
		private int lastDragCellX = -1;
		private int lastDragCellY = -1;
		
		private void drawnPathAddRemove(int x, int y) {
			if (drawnPath.size() == 0) {
				drawnPath.add(new Coordinates(x, y));
				lastDragCellX = clickedCellX;
				lastDragCellY = clickedCellY;
			}
			else {
				Coordinates start = drawnPath.get(0); 
				Coordinates last  = drawnPath.get(drawnPath.size()-1);
				int size = drawnPath.size();
				if (x == last.getX() && y == last.getY()) {
					drawnPath.remove(drawnPath.size() - 1);
					lastDragCellX = clickedCellX;
					lastDragCellY = clickedCellY;
				}
				else if (drag && size > 1 && x == drawnPath.get(drawnPath.size()-2).getX() && y == drawnPath.get(drawnPath.size()-2).getY()) {
					drawnPath.remove(drawnPath.size()-1);
					lastDragCellX = x;
					lastDragCellY = y;
				}
				else if (size > 2 && x == start.getX() && y == start.getY() && isNeighbor(last.getX(), last.getY(), x, y)) {
					drawnPath.add(new Coordinates(x, y));
					lastDragCellX = clickedCellX;
					lastDragCellY = clickedCellY;
				}
				else {
					boolean found = false;
					for (int i = 1; i < drawnPath.size(); i++){
						if (drawnPath.get(i).getX() == x && drawnPath.get(i).getY() == y) {
							//drawnPath.remove(i); //uncommenting this allows for looped triangle paths and skipped squares
							found = true;
							break;
						}
					}
					if (!found && isNeighbor(last.getX(), last.getY(), x, y) && !(last.equals(start) && size > 2)) {
						drawnPath.add(new Coordinates(x,y));
						lastDragCellX = clickedCellX;
						lastDragCellY = clickedCellY;
					}
				}				
			}			
		}
		
		private Boolean isNeighbor(int x, int y, int nx, int ny) {
			if ((nx==x && (y==ny-1 || y==ny+1)) || (ny==y && (x==nx-1 || x==nx+1))) {
				return true;
			}
			return false;
		}
		
		@Override
		public void mousePressed(MouseEvent click) {
			if(SwingUtilities.isRightMouseButton(click)){ //right click delete grid cell
				grid.clearCell((int)(click.getX()/mainScreen.getCellSize()), (int)(click.getY()/mainScreen.getCellSize()));
				updateView();
			}
			else {
				clickedCellX = (int)(click.getX()/mainScreen.getCellSize()); //Determine which grid cell is clicked
				clickedCellY = (int)(click.getY()/mainScreen.getCellSize());
				drawnPathAddRemove(clickedCellX, clickedCellY);
				drag = true;
				updateView();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			drag = false;
			lastDragCellX = -1;
			lastDragCellY = -1;
		}
		
		@Override
		public void mouseDragged(MouseEvent mouseDrag) {
			if (drag) {
				int mouseDragCellX = (int)(mouseDrag.getX()/mainScreen.getCellSize());
				int mouseDragCellY = (int)(mouseDrag.getY()/mainScreen.getCellSize());
				if (mouseDragCellX != lastDragCellX || mouseDragCellY != lastDragCellY && 
						mouseDrag.getX() < mainScreen.getGamePanelSize() && mouseDrag.getY() < mainScreen.getGamePanelSize()) {
					clickedCellX = mouseDragCellX;
					clickedCellY = mouseDragCellY;
					drawnPathAddRemove(mouseDragCellX, mouseDragCellY);
					updateView();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			//System.out.println("enter");
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			//System.out.println("exit");
			// TODO Auto-generated method stub

		}
		
		@Override
		public void mouseClicked(MouseEvent click) {
			//System.out.println("click");
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			//System.out.println("move");
			// TODO Auto-generated method stub
			
		}
	}
}