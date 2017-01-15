package View;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame{
	
	private FirstTabPanel firstTab;
	private OSCPanel secondTab;

	public MainFrame(FirstTabPanel mainScreen, OSCPanel osc) {
		JTabbedPane tabs=new JTabbedPane();
		
		tabs.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				System.out.println(mainScreen.getGamePanelSize());
	            
	        }
		});
		
		
		firstTab = mainScreen;
		secondTab = osc;
		
        tabs.addTab("Game", firstTab);
        tabs.addTab("OSCSend", secondTab);
        setContentPane(tabs);
		
        pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Cellular");
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		
		System.out.println(mainScreen.getGamePanelSize());
	}
	
	public FirstTabPanel getFrame() {
		return firstTab;
	}
	
	public void onResize() {
		
	}

}
