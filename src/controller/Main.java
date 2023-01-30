package controller;
import javax.swing.JFrame;
public class Main {
    public static void main(String args[]) {
        JFrame f = new JFrame("Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIController ui = new UIController();
        f.add(ui);
        f.setSize(700, 720);
        f.setVisible(true);
        f.repaint();
    }

}
