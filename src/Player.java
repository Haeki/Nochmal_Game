import java.util.ArrayList;
import java.util.TreeSet;

public abstract class Player {

    int playerNum;
    Board board;
    UI ui;
    int remainingJokers = 8;


    public int getScore() {
        return board.getScore();
    }

    //abstract public void selectFields(int col, int num);

    abstract public int[] selectFields(ArrayList<Integer> col, ArrayList<Integer> num);

    void init(Board board) {
        this.board = board;
    }

    void uiUpdateColorComplete() {
        ui.updateColorFinished(board.finishedColorsFirst, board.finishedColorsLast, board.completedColors);
    }

    void uiUpdateColumnsComplete() {
        ui.updateColumnFinished(board.finishedColumnsFirst, board.finishedColumnsLast, board.completeColumns);
    }

    public void uiUpdateScore() {
        int[] scores = new int[5];
        scores[0] = (board.finishedColorsFirst.size() * 5) + (board.finishedColorsLast.size() * 3);
        int columnsScore = 0;
        for (int i: board.finishedColumnsFirst) {
            columnsScore += Game.pointsFirst[i];
        }
        for (int i: board.finishedColumnsLast) {
            columnsScore += Game.pointsLast[i];
        }
        scores[1] = columnsScore;
        scores[2] = remainingJokers;
        scores[3] = (board.getUncheckedStars() * 2);
        scores[4] = board.getScore();
        ui.updateScore(scores);
    }

    public void checkComplete() {
        //System.out.println("Checking if Player: " + playerNum + " has completed something");
        board.checkComplete();
    }

    public TreeSet[] getComplete() {
        board.checkComplete();
        return new TreeSet[] {board.finishedColorsFirst, board.finishedColumnsFirst};
    }

    public void updateComplete(TreeSet<BoardField.FieldColor> completeColors, TreeSet<Integer> completeColumns) {
        board.completeColumns = completeColumns;
        board.completedColors = completeColors;
    }

    public void calcScore() {
        board.calcScore(remainingJokers);
    }

    public boolean isGameComplete() {
        return board.isGameComplete();
    }

    public void won() {
        System.out.println("Player " + playerNum + " has won with " + board.getScore() + " Points");
    }
}
