package NewView;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class View extends JFrame {

	private JTextField txtIpAddress;
	private JTextField txtPort;
	
	private GamePanel gamePanel; //holds the actual grid
	private int cellSize;
	private int numCells;
	private String[] notes = {"- ", "C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "A#", "B "};
	private String[] octaves = {"-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	private String[] colors = {"-", "Red", "Blue", "Green", "Yellow", "Orange", "Magenta", "Random"};
	private String[] pathOptions = {"-", "Drawn", "Random", "Birth"};
	
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

	/**
	 * Create the application.
	 */
	public View(int newNumCells) {
		cellSize = 50;
		numCells = newNumCells;
		gamePanel = new GamePanel(cellSize*numCells, numCells);
		
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
//				System.out.println(getSize().getWidth());
//				System.out.println(gamePanel.getSize().getWidth());
//				setSize((int)getSize().getWidth(), (int)Math.floor((int) getSize().getWidth() / numCells)*numCells);
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
		gridBagLayout.columnWidths = new int[]{220, 0, 60, 50, 60, 92, 0};
		gridBagLayout.rowHeights = new int[]{34, 47, 21, 20, 23, 34, 0, 31, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.BLACK);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridheight = 8;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(gamePanel, gbc_panel);
		
//		JButton startStop = new JButton("Start/Stop");
//		startStop.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
		GridBagConstraints gbc_startStop = new GridBagConstraints();
		gbc_startStop.insets = new Insets(0, 0, 5, 5);
		gbc_startStop.gridx = 2;
		gbc_startStop.gridy = 0;
		getContentPane().add(startStop, gbc_startStop);
		
		//JButton clear = new JButton("Clear");
		GridBagConstraints gbc_clear = new GridBagConstraints();
		gbc_clear.insets = new Insets(0, 0, 5, 5);
		gbc_clear.gridx = 3;
		gbc_clear.gridy = 0;
		getContentPane().add(clear, gbc_clear);
		
		//JButton reset = new JButton("Reset");
		GridBagConstraints gbc_reset = new GridBagConstraints();
		gbc_reset.insets = new Insets(0, 0, 5, 5);
		gbc_reset.gridx = 4;
		gbc_reset.gridy = 0;
		getContentPane().add(reset, gbc_reset);
		
		//JButton generate = new JButton("Generate");
		GridBagConstraints gbc_generate = new GridBagConstraints();
		gbc_generate.insets = new Insets(0, 0, 5, 0);
		gbc_generate.gridx = 5;
		gbc_generate.gridy = 0;
		getContentPane().add(generate, gbc_generate);
		
		//JCheckBox chckbxBirth = new JCheckBox("Birth");
		GridBagConstraints gbc_chckbxBirth = new GridBagConstraints();
		gbc_chckbxBirth.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBirth.gridx = 2;
		gbc_chckbxBirth.gridy = 1;
		getContentPane().add(birth, gbc_chckbxBirth);
		
		//JCheckBox chckbxOsc = new JCheckBox("OSC");
		GridBagConstraints gbc_chckbxOsc = new GridBagConstraints();
		gbc_chckbxOsc.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOsc.gridx = 4;
		gbc_chckbxOsc.gridy = 1;
		getContentPane().add(OSC, gbc_chckbxOsc);
		
		//JComboBox pathSelect = new JComboBox();
		GridBagConstraints gbc_pathSelect = new GridBagConstraints();
		gbc_pathSelect.anchor = GridBagConstraints.NORTH;
		gbc_pathSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_pathSelect.insets = new Insets(0, 0, 5, 5);
		gbc_pathSelect.gridx = 5;
		gbc_pathSelect.gridy = 3;
		getContentPane().add(pathSelect, gbc_pathSelect);
		
		//JComboBox noteSelect = new JComboBox();
		GridBagConstraints gbc_noteSelect = new GridBagConstraints();
		gbc_noteSelect.anchor = GridBagConstraints.NORTH;
		gbc_noteSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_noteSelect.insets = new Insets(0, 0, 5, 5);
		gbc_noteSelect.gridx = 2;
		gbc_noteSelect.gridy = 3;
		getContentPane().add(noteSelect, gbc_noteSelect);
		
		//JComboBox octaveSelect = new JComboBox();
		GridBagConstraints gbc_octaveSelect = new GridBagConstraints();
		gbc_octaveSelect.anchor = GridBagConstraints.NORTH;
		gbc_octaveSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_octaveSelect.insets = new Insets(0, 0, 5, 5);
		gbc_octaveSelect.gridx = 3;
		gbc_octaveSelect.gridy = 3;
		getContentPane().add(octaveSelect, gbc_octaveSelect);
		
		//JComboBox colorSelect = new JComboBox();
		GridBagConstraints gbc_colorSelect = new GridBagConstraints();
		gbc_colorSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_colorSelect.anchor = GridBagConstraints.NORTH;
		gbc_colorSelect.insets = new Insets(0, 0, 5, 0);
		gbc_colorSelect.gridx = 4;
		gbc_colorSelect.gridy = 3;
		getContentPane().add(colorSelect, gbc_colorSelect);
		
		GridBagConstraints gbc_lblNote = new GridBagConstraints();
		gbc_lblNote.insets = new Insets(0, 0, 5, 5);
		gbc_lblNote.gridx = 2;
		gbc_lblNote.gridy = 2;
		getContentPane().add(note, gbc_lblNote);
		
		GridBagConstraints gbc_lblOctave = new GridBagConstraints();
		gbc_lblOctave.insets = new Insets(0, 0, 5, 5);
		gbc_lblOctave.gridx = 3;
		gbc_lblOctave.gridy = 2;
		getContentPane().add(octave, gbc_lblOctave);
		
		GridBagConstraints gbc_lblColor = new GridBagConstraints();
		gbc_lblColor.insets = new Insets(0, 0, 5, 5);
		gbc_lblColor.gridx = 4;
		gbc_lblColor.gridy = 2;
		getContentPane().add(color, gbc_lblColor);
		
		GridBagConstraints gbc_lblPath = new GridBagConstraints();
		gbc_lblPath.insets = new Insets(0, 0, 5, 0);
		gbc_lblPath.gridx = 5;
		gbc_lblPath.gridy = 2;
		getContentPane().add(path, gbc_lblPath);
		
		//JButton insert = new JButton("Insert");
		GridBagConstraints gbc_insert = new GridBagConstraints();
		gbc_insert.anchor = GridBagConstraints.NORTH;
		gbc_insert.insets = new Insets(0, 0, 5, 5);
		gbc_insert.gridx = 2;
		gbc_insert.gridy = 4;
		getContentPane().add(insert, gbc_insert);
		
		//JButton delete = new JButton("Delete");
		GridBagConstraints gbc_delete = new GridBagConstraints();
		gbc_delete.anchor = GridBagConstraints.NORTH;
		gbc_delete.fill = GridBagConstraints.HORIZONTAL;
		gbc_delete.insets = new Insets(0, 0, 5, 5);
		gbc_delete.gridx = 3;
		gbc_delete.gridy = 4;
		getContentPane().add(delete, gbc_delete);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setText("IP Address");
		GridBagConstraints gbc_txtIpAddress = new GridBagConstraints();
		gbc_txtIpAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpAddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtIpAddress.gridwidth = 4;
		gbc_txtIpAddress.gridx = 2;
		gbc_txtIpAddress.gridy = 6;
		getContentPane().add(txtIpAddress, gbc_txtIpAddress);
		txtIpAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("Port");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.anchor = GridBagConstraints.NORTH;
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.gridwidth = 4;
		gbc_txtPort.gridx = 2;
		gbc_txtPort.gridy = 7;
		getContentPane().add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);
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
		OSC.setActionCommand("OSC");
		reset.setActionCommand("reset");
	}
	
	public GamePanel getPanel() {
		return gamePanel;
	}
	
	public int getGamePanelSize() {
		return gamePanel.getPanelSize();
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
	
	public int getCellSize() {
		return gamePanel.getCellSize();
	}
	
	public int getHoffset() {
		return gamePanel.getHoffset();
	}
	
	public int getVoffset() {
		return gamePanel.getVoffset();
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
