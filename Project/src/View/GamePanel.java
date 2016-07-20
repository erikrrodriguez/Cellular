package View;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import Model.BirthCell;
import Model.Coordinates;
import Model.GridCell;
import Model.NoteCell;

/*
 * This is the representation of the grid within the frame.
 */
public class GamePanel extends JPanel{
	
	private ArrayList<NoteCell> noteCells;
	private ArrayList<GridCell> occupiedCells;
	private ArrayList<Coordinates> drawnPath;
	private int panelSize;
	private int halfCellSize;
	private int cellSize;
	private int numCells;
	private int minSize;
	private int hoffset;
	private int voffset;
	private int fontSize;
	public String note;
	public int spacing;

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
		
		g2.setColor(Color.BLACK);
		g2.fillRect(hoffset, voffset, panelSize, panelSize);

		//Draw Paths
		for(NoteCell noteCell : noteCells) {
			g2.setColor(noteCell.getColor());
			ArrayList<Coordinates> path = noteCell.getPath();
			for(int i = 0; i < path.size()-1; i++) {
				g2.drawLine(cellSize*path.get(i).getX()+halfCellSize+hoffset, cellSize*path.get(i).getY()+halfCellSize+voffset, cellSize*path.get(i+1).getX()+halfCellSize+hoffset, cellSize*path.get(i+1).getY()+halfCellSize+voffset);
			}
			if (noteCell.isLoop()) {
				g2.drawLine(cellSize*path.get(0).getX()+halfCellSize+hoffset, cellSize*path.get(0).getY()+halfCellSize+voffset, cellSize*path.get(path.size()-1).getX()+halfCellSize+hoffset, cellSize*path.get(path.size()-1).getY()+halfCellSize+voffset);
			}
		}

		//Draw Cells
		for (GridCell gridCell : occupiedCells) {
			if (gridCell.getNumNoteCells() > 1) {
				g2.setColor(Color.WHITE);
				g2.fillRect(cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+voffset, cellSize, cellSize);
				
				g2.setColor(Color.black);
				g2.setFont(new Font("default", Font.BOLD, fontSize));
				String exclaim = "!!";
				for(int i = 2; i < gridCell.getNumNoteCells(); i++) {
					exclaim += "!";
				}
				//g2.drawString(exclaim, cellSize*gridCell.getX()+16+hoffset, cellSize*gridCell.getY()+32+voffset);
			}
			else { //If only one note cell in the grid cell
				g2.setColor(gridCell.getNoteCell().getColor());
				g2.fillRect(cellSize*gridCell.getX()+hoffset, cellSize*gridCell.getY()+voffset, cellSize, cellSize);
				
				if (gridCell.getNoteCell() instanceof BirthCell) {
					g2.setColor(Color.white);
				}
				else {
					g2.setColor(Color.black);
				}
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
				//g2.drawString(note, cellSize*gridCell.getX()+spacing+hoffset, cellSize*gridCell.getY()+32+voffset);
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
		for (int i = 0; i < panelSize; i += cellSize) {
			g2.setColor(Color.WHITE);
			g2.drawLine(i+hoffset, voffset, i+hoffset, cellSize*numCells+voffset);
			g2.drawLine(hoffset, i+voffset, cellSize*numCells+hoffset, i+voffset);
		}

	}
	
	public void setCells(ArrayList<NoteCell> noteCells, ArrayList<GridCell> occupiedCells, ArrayList<Coordinates> drawnPath) {
		this.noteCells = noteCells;
		this.occupiedCells = occupiedCells;
		this.drawnPath = drawnPath;
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

}
