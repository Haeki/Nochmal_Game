package haeki.board;

import haeki.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

@SuppressWarnings("CopyConstructorMissesField")
public class Board {
    private final BoardField[] board;
    private final int width;
    private final int height;

    private int uncheckedStars;
    private int score;

    private TreeSet<Integer> completedColumns = new TreeSet<>();
    private final TreeSet<Integer> finishedColumnsFirst = new TreeSet<>();
    private final TreeSet<Integer> finishedColumnsLast = new TreeSet<>();
    private TreeSet<BoardField.FieldColor> completedColors = new TreeSet<>();
    private final TreeSet<BoardField.FieldColor> finishedColorsFirst = new TreeSet<>();
    private final TreeSet<BoardField.FieldColor> finishedColorsLast = new TreeSet<>();
    private final Color boardColor;

    public Board(BoardField[] b, int w, int h, Color boardColor) {
        width = w;
        height = h;
        this.boardColor = boardColor;
        board = b.clone();
        for (int y = 0; y < height; y++) {
            getField(7, y).setAccessible(true);
        }
    }

    public Board(Board board) {
        this.board = Arrays.stream(board.getBoard()).map(BoardField::new).toArray(BoardField[]::new);
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.completedColumns.addAll(board.completedColumns);
        this.boardColor = board.boardColor;
        this.finishedColumnsFirst.addAll(board.finishedColumnsFirst);
        this.finishedColumnsLast.addAll(board.finishedColumnsLast);
        this.completedColors.addAll(board.completedColors);
        this.finishedColorsFirst.addAll(board.finishedColorsFirst);
        this.finishedColorsLast.addAll(board.finishedColorsLast);
    }

    public int getScore() {
        return score;
    }

    public int getUncheckedStars() {
        return uncheckedStars;
    }

    public int getBoardSize() {
        return board.length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private BoardField[] getBoard() {
        return board;
    }

    public BoardField getField(int x, int y) {
        return board[x + (15*y)];
    }

    public BoardField getField(int index) {
        return board[index];
    }

// --Commented out by Inspection START (8/24/19, 11:52 PM):
//    public void updateAccessible() {
//        for (int x = 0; x < 15; x++) {
//            for (int y = 0; y < 7; y++) {
//                if(getField(x, y).isChecked()) {
//                    if(x > 0) {
//                        getField(x - 1, y).setAccessible(true);
//                    }
//                    if(x < 14) {
//                        getField(x + 1, y).setAccessible(true);
//                    }
//                    if(y > 0) {
//                        getField(x,y - 1).setAccessible(true);
//                    }
//                    if(y < 6) {
//                        getField(x,y + 1).setAccessible(true);
//                    }
//                }
//            }
//        }
//    }
// --Commented out by Inspection STOP (8/24/19, 11:52 PM)

    public void updateAccessible(Collection<BoardField> checkedFields) {
        for (BoardField f : checkedFields) {
           if(f.getX() > 0) {
               getField(f.getX() - 1, f.getY()).setAccessible(true);
           }
            if(f.getX() < 14) {
                getField(f.getX() + 1, f.getY()).setAccessible(true);
            }
            if(f.getY() > 0) {
                getField(f.getX(),f.getY() - 1).setAccessible(true);
            }
            if(f.getY() < 6) {
                getField(f.getX(),f.getY() + 1).setAccessible(true);
            }
        }
    }

    public ArrayList<BoardField> getNeighboursWithSameColor(BoardField boardField) {
        ArrayList<BoardField> neighbours = new ArrayList<>();
        if(boardField.getX() > 0) {
            BoardField bf = getField(boardField.getX() - 1, boardField.getY());
            if(bf.getFieldColor() == boardField.getFieldColor()) {
                neighbours.add(bf);
            }
        }
        if(boardField.getX() < 14) {
            BoardField bf = getField(boardField.getX() + 1, boardField.getY());
            if(bf.getFieldColor() == boardField.getFieldColor()) {
                neighbours.add(bf);
            }
        }
        if(boardField.getY() > 0) {
            BoardField bf = getField(boardField.getX(),boardField.getY() - 1);
            if(bf.getFieldColor() == boardField.getFieldColor()) {
                neighbours.add(bf);
            }
        }
        if(boardField.getY() < 6) {
            BoardField bf = getField(boardField.getX(),boardField.getY() + 1);
            if(bf.getFieldColor() == boardField.getFieldColor()) {
                neighbours.add(bf);
            }
        }
        return neighbours;
    }

    public void setChecked(Collection<BoardField> checkedFields) {
        for(BoardField bf : checkedFields) {
            getField(bf.getIndex()).setChecked(true);
        }
    }

    public boolean isGameComplete() {
        return (finishedColorsFirst.size() + finishedColorsLast.size()) >= 2;
    }

    public void calcScore(int remainingJokers) {
        checkComplete();
        score = 0;
        score += (finishedColorsFirst.size() * 5);
        score += (finishedColorsLast.size() * 3);
        for (int i: finishedColumnsFirst) {
            score += Game.pointsFirst[i];
        }
        for (int i: finishedColumnsLast) {
            score += Game.pointsLast[i];
        }
        score -= (uncheckedStars * 2);
        score += remainingJokers;
    }

    public void checkComplete() {
        uncheckedStars = 0;
        boolean[] colorFinished = new boolean[] {true, true, true, true, true};
        for (int x = 0; x < getWidth(); x++) {
            boolean columnFinish = true;
            for (int y = 0; y < getHeight(); y++) {
                BoardField bf = getField(x, y);
                if(!bf.isChecked()) {
                    columnFinish = false;
                    if(bf.isStar()) {
                        uncheckedStars++;
                    }
                    colorFinished[bf.getFieldColor().getIndex()] = false;
                }
            }
            if(columnFinish) {
                if(!finishedColumnsFirst.contains(x) && !finishedColumnsLast.contains(x)) {
                    if (completedColumns.contains(x)) {
                        finishedColumnsLast.add(x);
                    } else {
                        finishedColumnsFirst.add(x);
                    }
                }
            }
        }

        for (int i = 0; i < colorFinished.length; i++) {
            if(colorFinished[i]) {
                BoardField.FieldColor col = BoardField.FieldColor.get(i);
                if(!finishedColorsFirst.contains(col) && !finishedColorsLast.contains(col)) {
                    if (completedColors.contains(col)) {
                        finishedColorsLast.add(col);
                    } else {
                        finishedColorsFirst.add(col);
                    }
                }
            }
        }
        //System.out.println("FinishedColors: " + finishedColorsFirst);
        //System.out.println("FinishedColumns: " + finishedColumnsFirst);
        //System.out.println("uncheckedStars: " + uncheckedStars);
    }

    public TreeSet<Integer> getCompletedColumns() {
        return completedColumns;
    }

    public TreeSet<Integer> getFinishedColumnsFirst() {
        return finishedColumnsFirst;
    }

    public TreeSet<Integer> getFinishedColumnsLast() {
        return finishedColumnsLast;
    }

    public TreeSet<BoardField.FieldColor> getCompletedColors() {
        return completedColors;
    }

    public TreeSet<BoardField.FieldColor> getFinishedColorsFirst() {
        return finishedColorsFirst;
    }

    public TreeSet<BoardField.FieldColor> getFinishedColorsLast() {
        return finishedColorsLast;
    }


    public Color getColor() {
        return boardColor;
    }

    public void setCompletedColumns(TreeSet<Integer> completeColumns) {
        this.completedColumns = completeColumns;
    }

    public void setCompletedColors(TreeSet<BoardField.FieldColor> completedColors) {
        this.completedColors = completedColors;
    }
}
