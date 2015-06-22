package Model;
import kuusisto.tinysound.TinySound;

public class Audio {
	
	public kuusisto.tinysound.Sound A4, AS4, B4, C4, CS4, D4, DS4, E4, F4, FS4, G4, GS4;
	public kuusisto.tinysound.Sound A5, AS5, B5, C5, CS5, D5, DS5, E5, F5, FS5, G5, GS5;
	public kuusisto.tinysound.Sound A6, AS6, B6, C6, CS6, D6, DS6, E6, F6, FS6, G6, GS6;
	public kuusisto.tinysound.Sound[] sounds;
	
	public Audio() {
		TinySound.init();
		A4 = TinySound.loadSound("A4.wav");
		AS4 = TinySound.loadSound("AS4.wav");
		B4 = TinySound.loadSound("B4.wav");
		C4 = TinySound.loadSound("C4.wav");
		CS4 = TinySound.loadSound("CS4.wav");
		D4 = TinySound.loadSound("D4.wav");
		DS4 = TinySound.loadSound("DS4.wav");
		E4 = TinySound.loadSound("E4.wav");
		F4 = TinySound.loadSound("F4.wav");
		FS4 = TinySound.loadSound("FS4.wav");
		G4 = TinySound.loadSound("G4.wav");
		GS4 = TinySound.loadSound("GS4.wav");
		
		A5 = TinySound.loadSound("A5.wav");
		AS5 = TinySound.loadSound("AS5.wav");
		B5 = TinySound.loadSound("B5.wav");
		C5 = TinySound.loadSound("C5.wav");
		CS5 = TinySound.loadSound("CS5.wav");
		D5 = TinySound.loadSound("D5.wav");
		DS5 = TinySound.loadSound("DS5.wav");
		E5 = TinySound.loadSound("E5.wav");
		F5 = TinySound.loadSound("F5.wav");
		FS5 = TinySound.loadSound("FS5.wav");
		G5 = TinySound.loadSound("G5.wav");
		GS5 = TinySound.loadSound("GS5.wav");
		
		A6 = TinySound.loadSound("A6.wav");
		AS6 = TinySound.loadSound("AS6.wav");
		B6 = TinySound.loadSound("B6.wav");
		C6 = TinySound.loadSound("C6.wav");
		CS6 = TinySound.loadSound("CS6.wav");
		D6 = TinySound.loadSound("D6.wav");
		DS6 = TinySound.loadSound("DS6.wav");
		E6 = TinySound.loadSound("E6.wav");
		F6 = TinySound.loadSound("F6.wav");
		FS6 = TinySound.loadSound("FS6.wav");
		G6 = TinySound.loadSound("G6.wav");
		GS6 = TinySound.loadSound("GS6.wav");
		
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
