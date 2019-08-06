import javax.swing.*;
import java.awt.*;

public class AlgoUI extends UI{

    @Override
    public void addSubPanels(JPanel mainPanel, Board board) {
        JPanel upperPanel = new JPanel(new FlowLayout());
        upperPanel.setBackground(backgroundCol);
        upperPanel.add(createGamePanel(board));
        upperPanel.add(createScoresPanel());
        mainPanel.add(upperPanel);
        JPanel lowerPanel = new JPanel(new FlowLayout());
        lowerPanel.setBackground(backgroundCol);
        lowerPanel.add(createDicePanel());
        mainPanel.add(lowerPanel);
    }

    @Override
    Component createBoardFieldPanel(int x, int y, BoardField field) {
        return new BoardFieldPanel(x, field);
    }

    class BoardFieldPanel extends BoardFieldPanelAbstract {

        public BoardFieldPanel(int x, BoardField bf) {
            setPreferredSize(new Dimension(36, 36));
            setLayout(new BorderLayout());
            JLabel lbl = new JLabel();
            lbl.setOpaque(false);

            lbl.setVerticalAlignment(SwingConstants.CENTER);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            if(bf.isStar()) {
                lbl.setIcon(icon_star);
            } else {
                lbl.setIcon(icon_normal);
            }
            if(x == 7) {
                setWhiteBorder(true);
            }
            setBackground(fieldColors[bf.getFieldColor().getIndex()]);
            add(lbl, BorderLayout.CENTER);
        }

        @Override
        public void setIcon(ImageIcon icon) {
            ((JLabel) (getComponent(0))).setIcon(icon);
        }
    }
}
