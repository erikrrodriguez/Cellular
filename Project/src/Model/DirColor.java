package Model;
import java.awt.Color;

public class DirColor {
	
	private String direction;
	private Color color;
	
	public DirColor(String direc, Color col) {
		direction = direc;
		color = col;
	}
	
	public String getDir() {
		return direction;
	}
	
	public Color getColor() {
		return color;
	}
	
	
	public boolean equals(DirColor comp){
		if (comp.getDir().equals(direction) && comp.getColor().equals(color)) return true;
		return false;
	}

}
