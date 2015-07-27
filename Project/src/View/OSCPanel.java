package View;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OSCPanel extends JPanel {
	
	private JLabel addressLabel = new JLabel("Send to IP Address:");
	private JTextField address = new JTextField(20);
	
	public OSCPanel() {
		add(addressLabel);
		add(address);
	}

}
