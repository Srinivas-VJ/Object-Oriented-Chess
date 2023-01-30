import javax.swing.*;
import javax.swing.JFrame;
import controller.UIController;


public class Main {
    public static void main(String[] args) {
                JFrame f = new JFrame("Chess");
                f.setDefaultCloseOperation(3);
                UIController ui = new UIController();
                f.add(ui);
                f.setSize(700, 720);
                f.setVisible(true);
                f.repaint();
    }
}