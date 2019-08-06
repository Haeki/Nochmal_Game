import javax.swing.*;
import java.awt.*;
import java.util.*;

public abstract class UI {

    final char[] abc = "ABCDEFGHIJKLMNO".toCharArray();
    final String[] pointsFirstKey = {"5","3","3","3","2","2","2","1R","2","2","2","3","3","3","5"};
    final String[] pointsLastKey =  {"3","2","2","2","1","1","1","0R","1","1","1","2","2","2","3"};

    public final static int CONFIRM_MOVE = -1;
    public final static int CANCEL_MOVE = -2;

    final Color[] fieldColors = {new Color(0x8b8a30), new Color(0xe9932c), new Color(0x7e8c99), new Color(0xd0393e), new Color(0xdc5f2f), Color.BLACK};

    Color backgroundCol = new Color(0);
    Color whiteCol = new Color(0xEEEEEE);

    JLabel[] pointsLabels = new JLabel[5];
    ColorFinishPanel[] colorFinishedPanels = new ColorFinishPanel[10];
    WhiteFieldPanel[] columnPointsFirstPanels = new WhiteFieldPanel[15];
    WhiteFieldPanel[] columnPointsLastPanels = new WhiteFieldPanel[15];
    JLabel[] jokerPanels = new JLabel[8];
    JPanel[] colorDicePanel = new JPanel[3];
    JLabel[] numberDiceLabel = new JLabel[3];

    ImageIcon icon_normal;
    ImageIcon icon_normal_checked;
    ImageIcon icon_star;
    ImageIcon icon_star_checked;
    ImageIcon icon_joker;
    ImageIcon icon_joker_checked;
    HashMap<String, ImageIcon> numbers = new HashMap<>(6);
    HashMap<String, ImageIcon> numbersChecked = new HashMap<>(6);
    HashMap<String, ImageIcon> numbersCircled = new HashMap<>(6);

    JPanel boardPanel;
    JFrame frame;


    public void initUI(String winText, Board board) {
        loadImages();

        frame = new JFrame(winText);
        JPanel mainPanel = new JPanel();
        //mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK);

        addSubPanels(mainPanel, board);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //frame.setLayout(new BoxLayout(frame,  BoxLayout.X_AXIS));
        frame.getContentPane().add(mainPanel);

        // Finally, set some default options and pack the window so the content will appear.
        frame.pack();

        frame.setSize(new Dimension(1024, 720));
        //frame.addKeyListener(new SynchronizedKeyListener(this));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        //frame.setResizable(true);
        frame.setVisible(true);
    }

    public abstract void addSubPanels(JPanel mainPanel, Board board);

    public JPanel createGamePanel(Board board) {
        JPanel gamePanel = new JPanel();
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gamePanel.setBackground(backgroundCol);

        JPanel abcHeaderPanel = createWhiteFieldPanel();
        for (int i = 0; i < abc.length; i++) {
            WhiteFieldPanel innerPanel;
            if( i == 7) {
                innerPanel = new WhiteFieldPanel(String.valueOf(abc[i]), Color.RED);
            } else {
                innerPanel = new WhiteFieldPanel(String.valueOf(abc[i]));
            }
            abcHeaderPanel.add(innerPanel);
        }
        gamePanel.add(abcHeaderPanel);


        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(7, 15, 3,3));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
        boardPanel.setBackground(backgroundCol);
        gamePanel.add(boardPanel);


        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 15; x++) {
                boardPanel.add(createBoardFieldPanel(x, y, board.getField(x, y)));
            }
        }

        JPanel pointsFirstPanel = createWhiteFieldPanel();
        for (int i = 0; i < pointsFirstKey.length; i++) {
            WhiteFieldPanel innerPanel = new WhiteFieldPanel(numbers.get(pointsFirstKey[i]));
            columnPointsFirstPanels[i] = innerPanel;
            pointsFirstPanel.add(innerPanel);
        }
        gamePanel.add(pointsFirstPanel);

        JPanel pointsLastPanel = createWhiteFieldPanel();
        for (int i = 0; i < pointsLastKey.length; i++) {
            WhiteFieldPanel innerPanel = new WhiteFieldPanel(numbers.get(pointsLastKey[i]));
            columnPointsLastPanels[i] = innerPanel;
            pointsLastPanel.add(innerPanel);
        }
        pointsLastPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        gamePanel.add(pointsLastPanel);

        JokerFieldsPanel jokerPanel = new JokerFieldsPanel();
        for (int i = 0; i < 8; i++) {
            JLabel innerPanel = new JLabel(icon_joker);
            jokerPanels[i] = innerPanel;
            jokerPanel.add(innerPanel);
        }
        gamePanel.add(jokerPanel);

        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        return gamePanel;
    }

    abstract Component createBoardFieldPanel(int x, int y, BoardField field);

    public JPanel createScoresPanel() {
        JPanel scoresPanel = new JPanel();
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 5));
        scoresPanel.setBackground(backgroundCol);


        JPanel colorPanelHelper = new JPanel();
        colorPanelHelper.setBackground(backgroundCol);
        colorPanelHelper.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel finishedColorsPanel = new JPanel();
        finishedColorsPanel.setBackground(backgroundCol);
        finishedColorsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        finishedColorsPanel.setLayout(new GridLayout(5, 2, 3,3));

        for (int i = 0; i < 5; i++) {
            ColorFinishPanel colPanel = new ColorFinishPanel(i, "5");
            colorFinishedPanels[i * 2] = colPanel;
            finishedColorsPanel.add(colPanel);
            ColorFinishPanel colPanel2 = new ColorFinishPanel(i, "3");
            colorFinishedPanels[(i * 2) + 1] = colPanel2;
            finishedColorsPanel.add(colPanel2);
        }
        colorPanelHelper.add(finishedColorsPanel);
        scoresPanel.add(colorPanelHelper);

        JPanel individualScoresPanel = new JPanel();
        individualScoresPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        individualScoresPanel.setBackground(backgroundCol);
        individualScoresPanel.add(createPointsPanel("BONUS","= ", Color.BLACK, 0));
        individualScoresPanel.add(createPointsPanel("A - O","+ ", new Color(0, 134, 17), 1));
        individualScoresPanel.add(createPointsPanel("! (+1)","+ ", new Color(0, 134, 17), 2));
        individualScoresPanel.add(createPointsPanel("(-2)","-  ", Color.RED, 3));
        JLabel separatorLabel = new JLabel("---------------------------");
        separatorLabel.setForeground(Color.WHITE);
        individualScoresPanel.add(separatorLabel);
        individualScoresPanel.add(createPointsPanel("TOTAL","= ", Color.BLACK, 4));

        individualScoresPanel.setLayout(new BoxLayout(individualScoresPanel, BoxLayout.Y_AXIS));

        scoresPanel.add(individualScoresPanel);

        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));

        return scoresPanel;
    }

    private JPanel createPointsPanel(String pointText, String operator, Color operatorColor, int index) {
        JPanel pointsPanel = new JPanel();
        pointsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pointsPanel.setBackground(backgroundCol);
        JLabel lbl = new JLabel(pointText);
        lbl.setForeground(Color.WHITE);
        pointsPanel.add(lbl);
        pointsPanel.add(new WideWhiteFieldPanel(operator, operatorColor, 75, 38, index));
        return pointsPanel;
    }

    JPanel createDicePanel() {
        JPanel dicePanel = new JPanel();
        dicePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dicePanel.setBackground(backgroundCol);
        dicePanel.setLayout(new FlowLayout()); //GridLayout(2,4,8,8));

        JLabel colorText = new JLabel("Colors: ");
        colorText.setFont(colorText.getFont().deriveFont(Font.BOLD, 22));
        colorText.setForeground(Color.WHITE);
        dicePanel.add(colorText);
        for (int i = 0; i < 3; i++) {
            RoundedJPanel panel = new RoundedJPanel();
            panel.setPreferredSize(new Dimension(42, 42));
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createMatteBorder(4,4,4,4, Color.WHITE));
            //JLabel lbl = new JLabel();
            //lbl.setOpaque(false);

            //lbl.setVerticalAlignment(SwingConstants.CENTER);
            //lbl.setHorizontalAlignment(SwingConstants.CENTER);
            //lbl.setIcon(icon_normal);
            panel.setBackground(fieldColors[1]);
            //panel.add(lbl, BorderLayout.CENTER);

            colorDicePanel[i] = panel;
            dicePanel.add(panel);
        }
        JLabel numberText = new JLabel("Numbers: ");
        numberText.setFont(numberText.getFont().deriveFont(Font.BOLD, 26));
        numberText.setForeground(Color.WHITE);
        numberText.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        dicePanel.add(numberText);
        for (int i = 0; i < 3; i++) {
            RoundedJPanel panel = new RoundedJPanel();
            panel.setPreferredSize(new Dimension(42, 42));
            panel.setBackground(Color.WHITE);
            JLabel lbl = new JLabel("0");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 22));
            lbl.setForeground(Color.BLACK);
            panel.add(lbl);
            numberDiceLabel[i] = lbl;
            dicePanel.add(panel);
        }

        return  dicePanel;
    }

    public void updateDices(ArrayList<Integer> col, ArrayList<Integer> num) {
        for (int i = 0; i < col.size(); i++) {
            //System.out.println("--- Setting Dice Color to " + col.get(i));
            colorDicePanel[i].setBackground(fieldColors[col.get(i)]);
        }
        if(col.size() < 3) {
            colorDicePanel[2].setVisible(false);
        } else {
            colorDicePanel[2].setVisible(true);
        }
        for (int i = 0; i < num.size(); i++) {
            if(num.get(i) == 5) {
                numberDiceLabel[i].setText("?");
            } else {
                numberDiceLabel[i].setText(String.valueOf(num.get(i) + 1));
            }
        }
        if(num.size() < 3) {
            numberDiceLabel[2].getParent().setVisible(false);
        } else {
            numberDiceLabel[2].getParent().setVisible(true);
        }
    }

    public void updateColorFinished(TreeSet<BoardField.FieldColor> colorFirst, TreeSet<BoardField.FieldColor> colorsLast, TreeSet<BoardField.FieldColor> colorsFinished) {
        for (BoardField.FieldColor col : colorsFinished) {
            colorFinishedPanels[col.getIndex() * 2].setIcon(numbersChecked.get("5"));
        }
        for (BoardField.FieldColor col : colorFirst) {
            colorFinishedPanels[col.getIndex() * 2].setIcon(numbersCircled.get("5"));
        }
        for (BoardField.FieldColor col : colorsLast) {
            colorFinishedPanels[(col.getIndex() * 2) + 1].setIcon(numbersCircled.get("3"));
        }
    }

    public void updateJokers(int used) {
        for (int i = 0; i < used; i++) {
            jokerPanels[i].setIcon(icon_joker_checked);
        }
    }

    public void updateColumnFinished(TreeSet<Integer> columnsFirst, TreeSet<Integer> columnsLast, TreeSet<Integer> columnsFinished) {
        for(int i : columnsFinished) {
            columnPointsFirstPanels[i].setIcon(numbersChecked.get(pointsFirstKey[i]));
        }
        for(int i : columnsFirst) {
            columnPointsFirstPanels[i].setIcon(numbersCircled.get(pointsFirstKey[i]));
        }
        for(int i : columnsLast) {
            columnPointsLastPanels[i].setIcon(numbersCircled.get(pointsLastKey[i]));
        }
    }

    public void updateScore(int[] scores) {
        for (int i = 0; i < scores.length; i++) {
            pointsLabels[i].setText(String.valueOf(scores[i]));
        }
    }

    /*
    public void confirmField(BoardField boardField) {
        BoardFieldPanelAbstract boardFieldPanel = (BoardFieldPanelAbstract) boardPanel.getComponent(boardField.getIndex());
        if(boardField.isStar()) {
            boardFieldPanel.setIcon(icon_star_checked);
        } else {
            boardFieldPanel.setIcon(icon_normal_checked);
        }
    }*/

    public void checkField(BoardField checkedField) {
        BoardFieldPanelAbstract boardFieldPanel = (BoardFieldPanelAbstract) boardPanel.getComponent(checkedField.getIndex());
        if(checkedField.isStar()) {
            boardFieldPanel.setIcon(icon_star_checked);
        } else {
            boardFieldPanel.setIcon(icon_normal_checked);
        }
        //System.out.println("check Field: " + checkedField.getX() + "|" + checkedField.getY());
    }

    public void checkFields(Iterator<BoardField> fieldsIt) {
        while (fieldsIt.hasNext()) {
            checkField(fieldsIt.next());
        }
    }

    public void uncheckField(BoardField checkedField) {
        BoardFieldPanelAbstract boardFieldPanel = (BoardFieldPanelAbstract) boardPanel.getComponent(checkedField.getIndex());
        if(checkedField.isStar()) {
            boardFieldPanel.setIcon(icon_star);
        } else {
            boardFieldPanel.setIcon(icon_normal);
        }
        //System.out.println("uncheck Field: " + checkedField.getX() + "|" + checkedField.getY());
    }

    public void uncheckFields(Iterator<BoardField> fieldsIt) {
        while(fieldsIt.hasNext()) {
            uncheckField(fieldsIt.next());
        }
    }

    void loadImages() {
        icon_normal = new ImageIcon("res/icons/normal_32.png");
        icon_normal_checked = new ImageIcon("res/icons/normal_checked_32.png");
        icon_star = new ImageIcon("res/icons/star_32.png");
        icon_star_checked = new ImageIcon("res/icons/star_checked_32.png");
        icon_joker = new ImageIcon("res/icons/joker_32.png");
        icon_joker_checked = new ImageIcon("res/icons/joker_checked_32.png");

        char[] numbs = new char[] {'1','2','3','5'};
        for (char n: numbs) {
            numbers.put(String.valueOf(n), new ImageIcon("res/icons/numbers/" + n + "_32.png"));
            numbersChecked.put(String.valueOf(n), new ImageIcon("res/icons/numbers/" + n + "_checked_32.png"));
            numbersCircled.put(String.valueOf(n), new ImageIcon("res/icons/numbers/" + n + "_circled_32.png"));
        }

        char[] redNumbs = new char[] {'0', '1'};
        for (char n: redNumbs) {
            numbers.put(n + "R", new ImageIcon("res/icons/numbers/" + n + "_red_32.png"));
            numbersChecked.put(n + "R", new ImageIcon("res/icons/numbers/" + n + "_red_checked_32.png"));
            numbersCircled.put(n + "R", new ImageIcon("res/icons/numbers/" + n + "_red_circled_32.png"));
        }
    }

    public JPanel createWhiteFieldPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundCol);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 3,3));  //new GridLayout(1, 15, 3,3));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        //2panel.setPreferredSize(new Dimension(64,32));
        return panel;
    }

    class JokerFieldsPanel extends RoundedJPanel {

        public JokerFieldsPanel() {
            setBackground(Color.WHITE);
            setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));  //new GridLayout(1, 15, 3,3));
            setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            JLabel jokerText = new JLabel("?/X = ");
            jokerText.setFont(jokerText.getFont().deriveFont(Font.BOLD, 26));
            jokerText.setForeground(Color.BLACK);
            add(jokerText);
            //setPreferredSize(new Dimension(400, 40));
        }
    }

    class ColorFinishPanel extends RoundedJPanel {
        JLabel lbl;

        public void setIcon(ImageIcon icon) {
            lbl.setIcon(icon);
        }

        public ColorFinishPanel(int c, String val) {
            setLayout(new GridBagLayout());
            lbl = new JLabel(numbers.get(val));
            //lbl.setText(val);
            setBackground(fieldColors[c]);
            add(lbl);
            setPreferredSize(new Dimension(36, 36));
        }
    }

    class WideWhiteFieldPanel extends WhiteFieldPanel {

        public WideWhiteFieldPanel(String text,  Color fontColor, int fieldSizeX, int fieldSizeY, int index) {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBackground(whiteCol);
            JLabel lbl = new JLabel(text);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 24));
            lbl.setForeground(fontColor);
            add(lbl);
            JLabel lbl2 = new JLabel("");
            lbl2.setFont(lbl.getFont().deriveFont(24));
            lbl2.setForeground(Color.BLACK);
            pointsLabels[index] = lbl2;
            add(lbl2);
            setPreferredSize(new Dimension(fieldSizeX, fieldSizeY));
        }
    }

    class WhiteFieldPanel extends RoundedJPanel {
        JLabel lbl;

        public void init() {
            setLayout(new GridBagLayout());
            setBackground(whiteCol);
            setPreferredSize(new Dimension(36, 36));
        }

        public WhiteFieldPanel() {
            super();
        }

        public WhiteFieldPanel(ImageIcon img) {
            super();
            init();
            lbl = new JLabel(img);
            add(lbl);
        }

        public WhiteFieldPanel(String text, Color fontColor) {
            super();
            init();
            lbl = new JLabel(text);
            lbl.setForeground(fontColor);
            add(lbl);
        }

        public WhiteFieldPanel(String text) {
            super();
            init();
            lbl = new JLabel(text);
            lbl.setForeground(Color.BLACK);
            add(lbl);
        }

        public void setIcon(ImageIcon icon) {
            lbl.setIcon(icon);
        }
    }

    abstract class BoardFieldPanelAbstract extends RoundedJPanel {
        abstract void setIcon(ImageIcon icon);
    }


    /*
        public BoardFieldPanel(int x, int y, BoardField bf) {
            setPreferredSize(new Dimension(36, 36));
            boardField = bf;
            setLayout(new BorderLayout());
            JLabel lbl = new JLabel();
            lbl.setOpaque(false);

            lbl.setVerticalAlignment(SwingConstants.CENTER);

            if(boardField.isStar()) {
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
    }*/

    public class RoundedJPanel extends JPanel {

        /** Double values for Horizontal and Vertical radius of corner arcs */
        protected Dimension arcs = new Dimension(8, 8);
        boolean whiteBorder = false;

        public RoundedJPanel() {
            super();
            setOpaque(false);
        }

        public void setWhiteBorder(boolean b) {
            whiteBorder = b;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Draws the rounded opaque panel with borders.
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);
            if(whiteBorder) {
                graphics.setColor(Color.WHITE);
                graphics.setStroke(new BasicStroke(2));
                graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
            }
            graphics.setColor(getForeground());

            // Sets strokes to default, is better.
            graphics.setStroke(new BasicStroke());
        }

        public void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintChildren(g2);
        }
    }

    public void showMessage(String Msg) {
        JOptionPane.showMessageDialog(frame, Msg);
    }
}
