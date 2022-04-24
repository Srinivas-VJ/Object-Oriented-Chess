
import javax.swing.JFrame;

public class test {
	public static void main(String args[]) {
		JFrame f = new JFrame("Chess");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testUI ui = new testUI();
		f.add(ui);
		f.setSize(700, 720);
		f.setVisible(true);
		f.repaint();
	}

}
