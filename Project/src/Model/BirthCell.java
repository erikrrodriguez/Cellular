package Model;

public class BirthCell extends NoteCell {

	public BirthCell(int x, int y, String newNote) {
		super(x, y, newNote);
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
	
	public boolean isNeighbor(int nx, int ny) {
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

}
