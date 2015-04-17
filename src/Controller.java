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
	private int clickedCellX;
	private int clickedCellY;
	private ArrayList<Coordinates> drawnPath;

	public Controller(Frame frame, Grid grid) {
		drawnPath = new ArrayList<Coordinates>();
		this.frame = frame;
		this.grid = grid;
		this.frame.addListener(new Listener());
		this.frame.getPanel().addMouse(new MyMouse());
		running = true;
		pause = true;
		frame.changeVisible(true);
		grid.update();
		sendCells();
	}

	public void start() {
		while (running) {
			if (!pause) {
				grid.update();
				sendCells();
			}
			frame.getPanel().repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(250);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public ArrayList<NoteCell> getCells() {
		return grid.getCells();
	}

	public ArrayList<GridCell> getOccupiedCells() {
		return grid.getOccupiedCells();
	}

	private void sendCells() {
		frame.getPanel().setCells(getCells(), getOccupiedCells(), drawnPath);
	}

	public boolean isPause() {
		return pause;
	}

	public void removeCell(int x, int y) {
		for (Coordinates coor : drawnPath){
			if (coor.getX() == x && coor.getY() == y) {
				drawnPath.remove(coor);				
			}
		}
	}
	
	public void printPath() {
		String path = "";
		for(Coordinates coor : drawnPath) {
			path += "(" + coor.getX()+","+coor.getY()+") ";
		}
		System.out.println(path);
	}

	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent button) {
			String b = button.getActionCommand();
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
			}
			else if (b == "generate" && drawnPath.size() == 0) {
				grid.clearGrid();
				grid.generate();
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
//					int octave = Integer.parseInt(frame.getOctave());
					Color color = frame.getColor();
					NoteCell noteCell = new NoteCell(note, color, drawnPath);
					grid.addNoteCell(noteCell);
					drawnPath.clear();
					pause = false;
					frame.changeVisible(pause);
				}
			}
		}
	}

	private class MyMouse implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent click) {
			clickedCellX = (int)(click.getX()/50);
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