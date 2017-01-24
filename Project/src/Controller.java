import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.BirthCell;
import Model.Coordinates;
import Model.Grid;
import Model.GridCell;
import Model.NoteCell;
import View.View;

public class Controller {

	private View mainScreen; //MainFrame
	private Grid grid;
	private boolean running;
	private boolean pause;
	private int clickedCellX; //(x,y) position of the cell the user clicks
	private int clickedCellY;
	private int gridSize;
	private int bpm;
	private ArrayList<Coordinates> drawnPath; //To hold the path that the user draws.
	private JFileChooser presetFileChooser;
	private JFileChooser scoreFileChooser;
	private FileDialog fd;
	private String dir = System.getProperty("user.home") + "/Desktop";

	public Controller(View frame, Grid grid) { //MainFrame
		drawnPath = new ArrayList<Coordinates>();
		this.mainScreen = frame;
		this.grid = grid;

		gridSize = grid.getNumCells();

		mainScreen.getFrame().addListener(new Listener()); //Listener for all buttons
		mainScreen.getFrame().getPanel().addMouse(new Mouse());
		bpm = 250; //240 bpm in milliseconds

		running = true;
		pause = true;
		grid.update();

		updateView(); //Transfers note cells from the grid to the frame

		presetFileChooser = new JFileChooser();
		scoreFileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text Files", "txt");
		presetFileChooser.setFileFilter(filter);
		scoreFileChooser.setFileFilter(filter);

		fd = new FileDialog(mainScreen, "Choose A File:");
		fd.setFile("*.txt");
	}

	/*
	 * Main Loop
	 */
	public void start() {
		while (running) {
			if (!pause) {
				grid.setIpandPort(mainScreen.getIP(), mainScreen.getPort());
				grid.update();
				updateView(); //Update panel with new cell info
			}
			try { //Delay before next grid update
				TimeUnit.MILLISECONDS.sleep(bpm);
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
	public Set<GridCell> getOccupiedCells() {
		return grid.getOccupiedCells();
	}

	public Set<GridCell> getGridCellsWithPaths() {
		return grid.getGridCellsWithPaths();
	}

	/*
	 * Send the note cells, occupied grid cells, and the drawn path array list to the frame
	 */
	private void updateView() {
		mainScreen.getFrame().getPanel().setCells(getOccupiedCells(), getGridCellsWithPaths(), drawnPath);
	}

	public boolean isPause() {
		return pause;
	}

	public void setBPM(int newBPM) {
		//converting from bpm to milliseconds
		if (newBPM > 0 ) {
			bpm = 60000 / newBPM;
		}
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
			case "death": grid.changeDeath(); break;
			case "reset": reset(); break;
			case "OSC": grid.changeOSC(); break;
			case "score": exportScore(); break;
			case "import": importPreset(); break;
			case "export": exportPreset(); break;
			case "showNotes": showNotes(); break;
			case "changeGridSize": changeGridSize(); break;
			case "setBpm": setBPM(mainScreen.getBpm());
			default: break;
			}
			updateView();
		}

		public void reset() {
			grid.resetCells();
		}
		public void startStop() {
			pause = !pause;
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

		public void showNotes() {
			mainScreen.changeShowNotes();
			grid.changeShowNotes();
		}
		
		public void changeGridSize() {
			gridSize = mainScreen.getTxtGridSize();
			if(grid.getFurthestX() > gridSize-1 || grid.getFurthestY() > gridSize-1) {
				clear();
				//grid.reducePaths(gridSize);
			}
			if(grid.getFurthestX() < gridSize-1 || grid.getFurthestY() < gridSize-1) {
				//grid.enlargePaths(gridSize);
			}
			grid.changeGridSize(gridSize);
			mainScreen.changeGridSize(gridSize); //passes to game panel. not the txtfield
			reset(); //to bring out of bounds birth cells back into range, but still preserving cycle
		}

		public void exportScore() {
			fd.setDirectory(dir);
			fd.setMode(FileDialog.SAVE);
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String filename = fd.getFile();
			if(filename != null) {
				if(!filename.endsWith(".txt")) filename = filename + ".txt";
				File file = new File(dir+filename);
				pause = true;
				reset();
				grid.exportScore(file);
			}
		}

		public void importPreset() {
			fd.setDirectory(dir);
			fd.setMode(FileDialog.LOAD);
			fd.setVisible(true);
			dir = fd.getDirectory();
			String filename = fd.getFile();
			if(filename != null) {
				if(!filename.endsWith(".txt")) filename = filename + ".txt";
				File file = new File(dir+filename);
				clear();
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String line, pitch, octave, colorString;
					String[] info;
					int size;
					Color color;
					while ((line = br.readLine()) != null) {
						if(line.startsWith("*")) { //Set IP, Port, bpm, gridSize
							info = line.split("_");
							setBPM(Integer.parseInt(info[3]));
							mainScreen.setBPM(info[3]);
							grid.setIpandPort(info[1], info[2]);
							mainScreen.setIpAndPort(info[1], info[2]);
							mainScreen.setGridSize(info[4]);
							changeGridSize();
						} else if (line.startsWith("+")) { //Set toggles
							info = line.split("_");
							grid.setBirth(Boolean.valueOf(info[1]));
							grid.setDeath(Boolean.valueOf(info[2]));
							grid.setOSC(Boolean.valueOf(info[3]));
							grid.setShowNotes(Boolean.valueOf(info[4]));
							mainScreen.setShowNotes(Boolean.valueOf(info[4]));
						} else {
							info = line.split("_");
							size = info.length;
							pitch = info[0];
							octave = info[1];
							colorString = info[2]; //"r:g:b"
							color = new Color(Integer.parseInt(colorString.split(":")[0]), Integer.parseInt(colorString.split(":")[1]),
									Integer.parseInt(colorString.split(":")[2]));
							if (info[3].equals("N")) { //Note Cell
								NoteCell noteCell = new NoteCell(Integer.parseInt(info[4].split(",")[0]), Integer.parseInt(info[4].split(",")[1]),
										pitch+octave, color, gridSize, true);
								for(int i = 5; i < size; i++) { //build path
									noteCell.addToPath(Integer.parseInt(info[i].split(",")[0]), Integer.parseInt(info[i].split(",")[1]));
								}
								grid.addNoteCell(noteCell);
							} else {
								if (info[4].equals("true")) { // is birth cell placed?
									BirthCell birthCell = new BirthCell(Integer.parseInt(info[5].split(",")[0]),
											Integer.parseInt(info[5].split(",")[1]), pitch+octave, color, true, gridSize);
									grid.addNoteCell(birthCell);
								} else {
									BirthCell birthCell = new BirthCell(Integer.parseInt(info[5].split(",")[0]),
											Integer.parseInt(info[5].split(",")[1]), pitch+octave, color, false, gridSize);
									grid.addNoteCell(birthCell);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void exportPreset() {
			fd.setDirectory(dir);
			fd.setMode(FileDialog.SAVE);
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String filename = fd.getFile();
			if(filename != null) {
				if(!filename.endsWith(".txt")) filename = filename + ".txt";
				File file = new File(dir+filename);
				grid.exportPreset(file, mainScreen.getIP(), mainScreen.getPort(), mainScreen.getBpm(), gridSize);
			}

		}
		public void insert() {
			if (mainScreen.getFrame().getPitch() != "- "	&& 
					mainScreen.getFrame().getOctave() != "-" && 
					mainScreen.getFrame().getPath() == "Birth") {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				BirthCell birthCell = new BirthCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, color, true, gridSize);
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
				NoteCell noteCell = new NoteCell(note+octave, color, drawnPath, gridSize);
				grid.addNoteCell(noteCell);
				drawnPath.clear();
			}
			if (mainScreen.getFrame().getPitch() != "- "	&& 
					mainScreen.getFrame().getOctave() != "-" && 
					mainScreen.getFrame().getPath() == "Random") {
				String note = mainScreen.getFrame().getPitch();
				String octave = mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				System.out.println(drawnPath.get(0).getX());
				System.out.println(drawnPath.get(0).getY());
				NoteCell noteCell = new NoteCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, gridSize);
				noteCell.generateRandomPath();
				grid.addNoteCell(noteCell);
				drawnPath.clear();
			}
			if (mainScreen.getFrame().getPitch() == "- " && //Insertion of Player cells with various paths
					mainScreen.getFrame().getOctave() == "-") {
				String note = mainScreen.getFrame().getPitch();
				String octave = "0"; //mainScreen.getFrame().getOctave();
				Color color = mainScreen.getFrame().getColor();
				if (mainScreen.getFrame().getPath() == "Random") {
					NoteCell noteCell = new NoteCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, gridSize);
					noteCell.generateRandomPath();
					grid.addNoteCell(noteCell);
					drawnPath.clear();
				}
				if (mainScreen.getFrame().getPath() == "Drawn") {
					NoteCell noteCell = new NoteCell(note+octave, color, drawnPath, gridSize);
					grid.addNoteCell(noteCell);
					drawnPath.clear();
				}
				if (mainScreen.getFrame().getPath() == "Birth") {
					BirthCell birthCell = new BirthCell(drawnPath.get(0).getX(), drawnPath.get(0).getY(), note+octave, color, color, true, gridSize);
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
				drawnPath.add(new Coordinates(x, y, gridSize));
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
					drawnPath.add(new Coordinates(x, y, gridSize));
					lastDragCellX = clickedCellX;
					lastDragCellY = clickedCellY;
				}
				else {
					boolean found = false; //uncommenting below restricts drawn paths from overlapping
					//					for (int i = 1; i < drawnPath.size(); i++){
					//						if (drawnPath.get(i).getX() == x && drawnPath.get(i).getY() == y) {
					//							//drawnPath.remove(i); //uncommenting this allows for looped triangle paths and skipped squares
					//							found = true;
					//							break;
					//						}
					//					}
					if (!found && isNeighbor(last.getX(), last.getY(), x, y) && !(last.equals(start) && size > 2)) {
						drawnPath.add(new Coordinates(x,y, gridSize));
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
				grid.clearCell((int)((click.getX()-mainScreen.getHoffset())/mainScreen.getCellSize()), (int)((click.getY()-mainScreen.getVoffset())/mainScreen.getCellSize()));
				updateView();
			}
			else {
				clickedCellX = (int)((click.getX()-mainScreen.getHoffset())/mainScreen.getCellSize()); //Determine which grid cell is clicked
				clickedCellY = (int)((click.getY()-mainScreen.getVoffset())/mainScreen.getCellSize());
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
				int mouseDragCellX = (int)((mouseDrag.getX()-mainScreen.getHoffset())/mainScreen.getCellSize());
				int mouseDragCellY = (int)((mouseDrag.getY()-mainScreen.getVoffset())/mainScreen.getCellSize());
				if ((mouseDragCellX != lastDragCellX || mouseDragCellY != lastDragCellY) && 
						mouseDrag.getX() < mainScreen.getGamePanelSize() 
						&& mouseDrag.getY() < mainScreen.getGamePanelSize()) {
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