package Model;

import java.awt.Color;

public class BirthCell extends NoteCell {

	private boolean placed;
	private boolean reset;
	private int xStart;
	private int yStart;
	private String note;

	public BirthCell(int x, int y, String newNote, Color parColor1,
			Color parColor2, boolean placed, int newGridSize) {
		super(x, y, newNote, newGridSize);
		note = newNote;
		this.xStart = x;
		this.yStart = y;
		reset = false;
		this.placed = placed;
		this.color = mixColors(parColor1, parColor2);
	}

	public BirthCell(int x, int y, String newNote, Color color, boolean placed,
			int newGridSize) {
		super(x, y, newNote, newGridSize);
		note = newNote;
		this.xStart = x;
		this.yStart = y;
		reset = false;
		this.placed = placed;
		this.color = color;
	}

	public BirthCell(int x, int y, String newNote, boolean placed,
			int newGridSize) {
		super(x, y, newNote, newGridSize);
		note = newNote;
		this.xStart = x;
		this.yStart = y;
		reset = false;
		this.placed = placed;
		setRandomColor();
	}

	@Override
	public void advance() {
		int newX, newY;
		if (reset) {
			newX = xStart;
			newY = yStart;
			reset = false;
		} else {
			do {
				newX = randInt(curPos.getX() - 1, curPos.getX() + 1);
				newY = randInt(curPos.getY() - 1, curPos.getY() + 1);
			} while (!isNeighbor(newX, newY));
		}
		curPos.set(newX, newY);
	}

	public int getXStart() {
		return xStart;
	}

	public int getYStart() {
		return yStart;
	}

	@Override
	public void setPos(int x) {
		reset = true;
	}

	public boolean isPlaced() {
		return placed;
	}

	@Override
	public String getScoreNote() {
		return "\\parenthesize_" + note;
	}

	private boolean isNeighbor(int nx, int ny) {
		if (nx < 0 || nx > gridSize - 1 || ny < 0 || ny > gridSize - 1) {
			return false;
		}
		if ((nx < curPos.getX() && ny < curPos.getY())
				|| (nx > curPos.getX() && ny > curPos.getY())
				|| (nx > curPos.getX() && ny < curPos.getY())
				|| (nx < curPos.getX() && ny > curPos.getY())) {
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
		switch (getOctave()) {
		case 4:
			newColor = newColor.darker();
			break;
		case 6:
			newColor = newColor.brighter();
			break;
		default:
			break;
		}
		return newColor;
	}
}
