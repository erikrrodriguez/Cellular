package View;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Frame for the entire program including the grid and the buttons
 */
public class Frame extends JPanel{
	private Panel panel; //holds the actual grid
	private int panelSize;
	private int cellWidth;
	private int numCells;
	private String[] notes = {"- ", "C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "A#", "B "};
	private String[] octaves = {"-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	private String[] colors = {"-", "Red", "Blue", "Green", "Yellow", "Orange", "Magenta", "Random"};
	private String[] pathOptions = {"-", "Drawn", "Random", "Birth"};
	
	private GridBagConstraints gbc;
	
	private JButton startStop = new JButton("Start/Stop");
	private JButton clear = new JButton("Clear");
	private JButton generate = new JButton("Generate");
	private JButton insert = new JButton("Insert");
	private JButton delete = new JButton("Delete");
	private JButton reset = new JButton("Reset");
	
	private JCheckBox birth = new JCheckBox("Birth", false);
	private JCheckBox OSC = new JCheckBox("OSC", false);
	
	private JLabel note = new JLabel("Note:");
	private JLabel octave = new JLabel("Octave:");
	private JLabel color = new JLabel("Color:");
	private JLabel path = new JLabel("Path:");
	
	private JComboBox<String> pathSelect = new JComboBox<String>(pathOptions);
	private JComboBox<String> noteSelect = new JComboBox<String>(notes);
	private JComboBox<String> octaveSelect = new JComboBox<String>(octaves);
	private JComboBox<String> colorSelect = new JComboBox<String>(colors);
	
	public Frame(int newNumCells) {
		cellWidth = 50;
		numCells = newNumCells;
		panelSize = cellWidth*numCells;
		controlSetup(); //Set action commands for buttons
		
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		panel = new Panel(panelSize, numCells);
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(cellWidth,cellWidth));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 9;
		gbc.gridheight = 9;
		add(panel, gbc);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 9;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		add(filler, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(startStop, gbc);
		
		gbc.gridx = 10;
		gbc.gridy = 2;
		add(clear, gbc);
		
		gbc.gridx = 11;
		gbc.gridy = 2;
		add(reset, gbc);
		
		gbc.gridx = 12;
		gbc.gridy = 2;
		add(generate, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.CENTER;
		add(birth, gbc);
		
		gbc.gridx = 10;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.CENTER;
		add(OSC, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		add(filler, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(note, gbc);
		
		gbc.gridx = 10;
		gbc.gridy = 6;
		add(octave, gbc);
		
		gbc.gridx = 11;
		gbc.gridy = 6;
		add(color, gbc);
		
		gbc.gridx = 12;
		gbc.gridy = 6;
		add(path, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 7;
		add(noteSelect, gbc);
		
		gbc.gridx = 10;
		gbc.gridy = 7;
		add(octaveSelect, gbc);
		
		gbc.gridx = 11;
		gbc.gridy = 7;
		add(colorSelect, gbc);
		
		gbc.gridx = 12;
		gbc.gridy = 7;
		add(pathSelect, gbc);
		
		gbc.gridx = 9;
		gbc.gridy = 8;
		add(insert, gbc);
		
		gbc.gridx = 10;
		gbc.gridy = 8;
		add(delete, gbc);
	}
	
	public void controlSetup() {
		insert.setActionCommand("insert");
		delete.setActionCommand("delete");
		startStop.setActionCommand("startStop");
		clear.setActionCommand("clear");
		generate.setActionCommand("generate");
		birth.setActionCommand("birth");
		OSC.setActionCommand("OSC");
		reset.setActionCommand("reset");
	}
	
	public Panel getPanel() {
		return panel;
	}
	
	public int getPanelSize() {
		return panelSize;
	}
	
	public String getPitch(){
		return (String) noteSelect.getSelectedItem();
	}
	
	public void setPitch(String pitch) {
		noteSelect.setSelectedItem(pitch);
	}
	
	public String getOctave() {
		return (String) octaveSelect.getSelectedItem();
	}
	
	public void setOctave(String octave) {
		noteSelect.setSelectedItem(octave);
	}
	
	public String getPath() {
		return (String) pathSelect.getSelectedItem();
	}
	
	public Color getColor() {
		switch ((String) colorSelect.getSelectedItem()) {
			case "Red": return Color.RED;
			case "Blue": return Color.BLUE;
			case "Green": return Color.GREEN;
			case "Yellow": return Color.YELLOW;
			case "Orange": return Color.ORANGE;
			case "Magenta": return Color.MAGENTA;
			case "Random": return Color.PINK;
			default: return null;
		}
	}
	
	public void setColor(String color) {
		colorSelect.setSelectedItem(color);
	}
	
	public void addListener(ActionListener listener) {
		startStop.addActionListener(listener);
		clear.addActionListener(listener);
		generate.addActionListener(listener);
		insert.addActionListener(listener);
		delete.addActionListener(listener);
		birth.addActionListener(listener);
		OSC.addActionListener(listener);
		reset.addActionListener(listener);
	}
}
