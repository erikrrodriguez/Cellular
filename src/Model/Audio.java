package Model;
import kuusisto.tinysound.TinySound;

public class Audio {
	
	public kuusisto.tinysound.Sound A;
	public kuusisto.tinysound.Sound B;
	public kuusisto.tinysound.Sound C;
	public kuusisto.tinysound.Sound D;
	public kuusisto.tinysound.Sound E;
	public kuusisto.tinysound.Sound F;
	public kuusisto.tinysound.Sound G;
	
	public Audio() {
		TinySound.init();
		A = TinySound.loadSound("A04.wav");
		B = TinySound.loadSound("B04.wav");
		C = TinySound.loadSound("C04.wav");
		D = TinySound.loadSound("D04.wav");
		E = TinySound.loadSound("E04.wav");
		F = TinySound.loadSound("F04.wav");
		G = TinySound.loadSound("G04.wav");
	}
	
	public void shutdown() {
		TinySound.shutdown();
	}

}
