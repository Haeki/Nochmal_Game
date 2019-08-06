import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {
    public final static int[] pointsFirst = {5,3,3,3,2,2,2,1,2,2,2,3,3,3,5};
    public final static int[] pointsLast =  {3,2,2,2,1,1,1,0,1,1,1,2,2,2,3};

    BoardField[] boardArr = new BoardField[105];
    private ArrayList<Player> players;
    private GameUI gameUI;
    ArrayList<Integer> colorDice = new ArrayList<>(Arrays.asList(1,1,1));
    ArrayList<Integer> numberDice = new ArrayList<>(Arrays.asList(1,1,1));
    TreeSet<Integer> completeColumns = new TreeSet<>();
    TreeSet<BoardField.FieldColor> completeColors = new TreeSet<>();
    int rounds;
    int activePlayer;
    String boardName = "error";
    Random rand;

    public Game(String boardPath) {
         try {
            boardFromFile(boardPath);
        } catch (IOException e) {
            System.exit(1);
        }
        rand = new Random();
    }

    public Game(String boardPath, long seed) {
        this(boardPath);
        rand.setSeed(seed);
    }

    public void boardFromFile(String path) throws IOException {

        File file = new File(path);
        if(!file.canRead()) {
            throw new IOException("Cant read from File: " + path);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String ln;

        if ((ln = br.readLine()) != null) {
            boardName = ln;
            System.out.println("Spielfeldfarbe: " + boardName);
        }

        int index = 0;
        int x = 0;
        int y = 0;
        while((ln = br.readLine()) != null) {
            char[] row = ln.toCharArray();
            x = 0;
            for (char f : row) {
                switch (f) {
                    case 'g': boardArr[index] = new BoardField(false, BoardField.FieldColor.GREEN, x, y); index++; break;
                    case 'y': boardArr[index] = new BoardField(false, BoardField.FieldColor.YELLOW, x, y); index++; break;
                    case 'b': boardArr[index] = new BoardField(false, BoardField.FieldColor.BLUE, x, y); index++; break;
                    case 'r': boardArr[index] = new BoardField(false, BoardField.FieldColor.RED, x, y); index++; break;
                    case 'o': boardArr[index] = new BoardField(false, BoardField.FieldColor.ORANGE, x, y); index++; break;
                    case 'G': boardArr[index] = new BoardField(true, BoardField.FieldColor.GREEN, x, y); index++; break;
                    case 'Y': boardArr[index] = new BoardField(true, BoardField.FieldColor.YELLOW, x, y); index++; break;
                    case 'B': boardArr[index] = new BoardField(true, BoardField.FieldColor.BLUE, x, y); index++; break;
                    case 'R': boardArr[index] = new BoardField(true, BoardField.FieldColor.RED, x, y); index++; break;
                    case 'O': boardArr[index] = new BoardField(true, BoardField.FieldColor.ORANGE, x, y); index++; break;
                }
                x++;
            }
            y++;
        }
        System.out.println("Loaded " + index + " Fields from: " + boardName);
    }



    public void initGame(ArrayList<Player> ps) {
        players = ps;
        rounds = 0;
        activePlayer = rand.nextInt(players.size());
        for (Player p : players) {
            p.init(new Board(Arrays.stream(boardArr).map(BoardField::new).toArray(BoardField[]::new), 15, 7));
        }
        //gameUI = new GameUI();
        //gameUI.initUI("Noch mal - " + boardName);
    }

    void rollDice() {
        for (int i = 0; i < 3; i++) {
            colorDice.set(i, rand.nextInt(6));
            numberDice.set(i, rand.nextInt(6));
        }
    }

    public static void printColor(int c) {
        switch (c) {
            case 0:
                System.out.print("Green ");
                break;
            case 1:
                System.out.print("Yellow ");
                break;
            case 2:
                System.out.print("Blue ");
                break;
            case 3:
                System.out.print("Red ");
                break;
            case 4:
                System.out.print("Orange ");
                break;
            case 5:
                System.out.print("Joker ");
                break;
        }
    }

    public static void printNumber(int n) {
        if(n == 5) {
            System.out.print("Joker ");
        } else {
            System.out.print((n + 1) + "  ");
        }
    }

    public static void printDice(Collection<Integer> colorD, Collection<Integer> numberD) {
        System.out.print("Colors: ");
        for (int val: colorD) {
            printColor(val);
        }
        System.out.println();
        System.out.print("Numbers: ");
        for (int val : numberD) {
            printNumber(val);
        }
        System.out.println(" ");
    }

    public void run() {
        boolean isRunning = true;
        while(isRunning) {
            //if(gameUI.getButtonInput() == 1) {
                rollDice();
                //printDice();

                if(rounds >= 3) {
                    ArrayList<Integer> remainingColors = new ArrayList<>(3);
                    ArrayList<Integer> remainingNumbers = new ArrayList<>(3);
                    remainingColors.addAll(colorDice);
                    remainingNumbers.addAll(numberDice);

                    //returns index of selected Dice [0] = color [1] = number
                    int[] selectedDiceIndex = players.get(activePlayer).selectFields(colorDice, numberDice);
                    if(selectedDiceIndex != null) {
                        remainingColors.remove(selectedDiceIndex[0]);
                        remainingNumbers.remove(selectedDiceIndex[1]);
                    }

                    for (int i = 0; i < players.size(); i++) {
                        if(i != activePlayer) {
                            players.get(i).selectFields(remainingColors, remainingNumbers);
                        }
                    }
                } else {
                    for (Player p : players) {
                        p.selectFields(colorDice, numberDice);
                    }
                }
                for (Player p : players) {
                    TreeSet[] complete = p.getComplete();
                    completeColors.addAll(complete[0]);
                    completeColumns.addAll(complete[1]);
                    if(p.isGameComplete()) {
                        isRunning = false;
                    }
                }
                for (Player p : players) {
                    p.updateComplete(completeColors, completeColumns);
                    p.uiUpdateColorComplete();
                    p.uiUpdateColumnsComplete();
                }
            //}
            rounds++;
                activePlayer++;
                if(activePlayer >= players.size()) {
                    activePlayer = 0;
                }
            //isRunning = false;
        }
        for (Player p : players) {
            p.calcScore();
            p.uiUpdateScore();
        }
        players.sort(Comparator.comparingInt(Player::getScore));
        players.get(players.size()-1).won();
    }
}
