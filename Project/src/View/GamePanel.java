package View;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JPanel;

import Model.BirthCell;
import Model.Coordinates;
import Model.GridCell;
import Model.NoteCell;

/*
 * This is the representation of the grid within the frame.
 */
public class GamePanel extends JPanel{
	private Set<GridCell> occupiedCells;
	private Set<GridCell> gridCellsWithPaths;
	private ArrayList<Coordinates> drawnPath;
	private int panelSize;
	private int halfCellSize;
	private int cellSize;
	private int numCells;
	private int minSize;
	private int hoffset;
	private int voffset;
	private int fontSize;
	private boolean showNotes = false;
	public String note;
	public int spacing;
	private float percentage = (float) 0.50;

	public GamePanel(int newPanelSize, int newNumCells){
		panelSize = newPanelSize;
		numCells = newNumCells;
		cellSize = panelSize / numCells;
		halfCellSize = cellSize/2;
		
		note = "";
		spacing = 0;
		hoffset = 0;
		voffset = 0;
		fontSize = 20;
	}
	
	public void addMouse(MouseListener m) {
		this.addMouseListener(m);
		this.addMouseMotionListener((MouseMotionListener) m);
	}
	public Dimension getPreferredSize() {
        return new Dimension(panelSize,panelSize);
    }
	
	/*
	 * Redraw the grid.
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setColor(Color.BLACK);
		g2.fillRect(hoffset, voffset, panelSize, panelSize);

		//Draw Paths OLD
//		for(NoteCell noteCell : noteCells) {
//			g2.setColor(noteCell.getColor());
//			ArrayList<Coordinates> path = noteCell.getPath();
//			for(int i = 0; i < path.size()-1; i++) {
//				g2.drawLine(cellSize*path.get(i).getX()+halfCellSize+hoffset, cellSize*path.get(i).getY()+halfCellSize+voffset, cellSize*path.get(i+1).getX()+halfCellSize+hoffset, cellSize*path.get(i+1).getY()+halfCellSize+voffset);
//			}
//			if (noteCell.isLoop()) {
//				g2.drawLine(cellSize*path.get(0).getX()+halfCellSize+hoffset, cellSize*path.get(0).getY()+halfCellSize+voffset, cellSize*path.get(path.size()-1).getX()+halfCellSize+hoffset, cellSize*path.get(path.size()-1).getY()+halfCellSize+voffset);
//			}
//		}
		//NEXT GEN Draw Paths
		String[] node = new String[4];
		for(GridCell gridCell : gridCellsWithPaths) {
			int size = gridCell.getContainedPaths().size();
			//System.out.println(gridCellsWithPaths.size());
			int upCount = gridCell.getUpCount();
			//System.out.println("upcount: " +upCount);
			int downCount = gridCell.getDownCount();
			int leftCount = gridCell.getLeftCount();
			int rightCount = gridCell.getRightCount();
			float upSpace = 0; float downSpace = 0; float leftSpace = 0; float rightSpace = 0;
			if(upCount > 0) upSpace = cellSize*percentage/upCount;
			if(downCount > 0) downSpace = cellSize*percentage/downCount;
			if(leftCount > 0) leftSpace = cellSize*percentage/leftCount;
			if(rightCount > 0) rightSpace = cellSize*percentage/rightCount;
			//System.out.println("upspace: " +upSpace);
			int upTally = 0; int downTally = 0; int leftTally = 0; int rightTally = 0;
			boolean upEven = upCount % 2 == 0;
			//System.out.println(upEven);
			boolean downEven = downCount % 2 == 0;
			boolean leftEven = leftCount % 2 == 0;
			boolean rightEven = rightCount % 2 == 0; 
			for(int i = 0; i < size; i++) {
				node = gridCell.getContainedPath(i).split(":"); //[dir,r,g,b]
				g2.setColor(new Color(Integer.parseInt(node[1]),Integer.parseInt(node[2]),Integer.parseInt(node[3])));
				if (node[0].equals("up")) {
					if(upTally == 0 && !upEven) {
						//draw middle line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+halfCellSize+Math.round(upTally*upSpace)+hoffset, cellSize*gridCell.getY()+voffset);
						upTally = 1;
					} else { //odd and 1+, even 0+
						if (upTally == 0) upTally = 1; //odd1+ even1+
						//draw line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+halfCellSize+Math.round(upTally*upSpace)+hoffset, cellSize*gridCell.getY()+voffset);
						if (upTally < 0) upTally--;
						upTally = upTally * -1;
					}
				} else if (node[0].equals("down")) {
					if(downTally == 0 && !downEven) {
						//draw middle line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+halfCellSize+Math.round(downTally*downSpace)+hoffset, cellSize*gridCell.getY()+cellSize+voffset);
						downTally = 1;
					} else { //odd and 1+, even 0+
						if (downTally == 0) downTally = 1; //odd1+ even1+
						//draw line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+halfCellSize+Math.round(downTally*downSpace)+hoffset, cellSize*gridCell.getY()+cellSize+voffset);
						if (downTally < 0) downTally--;
						downTally = downTally * -1;
					}
				} else if (node[0].equals("left")) {
					if(leftTally == 0 && !leftEven) {
						//draw middle line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+halfCellSize+Math.round(leftTally*leftSpace)+voffset);
						leftTally = 1;
					} else { //odd and 1+, even 0+
						if (leftTally == 0) leftTally = 1; //odd1+ even1+
						//draw line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+halfCellSize+Math.round(leftTally*leftSpace)+voffset);
						if (leftTally < 0) leftTally--;
						leftTally = leftTally * -1;
					}
				} else if (node[0].equals("right")) {
					if(rightTally == 0 && !rightEven) {
						//draw middle line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+cellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+Math.round(rightTally*rightSpace)+voffset);
						rightTally = 1;
					} else { //odd and 1+, even 0+
						if (rightTally == 0) rightTally = 1; //odd1+ even1+
						//draw line
						g2.drawLine(cellSize*gridCell.getX()+halfCellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+voffset, cellSize*gridCell.getX()+cellSize+hoffset, cellSize*gridCell.getY()+halfCellSize+Math.round(rightTally*rightSpace)+voffset);
						if (rightTally < 0) rightTally--;
						rightTally = rightTally * -1;
					}
				}
			}
		}

		//Draw Cells
		for (GridCell gridCell : occupiedCells) {
			if (gridCell.getNumNoteCells() > 1) {
				g2.setColor(Color.WHITE);
				g2.fillRect(cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+voffset, cellSize, cellSize);
				
				if (showNotes) { //Draw exclamation points on cells
					g2.setColor(Color.black);
					g2.setFont(new Font("default", Font.BOLD, fontSize));
					String exclaim = "!!";
					for(int i = 2; i < gridCell.getNumNoteCells(); i++) {
						exclaim += "!";
					}
					g2.drawString(exclaim, cellSize*gridCell.getX()+16+hoffset, cellSize*gridCell.getY()+32+voffset);
				}
			}
			else { //If only one note cell in the grid cell
				g2.setColor(gridCell.getNoteCell().getColor());				
				if (gridCell.getNoteCell() instanceof BirthCell) {
					g2.fillOval(cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+voffset, cellSize, cellSize);
					g2.setColor(Color.white);
				}
				else {
					g2.fillRect(cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+voffset, cellSize, cellSize);
					g2.setColor(Color.black);
				}
				if (showNotes) { //Draw Notes on cells
					g2.setFont(new Font("default", Font.BOLD, fontSize-2));
					if (gridCell.getNoteCell().getPitch().equals("- ")) {
						note = "";
					}
					else {
						note = gridCell.getNoteCell().getNote().replaceAll(" ", "");
					}
					spacing = 14;
					switch(note.length()){
						case 4: spacing -= 5;
						case 3: spacing -= 4;
						default: break;
					}
					g2.drawString(note, cellSize*gridCell.getX()+spacing+hoffset, cellSize*gridCell.getY()+32+voffset);
				}
			}
		}
		
		//Draw Drawn Path
		for(int i = 0; i < drawnPath.size(); i++) {
			if (i == drawnPath.size() - 1 && drawnPath.get(0).equals(drawnPath.get(drawnPath.size()-1))) {
				g2.setColor(Color.red);
			}
			else {
				g2.setColor(Color.WHITE);
			}
			g2.fillRect(cellSize*drawnPath.get(i).getX()+hoffset, cellSize*drawnPath.get(i).getY()+voffset, cellSize, cellSize);
			
		}
		
		//Draw Grid lines
		for (int i = 0; i <= panelSize; i += cellSize) {
			g2.setColor(Color.WHITE);
			g2.drawLine(i+hoffset, voffset, i+hoffset, cellSize*numCells+voffset);
			g2.drawLine(hoffset, i+voffset, cellSize*numCells+hoffset, i+voffset);
		}
	}
	
	public void setCells(Set<GridCell> occupiedCells, Set<GridCell> gridCellsWithPaths, ArrayList<Coordinates> drawnPath) {
		this.occupiedCells = occupiedCells;
		this.drawnPath = drawnPath;
		this.gridCellsWithPaths = gridCellsWithPaths;
		repaint();
	}

	public int getPanelSize() {
		return panelSize;
	}

	public void resize() {
		minSize = (int) Math.min(getSize().getHeight(), getSize().getWidth());
		cellSize = minSize / numCells;
		halfCellSize = cellSize/2;
		panelSize = minSize;
		hoffset = (int)(getSize().getWidth() - panelSize) / 2;
		voffset = (int)(getSize().getHeight() - panelSize) / 2;
		fontSize = (int)(0.095*panelSize-22.568);
//		System.out.println(panelSize);
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	public int getHoffset() {
		return hoffset;
	}
	
	public int getVoffset() {
		return voffset;
	}
	
	public void changeShowNotes() {
		showNotes = !showNotes;
	}

}
