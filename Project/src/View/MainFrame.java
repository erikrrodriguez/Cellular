package View;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame{
	
	private Frame firstTab;

	public MainFrame(Frame mainScreen, OSCPanel osc) {
		JTabbedPane tabs=new JTabbedPane();
		
		firstTab = mainScreen;
		
        tabs.addTab("Game", firstTab);
        
        tabs.addTab("OSCSend", osc);
        
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
