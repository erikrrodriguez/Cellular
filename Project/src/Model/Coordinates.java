package Model;

/*
 * Holds the (x,y) position of each note cell.
 */
public class Coordinates {
	private int x;
	private int y;
	
	public Coordinates(int newX, int newY){
		x = newX;
		y = newY;
	}
	
	/*
	 * Tests if this coordinate is in the same position as another.
	 */
	public Boolean equals(Coordinates testCoor){
		if(x == testCoor.getX() && y == testCoor.getY()){
			return true;
		}
		return false;
	}
	
	public void print() {
		System.out.println("("+x+","+y+")");
	}
	
	/*
	 * Tests if a given coordinate is a neighboring coordinate of this one, vertically or horizontally. 
	 */
	public Boolean isNeighbor(Coordinates testCoor) {
		int nx = testCoor.getX();
		int ny = testCoor.getY();
		if (nx < 0 || nx > 8 || ny < 0 || ny > 8) {
			return false;
		}
		if ((nx < x && ny < y) || (nx > x && ny > y) || (nx > x && ny < y) || (nx < x && ny > y)) {
			return false;
		}
		if (nx == x && ny == y) {
			return false;
		}
		return true;
	}
	
	public void set(int newX, int newY){
		x = newX;
		y = newY;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

}
