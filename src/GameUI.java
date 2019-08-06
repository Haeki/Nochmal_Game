import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameUI {
    private JFrame frame;

    public void initUI(String winText) {
        frame = new JFrame(winText);

        frame.pack();

        frame.setSize(new Dimension(720, 512));
        //frame.addKeyListener(new SynchronizedKeyListener(this));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        //frame.setResizable(true);
        frame.setVisible(true);
    }

    public int getButtonInput() {
        return 1;
    }

    public int[] getDiceSelect(ArrayList<Integer> colorDice, ArrayList<Integer> numberDice) {
        return new int[2];
    }
}