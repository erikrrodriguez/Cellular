package Model;

import java.awt.Color;

public class BirthCell extends NoteCell {

	public BirthCell(int x, int y, String newNote, Color parColor1, Color parColor2) {
		super(x, y, newNote);
		this.color = mixColors(parColor1, parColor2);
	}

	@Override
	public void advance() {
		int newX, newY;
		do {
			newX = randInt(curPos.getX()-1, curPos.getX()+1);
			newY = randInt(curPos.getY()-1, curPos.getY()+1);
		} while (!isNeighbor(newX, newY));
		
		curPos.set(newX, newY);
	}
	
	private boolean isNeighbor(int nx, int ny) {
		if (nx < 0 || nx > 8 || ny < 0 || ny > 8) {
			return false;
		}
		if ((nx < curPos.getX() && ny < curPos.getY()) || (nx > curPos.getX() && ny > curPos.getY())
				|| (nx > curPos.getX() && ny < curPos.getY()) || (nx < curPos.getX() && ny > curPos.getY())) {
			return false;
		}
		if (nx == curPos.getX() && ny == curPos.getY()) {
			return false;
		}
		return true;
	}
	
	private Color mixColors(Color color1, Color color2) {
		double totalAlpha = color1.getAlpha() + color2.getAlpha();
	    double weight0 = color1.getAlpha() / totalAlpha;
	    double weight1 = color2.getAlpha() / totalAlpha;

	    double r = weight0 * color1.getRed() + weight1 * color2.getRed();
	    double g = weight0 * color1.getGreen() + weight1 * color2.getGreen();
	    double b = weight0 * color1.getBlue() + weight1 * color2.getBlue();
	    double a = Math.max(color1.getAlpha(), color2.getAlpha());

	    Color newColor = new Color((int) r, (int) g, (int) b, (int) a);
	    switch(getOctave()){
		case 4: newColor = newColor.darker();
						break;
		case 6: newColor = newColor.brighter();
						break;
		default: break;
		}
	    return newColor;
	  }
	}

