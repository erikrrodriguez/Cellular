package Model;
import kuusisto.tinysound.TinySound;

public class Audio {
	
	public kuusisto.tinysound.Sound A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4;
	public kuusisto.tinysound.Sound A5, AS5, B5, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5;
	public kuusisto.tinysound.Sound A6, AS6, B6, C6, CS6, D6, DS6, E6, F6, FS6, G6, GS6;
	public kuusisto.tinysound.Sound[] sounds;
	
	public Audio() {
		TinySound.init();
		A4 = TinySound.loadSound("A04.wav");
		AS4 = TinySound.loadSound("A04.wav");
		B4 = TinySound.loadSound("B04.wav");
		C4 = TinySound.loadSound("C04.wav");
		CS4 = TinySound.loadSound("A04.wav");
		D4 = TinySound.loadSound("D04.wav");
		DS4 = TinySound.loadSound("A04.wav");
		E4 = TinySound.loadSound("E04.wav");
		F4 = TinySound.loadSound("F04.wav");
		FS4 = TinySound.loadSound("A04.wav");
		G4 = TinySound.loadSound("G04.wav");
		GS4 = TinySound.loadSound("A04.wav");
		
		sounds = new kuusisto.tinysound.Sound[] {A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4,
												A5, AS5, B5, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5,
												A6, AS6, B6, C6, CS6, D6, DS6, E6, F6, FS6, G6, GS6};
	}
	
	public void playSound(int index, double volume){
		sounds[index].play(volume);
	}
	
	public void shutdown() {
		TinySound.shutdown();
	}

}
