package haeki.player;

import haeki.board.Board;
import haeki.board.BoardField;
import haeki.Game;
import haeki.ui.UI;

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

    public void init(Board board) {
        this.board = board;
    }

    public void uiUpdateColorComplete() {
        ui.updateColorFinished(board.getFinishedColorsFirst(), board.getFinishedColorsLast(), board.getCompletedColors());
    }

    public void uiUpdateColumnsComplete() {
        ui.updateColumnFinished(board.getFinishedColumnsFirst(), board.getFinishedColumnsLast(), board.getCompletedColumns());
    }

    public void uiUpdateScore() {
        int[] scores = new int[5];
        scores[0] = (board.getFinishedColorsFirst().size() * 5) + (board.getFinishedColorsLast().size() * 3);
        int columnsScore = 0;
        for (int i: board.getFinishedColumnsFirst()) {
            columnsScore += Game.pointsFirst[i];
        }
        for (int i: board.getFinishedColumnsLast()) {
            columnsScore += Game.pointsLast[i];
        }
        scores[1] = columnsScore;
        scores[2] = remainingJokers;
        scores[3] = (board.getUncheckedStars() * 2);
        scores[4] = board.getScore();
        ui.updateScore(scores);
    }

    public void checkComplete() {
        //System.out.println("Checking if haeki.player.Player: " + playerNum + " has completed something");
        board.checkComplete();
    }

    public TreeSet[] getComplete() {
        board.checkComplete();
        return new TreeSet[] {board.getFinishedColorsFirst(), board.getFinishedColumnsFirst()};
    }

    public void updateComplete(TreeSet<BoardField.FieldColor> completedColors, TreeSet<Integer> completeColumns) {
        board.setCompletedColumns(completeColumns);
        board.setCompletedColors(completedColors);
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
