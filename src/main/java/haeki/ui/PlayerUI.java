package haeki.ui;

import haeki.board.Board;
import haeki.board.BoardField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayerUI extends UI {

    private int buttonID = -3;

    private final ActionListener buttonActionListener = e -> buttonID = Integer.parseInt(e.getActionCommand());

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public int chooseField() {
        buttonID = -3;
        while(buttonID < -2) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println(e.getCause());
            }
        }
        //System.out.println("Field id:" + buttonID + " was chosen");
        return buttonID;
    }

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

        JPanel buttonPanel = new JPanel();
        JButton cancelButt = new JButton();
        cancelButt.setText("Cancel");
        cancelButt.addActionListener(buttonActionListener);
        cancelButt.setActionCommand(String.valueOf(CANCEL_MOVE));
        buttonPanel.add(cancelButt);
        JButton confirmButt = new JButton();
        confirmButt.setText("Confirm");
        confirmButt.addActionListener(buttonActionListener);
        confirmButt.setActionCommand(String.valueOf(CONFIRM_MOVE));
        buttonPanel.add(confirmButt);
        buttonPanel.setBackground(backgroundCol);
        lowerPanel.add(buttonPanel);

        mainPanel.add(lowerPanel);
    }



    @Override
    Component createBoardFieldPanel(int x, int y, BoardField field) {
        return new BoardFieldPanel(x,y,field);
    }


    class BoardFieldPanel extends BoardFieldPanelAbstract {

        BoardFieldPanel(int x, int y, BoardField bf) {
            setPreferredSize(new Dimension(36, 36));
            setLayout(new BorderLayout());
            JButton butt = new JButton();
            butt.setOpaque(false);
            butt.setContentAreaFilled(false);
            butt.setBorderPainted(false);
            butt.addActionListener(buttonActionListener);
            butt.setActionCommand(String.valueOf(x + (15*y)));
            butt.setVerticalAlignment(SwingConstants.CENTER);
            if(bf.isStar()) {
                butt.setIcon(icon_star);
            } else {
                butt.setIcon(icon_normal);
            }
            if(x == 7) {
                setWhiteBorder(true);
            }
            setBackground(fieldColors[bf.getFieldColor().getIndex()]);
            add(butt, BorderLayout.CENTER);
        }

        @Override
        public void setIcon(ImageIcon icon) {
            ((JButton) (getComponent(0))).setIcon(icon);
        }
    }

}