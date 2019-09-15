package haeki.ui;

import haeki.player.AlgoPlayer;
import haeki.player.HumanPlayer;
import haeki.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class MenuUI {
    private final HashMap<String, Path> boards;
    private final JComboBox<String> boardChooser;
    private final ArrayList<JComboBox<String>> playerComboBoxen = new ArrayList<>();
    private final JPanel comboBoxPanel;
    private final String[] items = {"None", "Human Player", "Algo Player"};

    private final ActionListener comboBoxListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JComboBox cb = (JComboBox) actionEvent.getSource();
            //System.out.println("ComboBox: " + playerComboBoxen.indexOf(cb) + " changed to " + cb.getSelectedItem() + " playerComboBoxen.size() = " + playerComboBoxen.size());
            if(cb.getSelectedItem() == null) {
                System.err.println("ComboBox SelectedItem as String should not be null");
                return;
            }
            if (!cb.getSelectedItem().equals("None") && playerComboBoxen.indexOf(cb) == playerComboBoxen.size() - 1) {
                //System.out.println("Add new ComboBox");
                createPlayerComboBox();
                comboBoxPanel.updateUI();
            } else if (cb.getSelectedItem().equals("None") && playerComboBoxen.indexOf(cb) != playerComboBoxen.size() - 1) {
                comboBoxPanel.remove(cb);
                playerComboBoxen.remove(cb);
                comboBoxPanel.updateUI();
            }
        }
    };

    private boolean startGame = false;

    public MenuUI() {
        JFrame frame = new JFrame("Nochmal Game Menu");
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Noch Mal", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 48));
        titleLabel.setForeground(new Color(0xB06400));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        frame.getContentPane().add(titleLabel, c);

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        JLabel boardLabel = new JLabel("Choose Board Color: ");
        boardLabel.setFont(boardLabel.getFont().deriveFont(Font.PLAIN, 24));
        boardLabel.setForeground(Color.WHITE);
        boardPanel.add(boardLabel);
        boards = getBoards();
        boardChooser = new JComboBox<>(boards.keySet().toArray(new String[0]));
        boardChooser.setFont(boardChooser.getFont().deriveFont(Font.PLAIN, 24));
        ((JLabel)boardChooser.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        boardChooser.setSelectedIndex(0);
        boardChooser.setForeground(Color.WHITE);
        boardChooser.setBackground(Color.BLACK);
        boardPanel.add(boardChooser);
        c.gridy = 1;
        frame.getContentPane().add(boardPanel, c);

        JLabel playerLabel = new JLabel("Spieler:", JLabel.LEFT);
        playerLabel.setFont(playerLabel.getFont().deriveFont(Font.PLAIN, 24));
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        c.gridy = 2;
        frame.getContentPane().add(playerLabel, c);

        comboBoxPanel = new JPanel();
        JComboBox<String> comboBox1 = new JComboBox<>(new String[]{"Human Player", "Algo Player"});
        comboBox1.setForeground(Color.WHITE);
        comboBox1.setBackground(Color.BLACK);
        ((JLabel)comboBox1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        comboBox1.setSelectedIndex(0);
        comboBox1.setFont(comboBox1.getFont().deriveFont(Font.PLAIN, 24));
        playerComboBoxen.add(comboBox1);
        comboBoxPanel.add(comboBox1);

        createPlayerComboBox();

        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(comboBoxPanel);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        c.gridy = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(scrollPane, c);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(startButton.getFont().deriveFont(Font.PLAIN, 24));
        startButton.addActionListener(actionEvent -> startGame = true);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        buttonPanel.add(startButton);
        buttonPanel.setBackground(Color.BLACK);
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        frame.getContentPane().add(buttonPanel, c);

        // Finally, set some default options and pack the window so the content will appear.
        frame.pack();
        frame.setSize(new Dimension(420, 512));
        //frame.addKeyListener(new SynchronizedKeyListener(this));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private JComboBox<String> createPlayerComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setForeground(Color.WHITE);
        comboBox.setBackground(Color.BLACK);
        comboBox.setSelectedIndex(0);
        ((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        comboBox.setFont(comboBox.getFont().deriveFont(Font.PLAIN, 24));
        comboBox.addActionListener(comboBoxListener);
        playerComboBoxen.add(comboBox);
        comboBoxPanel.add(comboBox);
        return comboBox;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        int playerNum = 1;
        for (JComboBox<String> cb : playerComboBoxen) {
            String selectedItemString = (String) cb.getSelectedItem();
            if (selectedItemString == null) {
                System.err.println("ComboBox SelectedItem as String should not be null");
            } else {
                if (selectedItemString.equals("Human Player")) {
                    players.add(new HumanPlayer(playerNum));
                } else if (selectedItemString.equals("Algo Player")) {
                    players.add(new AlgoPlayer(playerNum));
                }
            }
            playerNum++;
        }
        return players;
    }

    public Path getBoard() {
        return boards.get(boardChooser.getSelectedItem());
    }

    private HashMap<String, Path> getBoards() {
        HashMap<String, Path> boardNames = new HashMap<>();
        URL url = getClass().getClassLoader().getResource("res/boards/");

        URI uri;
        try {
            assert url != null;
            uri = url.toURI();
            if (url.getProtocol().equals("file")) {
                for (Path path : Files.newDirectoryStream(Paths.get(uri), path -> path.toString().endsWith(".board"))) {
                    boardNames.put(Files.lines(path).findFirst().get(), path);
                }
            } else {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                for (Path path : Files.newDirectoryStream(fileSystem.getPath("res/boards"), path -> path.toString().endsWith(".board"))) {
                    boardNames.put(Files.lines(path).findFirst().get(), path);
                }
                fileSystem.close();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return boardNames;
    }

    public boolean startGame() {
        return startGame;
    }
}
