package View;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OSCPanel extends JPanel {
	
	private JLabel addressLabel = new JLabel("IP Address:");
	private JTextField address = new JTextField(15);
	
	private JLabel portLabel = new JLabel("Port #:");
	private JTextField port = new JTextField(5);
	
	public OSCPanel() {
		add(addressLabel);
		add(address);
		
		add(portLabel);
		add(port);
	}

}
