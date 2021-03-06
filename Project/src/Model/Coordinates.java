package Model;

/*
 * Holds the (x,y) position of each note cell.
 */
public class Coordinates {
	private int x;
	private int y;
	@SuppressWarnings("unused")
	private int gridSize;

	public Coordinates(int newX, int newY, int newGridSize) {
		x = newX;
		y = newY;
		gridSize = newGridSize;
	}

	/*
	 * Tests if this coordinate is in the same position as another.
	 */
	public Boolean equals(Coordinates testCoor) {
		if (x == testCoor.getX() && y == testCoor.getY()) {
			return true;
		}
		return false;
	}

	/*
	 * Tests if a given coordinate is a neighboring coordinate of this one,
	 * vertically or horizontally.
	 */
	public Boolean isNeighbor(int nx, int ny, int gridSize) {
		int d1 = Math.abs(nx - x);
		int d2 = Math.abs(ny - y);
		return nx >= 0 && ny >= 0 && nx <= gridSize - 1 && ny <= gridSize - 1
				&& d1 + d2 == 1;
	}

	public void set(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
