package View;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame{
	
	private Frame firstTab;
	private OSCPanel secondTab;

	public MainFrame(Frame mainScreen, OSCPanel osc) {
		JTabbedPane tabs=new JTabbedPane();
		
		firstTab = mainScreen;
		secondTab = osc;
		
        tabs.addTab("Game", firstTab);
        tabs.addTab("OSCSend", secondTab);
        setContentPane(tabs);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Cellular");
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	public Frame getFrame() {
		return firstTab;
	}

}
