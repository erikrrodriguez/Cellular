package Model;

/*
 * Holds the (currentX,currentY) position of each note cell.
 */
public class Coordinates {
	private int initialX;
	private int initialY;
	private int currentX;
	private int currentY;
	private boolean skip;
	
	public Coordinates(int newX, int newY, int newGridSize){
		initialX = newX;
		initialY = newY;
		currentX = newX;
		currentY = newY;
		skip = false;
	}
	
	/*
	 * Tests if this coordinate is in the same position as another.
	 */
	public Boolean equals(Coordinates testCoor){
		if (currentX == testCoor.getX() && currentY == testCoor.getY()){
			return true;
		}
		return false;
	}
	
	/*
	 * Tests if a given coordinate is a neighboring coordinate of this one, vertically or horizontally. 
	 */
	public Boolean isNeighbor(int nx, int ny, int gridSize) {
		int d1 = Math.abs(nx - currentX);
		int d2 = Math.abs(ny - currentY);
		return nx >= 0 && ny >= 0 && nx <= gridSize - 1 && ny <= gridSize - 1 && d1 + d2 == 1;
	}
	
	public void set(int newX, int newY){
		currentX = newX;
		currentY = newY;
	}
	
	public int getX(){
		return currentX;
	}
	
	public int getY(){
		return currentY;
	}
	
	public boolean getSkip() {
		return skip;
	}
	
	public void reducePath(int gridSize) {
		if (currentX > gridSize - 1) currentX--;
		if (currentY > gridSize - 1) currentY--;
		skip = true;
	}
	
	public void enlargePath(int gridSize) {
		if (currentX < initialX) currentX++;
		if (currentY < initialY) currentY++;
		if (currentX == initialX) skip = false;
	}
}
