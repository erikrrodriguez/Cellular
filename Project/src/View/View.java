package View;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import View.GamePanel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ThreadLocalRandom;

public class View extends JFrame {

	private JTextField txtIpAddress = new JTextField();
	private JTextField txtPort = new JTextField();
	private JTextField bpm = new JTextField();
	
	private GamePanel gamePanel; //holds the actual grid
	private int cellSize;
	private int numCells;
	private String[] notes = {"- ", "C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "A#", "B ","Random"};
	private String[] octaves = {"-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Random"};
	private String[] colors = {"-", "Red", "Blue", "Green", "Yellow", "Orange", "Magenta", "Random"};
	private String[] pathOptions = {"-", "Drawn", "Birth", "Random"};
	
	private JButton startStop = new JButton("Start/Stop");
	private JButton clear = new JButton("Clear");
	private JButton generate = new JButton("Generate");
	private JButton insert = new JButton("Insert");
	private JButton delete = new JButton("Delete");
	private JButton reset = new JButton("Reset");
	private JButton score = new JButton("Score");
	private JButton importPreset = new JButton("Import");
	private JButton exportPreset = new JButton("Export");
	
	private JCheckBox birth = new JCheckBox("Birth", false);
	private JCheckBox OSC = new JCheckBox("OSC", false);
	private JCheckBox showNotes = new JCheckBox("Show Notes", false);
	private JCheckBox death = new JCheckBox("Death");
	
	private JLabel note = new JLabel("Note:");
	private JLabel octave = new JLabel("Octave:");
	private JLabel color = new JLabel("Color:");
	private JLabel path = new JLabel("Path:");
	private JLabel lblIpAddress = new JLabel("IP Address:");
	private JLabel lblPort = new JLabel("Port:");
	private JLabel lblBpm = new JLabel("BPM:");
	
	private JComboBox<String> pathSelect = new JComboBox<String>(pathOptions);
	private JComboBox<String> noteSelect = new JComboBox<String>(notes);
	private JComboBox<String> octaveSelect = new JComboBox<String>(octaves);
	private JComboBox<String> colorSelect = new JComboBox<String>(colors);

	/**
	 * Create the application.
	 */
	public View(int newNumCells) {
		cellSize = 50;
		numCells = newNumCells;
		gamePanel = new GamePanel(cellSize*numCells, numCells);
		
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				gamePanel.resize();
	        }
		});
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frame = new JFrame();
		this.setBackground(Color.LIGHT_GRAY); //frame.getContentPane()
		this.setForeground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{220, 0, 90, 90, 90, 90, 0, 0};
		gridBagLayout.rowHeights = new int[]{50, 30, 30, 35, 50, 30, 30, 30, 15, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.BLACK);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridheight = 11;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(gamePanel, gbc_panel);
		
//		JButton startStop = new JButton("Start/Stop");
//		startStop.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
		GridBagConstraints gbc_startStop = new GridBagConstraints();
		gbc_startStop.fill = GridBagConstraints.HORIZONTAL;
		gbc_startStop.insets = new Insets(0, 0, 5, 5);
		gbc_startStop.gridx = 2;
		gbc_startStop.gridy = 0;
		startStop.setFocusable(false);
		getContentPane().add(startStop, gbc_startStop);
		
		//JButton clear = new JButton("Clear");
		GridBagConstraints gbc_clear = new GridBagConstraints();
		gbc_clear.fill = GridBagConstraints.HORIZONTAL;
		gbc_clear.insets = new Insets(0, 0, 5, 5);
		gbc_clear.gridx = 3;
		gbc_clear.gridy = 0;
		clear.setFocusable(false);
		getContentPane().add(clear, gbc_clear);
		
		//JButton reset = new JButton("Reset");
		GridBagConstraints gbc_reset = new GridBagConstraints();
		gbc_reset.fill = GridBagConstraints.HORIZONTAL;
		gbc_reset.insets = new Insets(0, 0, 5, 5);
		gbc_reset.gridx = 4;
		gbc_reset.gridy = 0;
		reset.setFocusable(false);
		getContentPane().add(reset, gbc_reset);
		
		//JButton generate = new JButton("Generate");
		GridBagConstraints gbc_generate = new GridBagConstraints();
		gbc_generate.fill = GridBagConstraints.HORIZONTAL;
		gbc_generate.insets = new Insets(0, 0, 5, 5);
		gbc_generate.gridx = 5;
		gbc_generate.gridy = 0;
		generate.setFocusable(false);
		getContentPane().add(generate, gbc_generate);
		
		//JCheckBox chckbxBirth = new JCheckBox("Birth");
		GridBagConstraints gbc_chckbxBirth = new GridBagConstraints();
		gbc_chckbxBirth.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBirth.gridx = 2;
		gbc_chckbxBirth.gridy = 1;
		birth.setFocusable(false);
		getContentPane().add(birth, gbc_chckbxBirth);
		
		//JComboBox pathSelect = new JComboBox();
		GridBagConstraints gbc_pathSelect = new GridBagConstraints();
		gbc_pathSelect.anchor = GridBagConstraints.NORTH;
		gbc_pathSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_pathSelect.insets = new Insets(0, 0, 5, 5);
		gbc_pathSelect.gridx = 5;
		gbc_pathSelect.gridy = 3;
		pathSelect.setMaximumRowCount(pathOptions.length);
		pathSelect.setFocusable(false);
		getContentPane().add(pathSelect, gbc_pathSelect);
		
		//JComboBox noteSelect = new JComboBox();
		GridBagConstraints gbc_noteSelect = new GridBagConstraints();
		gbc_noteSelect.anchor = GridBagConstraints.NORTH;
		gbc_noteSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_noteSelect.insets = new Insets(0, 0, 5, 5);
		gbc_noteSelect.gridx = 2;
		gbc_noteSelect.gridy = 3;
		noteSelect.setMaximumRowCount(notes.length);
		noteSelect.setFocusable(false);
		getContentPane().add(noteSelect, gbc_noteSelect);
		
		//JComboBox octaveSelect = new JComboBox();
		GridBagConstraints gbc_octaveSelect = new GridBagConstraints();
		gbc_octaveSelect.anchor = GridBagConstraints.NORTH;
		gbc_octaveSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_octaveSelect.insets = new Insets(0, 0, 5, 5);
		gbc_octaveSelect.gridx = 3;
		gbc_octaveSelect.gridy = 3;
		octaveSelect.setMaximumRowCount(octaves.length);
		octaveSelect.setFocusable(false);
		getContentPane().add(octaveSelect, gbc_octaveSelect);
		
		//JComboBox colorSelect = new JComboBox();
		GridBagConstraints gbc_colorSelect = new GridBagConstraints();
		gbc_colorSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_colorSelect.anchor = GridBagConstraints.NORTH;
		gbc_colorSelect.insets = new Insets(0, 0, 5, 5);
		gbc_colorSelect.gridx = 4;
		gbc_colorSelect.gridy = 3;
		colorSelect.setMaximumRowCount(colors.length);
		colorSelect.setFocusable(false);
		getContentPane().add(colorSelect, gbc_colorSelect);
		
		GridBagConstraints gbc_chckbxDeath = new GridBagConstraints();
		gbc_chckbxDeath.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDeath.gridx = 3;
		gbc_chckbxDeath.gridy = 1;
		death.setFocusable(false);
		getContentPane().add(death, gbc_chckbxDeath);
		
		//JCheckBox chckbxOsc = new JCheckBox("OSC");
		GridBagConstraints gbc_chckbxOsc = new GridBagConstraints();
		gbc_chckbxOsc.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOsc.gridx = 4;
		gbc_chckbxOsc.gridy = 1;
		OSC.setFocusable(false);
		getContentPane().add(OSC, gbc_chckbxOsc);
		
		GridBagConstraints gbc_chckbxShowNotes = new GridBagConstraints();
		gbc_chckbxShowNotes.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxShowNotes.gridx = 5;
		gbc_chckbxShowNotes.gridy = 1;
		showNotes.setFocusable(false);
		getContentPane().add(showNotes, gbc_chckbxShowNotes);
		
		GridBagConstraints gbc_lblNote = new GridBagConstraints();
		gbc_lblNote.anchor = GridBagConstraints.SOUTH;
		gbc_lblNote.insets = new Insets(0, 0, 5, 5);
		gbc_lblNote.gridx = 2;
		gbc_lblNote.gridy = 2;
		getContentPane().add(note, gbc_lblNote);
		
		GridBagConstraints gbc_lblOctave = new GridBagConstraints();
		gbc_lblOctave.anchor = GridBagConstraints.SOUTH;
		gbc_lblOctave.insets = new Insets(0, 0, 5, 5);
		gbc_lblOctave.gridx = 3;
		gbc_lblOctave.gridy = 2;
		getContentPane().add(octave, gbc_lblOctave);
		
		GridBagConstraints gbc_lblColor = new GridBagConstraints();
		gbc_lblColor.anchor = GridBagConstraints.SOUTH;
		gbc_lblColor.insets = new Insets(0, 0, 5, 5);
		gbc_lblColor.gridx = 4;
		gbc_lblColor.gridy = 2;
		getContentPane().add(color, gbc_lblColor);
		
		GridBagConstraints gbc_lblPath = new GridBagConstraints();
		gbc_lblPath.anchor = GridBagConstraints.SOUTH;
		gbc_lblPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblPath.gridx = 5;
		gbc_lblPath.gridy = 2;
		getContentPane().add(path, gbc_lblPath);
		
		//JButton insert = new JButton("Insert");
		GridBagConstraints gbc_insert = new GridBagConstraints();
		gbc_insert.fill = GridBagConstraints.HORIZONTAL;
		gbc_insert.insets = new Insets(0, 0, 5, 5);
		gbc_insert.gridx = 2;
		gbc_insert.gridy = 4;
		insert.setFocusable(false);
		getContentPane().add(insert, gbc_insert);
		
		//JButton delete = new JButton("Delete");
		GridBagConstraints gbc_delete = new GridBagConstraints();
		gbc_delete.fill = GridBagConstraints.HORIZONTAL;
		gbc_delete.insets = new Insets(0, 0, 5, 5);
		gbc_delete.gridx = 3;
		gbc_delete.gridy = 4;
		delete.setFocusable(false);
		getContentPane().add(delete, gbc_delete);
		
		GridBagConstraints gbc_lblBpm = new GridBagConstraints();
		gbc_lblBpm.insets = new Insets(0, 0, 5, 5);
		gbc_lblBpm.gridx = 2;
		gbc_lblBpm.gridy = 5;
		getContentPane().add(lblBpm, gbc_lblBpm);
		
		bpm.setText("240");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 5;
		bpm.setColumns(10);
		getContentPane().add(bpm, gbc_textField);
		
		GridBagConstraints gbc_lblIpAddress = new GridBagConstraints();
		gbc_lblIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpAddress.gridx = 2;
		gbc_lblIpAddress.gridy = 6;
		getContentPane().add(lblIpAddress, gbc_lblIpAddress);
		
		txtIpAddress.setText("localhost");
		GridBagConstraints gbc_txtIpAddress = new GridBagConstraints();
		gbc_txtIpAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpAddress.gridwidth = 2;
		gbc_txtIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtIpAddress.gridx = 3;
		gbc_txtIpAddress.gridy = 6;
		getContentPane().add(txtIpAddress, gbc_txtIpAddress);
		txtIpAddress.setColumns(10);
		
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 2;
		gbc_lblPort.gridy = 7;
		getContentPane().add(lblPort, gbc_lblPort);
		
		txtPort.setText("57110");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.gridwidth = 2;
		gbc_txtPort.insets = new Insets(0, 0, 5, 5);
		gbc_txtPort.gridx = 3;
		gbc_txtPort.gridy = 7;
		getContentPane().add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);
		
		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnImport.insets = new Insets(0, 0, 5, 5);
		gbc_btnImport.gridx = 2;
		gbc_btnImport.gridy = 9;
		importPreset.setFocusable(false);
		getContentPane().add(importPreset, gbc_btnImport);
		
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExport.insets = new Insets(0, 0, 5, 5);
		gbc_btnExport.gridx = 3;
		gbc_btnExport.gridy = 9;
		exportPreset.setFocusable(false);
		getContentPane().add(exportPreset, gbc_btnExport);
		
		//JButton score = new JButton("Export Score");
		GridBagConstraints gbc_score = new GridBagConstraints();
		gbc_score.fill = GridBagConstraints.HORIZONTAL;
		gbc_score.insets = new Insets(0, 0, 5, 5);
		gbc_score.gridx = 5;
		gbc_score.gridy = 9;
		score.setFocusable(false);
		getContentPane().add(score, gbc_score);
		setBounds(100, 100, 514, 367);
		
		controlSetup();
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Cellular");
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
	}
	
	public View getFrame() {
		return this;
	}
	
	public void controlSetup() {
		insert.setActionCommand("insert");
		delete.setActionCommand("delete");
		startStop.setActionCommand("startStop");
		clear.setActionCommand("clear");
		generate.setActionCommand("generate");
		birth.setActionCommand("birth");
		death.setActionCommand("death");
		OSC.setActionCommand("OSC");
		reset.setActionCommand("reset");
		score.setActionCommand("score");
		importPreset.setActionCommand("import");
		exportPreset.setActionCommand("export");
		showNotes.setActionCommand("showNotes");
	}
	
	public GamePanel getPanel() {
		return gamePanel;
	}
	
	public int getGamePanelSize() {
		return gamePanel.getPanelSize();
	}
	
	public String getPitch(){
		String pitch = (String) noteSelect.getSelectedItem();
		if(pitch.equals("Random")) {
			int rand = randomInt(0, 11);
			pitch = "C C#D D#E F F#G G#A A#B ".substring(2*rand, 2*rand+2);
		}
		return pitch;
	}
	
	public void setPitch(String pitch) {
		noteSelect.setSelectedItem(pitch);
	}
	
	public String getOctave() {
		String octave = (String) octaveSelect.getSelectedItem();
		if(octave.equals("Random")) {
			return Integer.toString(randomInt(0,9));
		}
		return octave;
	}
	
	public void setOctave(String octave) {
		noteSelect.setSelectedItem(octave);
	}
	
	public String getPath() {
		return (String) pathSelect.getSelectedItem();
	}
	
	public String getIP() {
		return txtIpAddress.getText();
	}
	
	public String getPort() {
		return txtPort.getText();
	}
	
	public String getBpm() {
		return bpm.getText();
	}
	
	public Color getColor() {
		switch ((String) colorSelect.getSelectedItem()) {
			case "Red": return Color.RED;
			case "Blue": return Color.BLUE;
			case "Green": return Color.GREEN;
			case "Yellow": return Color.YELLOW;
			case "Orange": return Color.ORANGE;
			case "Magenta": return Color.MAGENTA;
			case "Random": return new Color(randomInt(0,255),randomInt(0,255),randomInt(0,255));
			default: return null;
		}
	}
	
	public void setColor(String color) {
		colorSelect.setSelectedItem(color);
	}
	
	public int getCellSize() {
		return gamePanel.getCellSize();
	}
	
	public int getHoffset() {
		return gamePanel.getHoffset();
	}
	
	public int getVoffset() {
		return gamePanel.getVoffset();
	}
	
	public void changeShowNotes() {
		gamePanel.changeShowNotes();
	}
	
	public void setBirth(boolean birth) {
		this.birth.setSelected(birth);
	}
	
	public void setDeath(boolean death) {
		this.death.setSelected(death);
	}
	
	public void setOSC(boolean osc) {
		this.OSC.setSelected(osc);
	}
	
	public void setBPM(String bpm) {
		this.bpm.setText(bpm);
	}
	
	public void setIpAndPort(String ip, String port) {
		this.txtIpAddress.setText(ip);
		this.txtPort.setText(port);
	}
	
	public void setShowNotes(boolean showNotes) {
		this.showNotes.setSelected(showNotes);
		gamePanel.setShowNotes(showNotes);
	}
	
	public int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public void addListener(ActionListener listener) {
		startStop.addActionListener(listener);
		clear.addActionListener(listener);
		generate.addActionListener(listener);
		insert.addActionListener(listener);
		delete.addActionListener(listener);
		birth.addActionListener(listener);
		death.addActionListener(listener);
		OSC.addActionListener(listener);
		reset.addActionListener(listener);
		score.addActionListener(listener);
		showNotes.addActionListener(listener);
		importPreset.addActionListener(listener);
		exportPreset.addActionListener(listener);
	}
}
